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
	if(sensorId && 0 < sensorId){
		sensorService.findById(thingId, sensorId, function(sensor){
			sensor.thingId = thingId;
			showSensorEditModal(sensor, callback);
		});
	}else{
		var sensor = {};
		sensor.thingId = thingId;
		showSensorEditModal(sensor, callback);
	}
});
var showSensorEditModal = function(sensor, callback){
	$("#modal-container").loadTemplate("public/pages/things/sensors/edit.jsp", sensor, {
		success : function(){
			$("#modal-container").modal();
			$("#sensor-form").on("sensor-save-success", function(event, data){
				if(callback){
					callback(data);
				}
				$.modal.close();
			});
		}
	});
}
