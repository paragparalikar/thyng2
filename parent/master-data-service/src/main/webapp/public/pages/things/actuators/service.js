var actuatorService = {
		findByThingId : function(thingId, success, error, always) {
			$.get(window.location.origin + "/api/v1/actuators?thingId=" + thingId)
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		findById : function(id, success, error, always){
			$.get(window.location.origin + "/api/v1/actuators/"+id)
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		save : function(thingId, actuator, success, error, always){
			$.ajax({
				url: window.location.origin + "/api/v1/actuators?thingId="+thingId,
				type: actuator.id && 0 < actuator.id ? "PUT" : "POST",
				contentType:"application/json; charset=utf-8",
				data: JSON.stringify(actuator),
			}).done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		deleteById : function(id, success, error, always){
			$.ajax({
				url: window.location.origin + "/api/v1/actuators/" + id,
				type: "DELETE",
			}).done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		}
};


$.subscribe("show-actuator-view-modal", function(event, thingId, actuatorId){
	$("#modal-container-1").loadTemplate("public/pages/things/actuators/view.jsp", null, {
		beforeInsert: beforeTemplateInsert,
		success : function(){
			$("#modal-container-1").modal({
				closeExisting: false
			});
			actuatorService.findById(actuatorId, function(actuator){
				renderViewActuatorModal(actuator);
			});
		},
    	error: errorCallback
	});
});

$.subscribe("show-actuator-edit-modal", function(event, thingId, actuatorId, callback){
	$("#modal-container-1").loadTemplate("public/pages/things/actuators/edit.jsp", null, {
		beforeInsert: beforeTemplateInsert,
		success : function(){
			$("#modal-container-1").modal({
				closeExisting: false
			});
			var form = $("#actuator-form");
			form.on("actuator-edit-cancel", function(event, data){
				$.modal.close();
				form.unbind(event);
			});
			form.on("save-actuator", function(event, thingId, actuator){
				actuatorService.save(thingId, actuator, function(data){
					toast('Actuator "'+data.name+'" has been saved successfully');
					$.modal.close();
					form.unbind(event);
					if(callback){
						callback(data);
					}
				});
			});
			if(actuatorId && 0 < actuatorId){
				actuatorService.findById(actuatorId, function(actuator){
					renderEditActuatorView(thingId, actuator);
				});
			}else{
				renderEditActuatorView(thingId, null);
			}
		},
    	error: errorCallback
	});
});

$.subscribe("show-actuator-delete-modal", function(event, thingId, actuator, callback){
	$("#modal-container-1").loadTemplate("public/pages/utils/confirmation.html", {
        message: "Are you sure you want to delete actuator " + actuator.name + " ?"
    }, {
    	beforeInsert: beforeTemplateInsert,
		success : function(){
			$("#modal-container-1").modal({
				closeExisting: false
			});
			$("#confirmation-action-button").click(function(){
				actuatorService.deleteById(actuator.id, function () {
					 $.modal.close();
		        	toast('Actuator "'+actuator.name+'" has been deleted successfully');
		        	if(callback){
		        		callback();
		        	}
		        });
			});
		},
    	error: errorCallback
	});
});