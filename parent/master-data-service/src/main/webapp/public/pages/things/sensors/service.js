var sensorService = {
		findByThingId : function(thingId, success, error, always) {
			$.get(window.location.origin + "/api/v1/things/"+thingId+"/sensors")
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		findById : function(thingId, id, success, error, always){
			$.get(window.location.origin + "/api/v1/things/"+thingId+"/sensors/"+id)
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		save : function(thingId, sensor, success, error, always){
			$.ajax({
				url: window.location.origin + "/api/v1/things/"+thingId+"/sensors",
				type: sensor.id && 0 < sensor.id ? "PUT" : "POST",
				contentType:"application/json; charset=utf-8",
				data: JSON.stringify(sensor),
			}).done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		deleteById : function(thingId, id, success, error, always){
			$.ajax({
				url: window.location.origin + "/api/v1/things/" + thingId + "/sensors/" + id,
				type: "DELETE",
			}).done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		}
};
$.subscribe("show-sensor-edit-modal", function(event, thingId, sensorId, callback){
	$("#sensor-modal-container").loadTemplate("public/pages/things/sensors/edit.jsp", null, {
		success : function(){
			$("#sensor-modal-container").modal({
				closeExisting: false
			});
			var form = $("#sensor-form");
			renderEditSensorView(thingId, sensorId);
			form.on("sensor-edit-cancel", function(event, data){
				$.modal.close();
				form.unbind(event);
			});
			form.on("sensor-save-success", function(event, data){
				$.modal.close();
				form.unbind(event);
				if(callback){
					callback(data);
				}
			});
		}
	});
});
