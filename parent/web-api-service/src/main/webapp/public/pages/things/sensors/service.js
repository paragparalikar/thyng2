var sensorService = {
		findByThingId : function(thingId, success, error, always) {
			$.get(window.location.origin + "/api/v1/sensors?thingId=" + thingId)
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		findById : function(id, success, error, always){
			$.get(window.location.origin + "/api/v1/sensors/"+id)
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		save : function(thingId, sensor, success, error, always){
			$.ajax({
				url: window.location.origin + "/api/v1/sensors?thingId="+thingId,
				type: sensor.id && 0 < sensor.id ? "PUT" : "POST",
				contentType:"application/json; charset=utf-8",
				data: JSON.stringify(sensor),
			}).done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		deleteById : function(id, success, error, always){
			$.ajax({
				url: window.location.origin + "/api/v1/sensors/" + id,
				type: "DELETE",
			}).done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		}
};


$.subscribe("show-sensor-view-modal", function(event, thingId, sensorId){
	$("#modal-container-1").loadTemplate("public/pages/things/sensors/view.jsp", null, {
		beforeInsert: beforeTemplateInsert,
		success : function(){
			$("#modal-container-1").modal({
				closeExisting: false
			});
			sensorService.findById(sensorId, function(sensor){
				renderViewSensorModal(sensor);
			});
		},
    	error: errorCallback
	});
});

$.subscribe("show-sensor-edit-modal", function(event, thingId, sensorId, callback){
	$("#modal-container-1").loadTemplate("public/pages/things/sensors/edit.jsp", null, {
		beforeInsert: beforeTemplateInsert,
		success : function(){
			$("#modal-container-1").modal({
				closeExisting: false
			});
			var form = $("#sensor-form");
			form.on("sensor-edit-cancel", function(event, data){
				$.modal.close();
				form.unbind(event);
			});
			form.on("save-sensor", function(event, thingId, sensor){
				sensorService.save(thingId, sensor, function(data){
					toast('Sensor "'+data.name+'" has been saved successfully');
					$.modal.close();
					form.unbind(event);
					if(callback){
						callback(data);
					}
				});
			});
			if(sensorId && 0 < sensorId){
				sensorService.findById(sensorId, function(sensor){
					renderEditSensorView(thingId, sensor);
				});
			}else{
				renderEditSensorView(thingId, null);
			}
		},
    	error: errorCallback
	});
});

$.subscribe("show-sensor-delete-modal", function(event, thingId, sensor, callback){
	$("#modal-container-1").loadTemplate("public/pages/utils/confirmation.html", {
        message: "Are you sure you want to delete sensor " + sensor.name + " ?"
    }, {
    	beforeInsert: beforeTemplateInsert,
		success : function(){
			$("#modal-container-1").modal({
				closeExisting: false
			});
			$("#confirmation-action-button").click(function(){
				sensorService.deleteById(sensor.id, function () {
					 $.modal.close();
		        	toast('Sensor "'+sensor.name+'" has been deleted successfully');
		        	if(callback){
		        		callback();
		        	}
		        });
			});
		},
    	error: errorCallback
	});
});