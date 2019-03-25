errorCallback = null;
toast = null;

$(function() {
	toast = function(message, title, toastIcon){
		$.toast({
    		heading: title ? title : "Success",
    	    text: message,
    	    showHideTransition: 'slide',
    	    position: 'top-right',
    	    icon: toastIcon ? toastIcon : 'success'
    	});
	};
	errorCallback = function(xhr, status, text){
		var message = "<p>There has been an error.<br>Please contact <a href='http://www.google.com'>Administrator</a>.";
		message = text ? message + "<br>Error : "+text + "</p>" : message + "</p>";
		$.toast({
    		heading: "Error",
    	    text: message,
    	    showHideTransition: 'slide',
    	    position: 'top-right',
    	    hideAfter: false,
    	    icon: 'error'
    	});
	};
	// loadTemplates
	$.addTemplateFormatter("MapFormatter", function(value, template) {
		return $.map(value, function(val, key) {
			return key + "=" + val;
		}).join("\n");
	});
	$(document).on("submit","form",function(event){
		event.preventDefault();
		return false;
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
	$.validator.addMethod("notInArray", function(value, element, params){
		var values = params.provider();
		return this.optional(element) || $.inArray(value.trim(), values) === -1;
	}, "Value is not unique");
	
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