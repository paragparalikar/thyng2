$(".content-wrapper").load($("#menu-dashboard").addClass("active").attr("targetUrl"));
$(".sidebar-menu > li").click(function(){
	if(!$(this).hasClass("active")){
		$(".sidebar-menu > li").removeClass("active");
		$(".content-wrapper").load($(this).addClass("active").attr("targetUrl"));
	}
});


var confirm = function(title, text, cancelCallback, confirmCallback){
	
}
