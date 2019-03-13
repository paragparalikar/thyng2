$.subscribe("show-confirmation-modal", function(event, data, callback){
	$("#modal-container").loadTemplate("pages/utils/confirmation.html", data, {
		success : function(){
			$("#modal-container").modal();
			if(callback){
				$("#confirmation-action-button").click(function(){
					callback();
				});
			}
		}
	});
});