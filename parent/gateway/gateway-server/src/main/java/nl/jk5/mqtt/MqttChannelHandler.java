package nl.jk5.mqtt;

import com.google.common.collect.ImmutableSet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.concurrent.Promise;

final class MqttChannelHandler extends SimpleChannelInboundHandler<MqttMessage> {

    private final MqttClientImpl client;
    private final Promise<MqttConnectResult> connectFuture;

    MqttChannelHandler(MqttClientImpl client, Promise<MqttConnectResult> connectFuture) {
        this.client = client;
        this.connectFuture = connectFuture;
    }

    @SuppressWarnings("incomplete-switch")
	@Override
    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {
        switch (msg.fixedHeader().messageType()){
            case CONNACK:
                handleConack(ctx.channel(), (MqttConnAckMessage) msg);
                break;
            case SUBACK:
                handleSubAck((MqttSubAckMessage) msg);
                break;
            case PUBLISH:
                handlePublish(ctx.channel(), (MqttPublishMessage) msg);
                break;
            case UNSUBACK:
                handleUnsuback((MqttUnsubAckMessage) msg);
                break;
            case PUBACK:
                handlePuback((MqttPubAckMessage) msg);
                break;
            case PUBREC:
                handlePubrec(ctx.channel(), msg);
                break;
            case PUBREL:
                handlePubrel(ctx.channel(), msg);
                break;
            case PUBCOMP:
                handlePubcomp(msg);
                break;
        }
    }

    @SuppressWarnings("deprecation")
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttConnectVariableHeader variableHeader = new MqttConnectVariableHeader(
                this.client.getClientConfig().getProtocolVersion().protocolName(),  // Protocol Name
                this.client.getClientConfig().getProtocolVersion().protocolLevel(), // Protocol Level
                this.client.getClientConfig().getUsername() != null,                // Has Username
                this.client.getClientConfig().getPassword() != null,                // Has Password
                this.client.getClientConfig().getLastWill() != null                 // Will Retain
                        && this.client.getClientConfig().getLastWill().isRetain(),
                this.client.getClientConfig().getLastWill() != null                 // Will QOS
                        ? this.client.getClientConfig().getLastWill().getQos().value()
                        : 0,
                this.client.getClientConfig().getLastWill() != null,                // Has Will
                this.client.getClientConfig().isCleanSession(),                     // Clean Session
                this.client.getClientConfig().getTimeoutSeconds()                   // Timeout
        );
        MqttConnectPayload payload = new MqttConnectPayload(
                this.client.getClientConfig().getClientId(),
                this.client.getClientConfig().getLastWill() != null ? this.client.getClientConfig().getLastWill().getTopic() : null,
                this.client.getClientConfig().getLastWill() != null ? this.client.getClientConfig().getLastWill().getMessage() : null,
                this.client.getClientConfig().getUsername(),
                this.client.getClientConfig().getPassword()
        );
        ctx.channel().writeAndFlush(new MqttConnectMessage(fixedHeader, variableHeader, payload));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    private void invokeHandlersForIncomingPublish(MqttPublishMessage message){
        for (MqttSubscribtion subscribtion : ImmutableSet.copyOf(this.client.getSubscriptions().values())) {
            if(subscribtion.matches(message.variableHeader().topicName())){
                if(subscribtion.isOnce() && subscribtion.isCalled()){
                    continue;
                }
                message.payload().markReaderIndex();
                subscribtion.setCalled(true);
                subscribtion.getHandler().onMessage(message.variableHeader().topicName(), message.payload());
                if(subscribtion.isOnce()){
                    this.client.off(subscribtion.getTopic(), subscribtion.getHandler());
                }
                message.payload().resetReaderIndex();
            }
        }
        /*Set<MqttSubscribtion> subscribtions = ImmutableSet.copyOf(this.client.getSubscriptions().get(message.variableHeader().topicName()));
        for (MqttSubscribtion subscribtion : subscribtions) {
            if(subscribtion.isOnce() && subscribtion.isCalled()){
                continue;
            }
            message.payload().markReaderIndex();
            subscribtion.setCalled(true);
            subscribtion.getHandler().onMessage(message.variableHeader().topicName(), message.payload());
            if(subscribtion.isOnce()){
                this.client.off(subscribtion.getTopic(), subscribtion.getHandler());
            }
            message.payload().resetReaderIndex();
        }*/
        message.payload().release();
    }

    private void handleConack(Channel channel, MqttConnAckMessage message){
        switch(message.variableHeader().connectReturnCode()){
            case CONNECTION_ACCEPTED:
                this.connectFuture.setSuccess(new MqttConnectResult(true, MqttConnectReturnCode.CONNECTION_ACCEPTED, channel.closeFuture()));

                this.client.getPendingSubscribtions().entrySet().stream().filter((e) -> !e.getValue().isSent()).forEach((e) -> {
                    channel.write(e.getValue().getSubscribeMessage());
                    e.getValue().setSent(true);
                });

                this.client.getPendingPublishes().forEach((id, publish) -> {
                    if(publish.isSent()) return;
                    channel.write(publish.getMessage());
                    publish.setSent(true);
                    if(publish.getQos() == MqttQoS.AT_MOST_ONCE){
                        publish.getFuture().setSuccess(null); //We don't get an ACK for QOS 0
                        this.client.getPendingPublishes().remove(publish.getMessageId());
                    }
                });
                channel.flush();
                break;

            case CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD:
            case CONNECTION_REFUSED_IDENTIFIER_REJECTED:
            case CONNECTION_REFUSED_NOT_AUTHORIZED:
            case CONNECTION_REFUSED_SERVER_UNAVAILABLE:
            case CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION:
                this.connectFuture.setSuccess(new MqttConnectResult(false, message.variableHeader().connectReturnCode(), channel.closeFuture()));
                channel.close();
                // Don't start reconnect logic here
                break;
        }
    }

    private void handleSubAck(MqttSubAckMessage message){
        MqttPendingSubscribtion pendingSubscribtion = this.client.getPendingSubscribtions().get(message.variableHeader().messageId());
        if(pendingSubscribtion == null){
            return;
        }
        pendingSubscribtion.onSubackReceived();
        for (MqttPendingSubscribtion.MqttPendingHandler handler : pendingSubscribtion.getHandlers()) {
            MqttSubscribtion subscribtion = new MqttSubscribtion(pendingSubscribtion.getTopic(), handler.getHandler(), handler.isOnce());
            this.client.getSubscriptions().put(pendingSubscribtion.getTopic(), subscribtion);
            this.client.getHandlerToSubscribtion().put(handler.getHandler(), subscribtion);
        }
        this.client.getPendingSubscribeTopics().remove(pendingSubscribtion.getTopic());

        this.client.getServerSubscribtions().add(pendingSubscribtion.getTopic());
        pendingSubscribtion.getFuture().setSuccess(null);
    }

    @SuppressWarnings("incomplete-switch")
	private void handlePublish(Channel channel, MqttPublishMessage message){
        switch (message.fixedHeader().qosLevel()){
            case AT_MOST_ONCE:
                invokeHandlersForIncomingPublish(message);
                break;

            case AT_LEAST_ONCE:
                invokeHandlersForIncomingPublish(message);
                if(message.variableHeader().packetId() != -1){
                    MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
                    MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(message.variableHeader().packetId());
                    channel.writeAndFlush(new MqttPubAckMessage(fixedHeader, variableHeader));
                }
                break;

            case EXACTLY_ONCE:
                if(message.variableHeader().packetId() != -1){
                    MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PUBREC, false, MqttQoS.AT_MOST_ONCE, false, 0);
                    MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(message.variableHeader().packetId());
                    MqttMessage pubrecMessage = new MqttMessage(fixedHeader, variableHeader);

                    MqttIncomingQos2Publish incomingQos2Publish = new MqttIncomingQos2Publish(message, pubrecMessage);
                    this.client.getQos2PendingIncomingPublishes().put(message.variableHeader().packetId(), incomingQos2Publish);
                    message.payload().retain();
                    incomingQos2Publish.startPubrecRetransmitTimer(this.client.getEventLoop().next(), this.client::sendAndFlushPacket);

                    channel.writeAndFlush(pubrecMessage);
                }
                break;
        }
    }

    private void handleUnsuback(MqttUnsubAckMessage message){
        MqttPendingUnsubscribtion unsubscribtion = this.client.getPendingServerUnsubscribes().get(message.variableHeader().messageId());
        if(unsubscribtion == null){
            return;
        }
        unsubscribtion.onUnsubackReceived();
        this.client.getServerSubscribtions().remove(unsubscribtion.getTopic());
        unsubscribtion.getFuture().setSuccess(null);
        this.client.getPendingServerUnsubscribes().remove(message.variableHeader().messageId());
    }

    private void handlePuback(MqttPubAckMessage message){
        MqttPendingPublish pendingPublish = this.client.getPendingPublishes().get(message.variableHeader().messageId());
        pendingPublish.getFuture().setSuccess(null);
        pendingPublish.onPubackReceived();
        this.client.getPendingPublishes().remove(message.variableHeader().messageId());
        pendingPublish.getPayload().release();
    }

    private void handlePubrec(Channel channel, MqttMessage message){
        MqttPendingPublish pendingPublish = this.client.getPendingPublishes().get(((MqttMessageIdVariableHeader) message.variableHeader()).messageId());
        pendingPublish.onPubackReceived();

        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PUBREL, false, MqttQoS.AT_LEAST_ONCE, false, 0);
        MqttMessageIdVariableHeader variableHeader = (MqttMessageIdVariableHeader) message.variableHeader();
        MqttMessage pubrelMessage = new MqttMessage(fixedHeader, variableHeader);
        channel.writeAndFlush(pubrelMessage);

        pendingPublish.setPubrelMessage(pubrelMessage);
        pendingPublish.startPubrelRetransmissionTimer(this.client.getEventLoop().next(), this.client::sendAndFlushPacket);
    }

    private void handlePubrel(Channel channel, MqttMessage message){
        if(this.client.getQos2PendingIncomingPublishes().containsKey(((MqttMessageIdVariableHeader) message.variableHeader()).messageId())){
            MqttIncomingQos2Publish incomingQos2Publish = this.client.getQos2PendingIncomingPublishes().get(((MqttMessageIdVariableHeader) message.variableHeader()).messageId());
            this.invokeHandlersForIncomingPublish(incomingQos2Publish.getIncomingPublish());
            incomingQos2Publish.onPubrelReceived();
            this.client.getQos2PendingIncomingPublishes().remove(incomingQos2Publish.getIncomingPublish().variableHeader().packetId());
        }
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PUBCOMP, false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(((MqttMessageIdVariableHeader) message.variableHeader()).messageId());
        channel.writeAndFlush(new MqttMessage(fixedHeader, variableHeader));
    }

    private void handlePubcomp(MqttMessage message){
        MqttMessageIdVariableHeader variableHeader = (MqttMessageIdVariableHeader) message.variableHeader();
        MqttPendingPublish pendingPublish = this.client.getPendingPublishes().get(variableHeader.messageId());
        pendingPublish.getFuture().setSuccess(null);
        this.client.getPendingPublishes().remove(variableHeader.messageId());
        pendingPublish.getPayload().release();
        pendingPublish.onPubcompReceived();
    }
}
