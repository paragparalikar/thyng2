var actuatorService = {
		findByThingId : function(thingId, success, error, always) {
			$.get(window.location.origin + "/api/v1/things/"+thingId+"/actuators")
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		findById : function(thingId, id, success, error, always){
			$.get(window.location.origin + "/api/v1/things/"+thingId+"/actuators/"+id)
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		save : function(thingId, sensor){
			$.ajax({
				url: window.location.origin + "/api/v1/things/"+thingId+"/actuators",
				type: sensor.id && 0 < sensor.id ? "PUT" : "POST",
				contentType:"application/json; charset=utf-8",
				data: JSON.stringify(sensor),
			}).done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		deleteById : function(thingId, id, success, error, always){
			$.ajax({
				url: window.location.origin + "/api/v1/things/" + thingId + "/actuators/" + id,
				type: "DELETE",
			}).done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		}
};