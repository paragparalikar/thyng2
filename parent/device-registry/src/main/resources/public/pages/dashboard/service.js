var showDashboard = function(){
    $("#page-container").loadTemplate("pages/dashboard/dashboard.html");
}
$.router.add("#dashboard", showDashboard);
$.router.add("", showDashboard);
$.router.add("/", showDashboard);
$.router.add("/#", showDashboard);