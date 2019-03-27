var thingService = {
	findAll : function(success, error, always) {
		$.get(window.location.origin + "/api/v1/things")
		.done(success).fail(error ? error : errorCallback).always(always);
	},
	findOne : function(id, success, error, always) {
		$.get(window.location.origin + "/api/v1/things/" + id)
		.done(success).fail(error ? error : errorCallback).always(always);
	},
	save: function(thing, success, error, always){
		$.ajax({
			url: window.location.origin + "/api/v1/things",
			type: "POST",
			contentType:"application/json; charset=utf-8",
			data: thing,
		}).done(success).fail(error ? error : errorCallback).always(always);
	},
	deleteById : function(id, success, error, always){
		$.ajax({
			url: window.location.origin + "/api/v1/things/" + id,
			type: "DELETE",
		}).done(success).fail(error ? error : errorCallback).always(always);
	}
};

$.router.add("#things", function(){
    $("#template-container").loadTemplate("list-things", null, {
    	success: function(){
    		render();
    	},
    	error: errorCallback
    });
});

$.router.add("#things/:id", function(params){
	var id = arguments[0];
	$("#template-container").loadTemplate("edit-thing", null, {
		success : function(){
			render(id);
		},
    	error: errorCallback
	});
});

$.subscribe("show-thing-view-modal", function(event, id){
	thingService.findOne(id, function(thing, status){
		$("#modal-container").loadTemplate("view-thing", thing, {
			success : function(){
				$("#modal-container").modal();
				renderModal(thing);
			},
	    	error: errorCallback
		});
	});
});

