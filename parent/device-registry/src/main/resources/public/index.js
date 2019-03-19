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