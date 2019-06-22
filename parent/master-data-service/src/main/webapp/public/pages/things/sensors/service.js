var sensorService = {
		findByThingId : function(thingId, success, error, always) {
			$.get(window.location.origin + "/api/v1/things/"+thingId+"/sensors")
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		findById : function(thingId, id, success, error, always){
			$.get(window.location.origin + "/api/v1/things/"+thingId+"/sensors/"+id)
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		save : function(thingId, sensor){
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