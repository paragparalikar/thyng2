var showDashboard = function(){
    $("#template-container").loadTemplate("public/pages/dashboard/dashboard.html", null, {
    	error: errorCallback
    });
}
$.router.add("#dashboard", showDashboard);
$.router.add("", showDashboard);
$.router.add("/", showDashboard);
$.router.add("/#", showDashboard);