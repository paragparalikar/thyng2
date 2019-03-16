var showDashboard = function(){
    $("#template-container").loadTemplate("pages/dashboard/dashboard.html");
}
$.router.add("#dashboard", showDashboard);
$.router.add("", showDashboard);
$.router.add("/", showDashboard);
$.router.add("/#", showDashboard);