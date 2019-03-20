$(function() {
	// loadTemplates
	$.addTemplateFormatter("MapFormatter", function(value, template) {
		return $.map(value, function(val, key) {
			return key + "=" + val;
		}).join("\n");
	});
	$(document).on("keypress", ":input:not(textarea)", function(event) {
	    return event.keyCode != 13;
	});
	$.validator.addMethod("propertiesMap", function(value, element, params){
		if(!this.optional(element)){
			var props = value.split("\n");
			for(var index = 0; index < props.length; index++){
				if(props[index]){
					var pair = props[index].split("=");
					if(!pair[0] || pair[0].trim().length === 0 || !pair[1] || pair[1].trim().length === 0){
						return false;
					}
				}
			}
		}
		return true;
	},"Invalid format");
	// Sidebar
	$("#sidnav-toggle").click(function() {
		$("#page-wrapper").toggleClass("nav-open");
	});
	$("#sidenav > ul > li > a").click(function() {
		$("#sidenav > ul > li > a").removeClass("active");
		$(this).toggleClass("active");
	});

	// Router
	$.router.start();
	$(window).hashchange();
});