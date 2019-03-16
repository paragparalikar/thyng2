<html>

<head>
<title>Thyng</title>
<link rel="stylesheet" href="libs/bootstrap/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="libs/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="libs/jquery-modal/0.9.1/jquery.modal.min.css">
<link rel="stylesheet" href="libs\datatables.net-bs\css\dataTables.bootstrap.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
<style type="text/css">
.modal {
	width: auto;
	max-width: none;
	padding: 0;
}
#page-wrapper{
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
}
#sidenav{
	position: absolute;
	top: 0;
	left: 0;
	bottom: 0;
	z-index: 999;
	background-color: #212529;
	border-top: 1px solid #3b3b3b;
	background-image: url(/images/bg1.jpg);
	background-repeat: no-repeat;
	background-size: cover;
	background-blend-mode: overlay;
}
#sidenav > ul{
	list-style-type: none;
	padding-left: 0;
}
#sidenav > ul > li > a > i{
	font-size: 16px;
	padding: 17px;
}
#page-wrapper.nav-open #sidenav > ul > li > a{
	color: #cccccc;
}
#page-wrapper:not(.nav-open) #sidenav > ul > li > a{
	color: white;
}
#sidenav > ul > li > a.active{
	color: yellow !important;
}
#sidnav-toggle, #sidnav-toggle:FOCUS{
	color: white;
	background-color: transparent;
	border-width: 0;
	height: 100%;
	outline: 0;
}
#page-wrapper.nav-open #sidenav{
	width: 250px;
}
#page-wrapper:not(.nav-open) #sidenav{
	width: 50px;
}
#page-header{
	position: absolute;
	top: 0;
	right: 0;
	height: 50px;
	background-color: #212529;
}

#page-wrapper:not(.nav-open) #page-header,
#page-wrapper:not(.nav-open) #page-container{
	left: 50px;
}
#page-wrapper.nav-open #page-header,
#page-wrapper.nav-open #page-container{
	left: 250px;
}
#page-container {
	position: absolute;
	top: 50px;
	right: 0;
	bottom: 0;
}
#sidenav, #page-header, #page-container, #logo, .menu-text{
	-webkit-transition: left .3s ease, width .3s ease, display .3s linear;
	transition: left .3s ease, width .3s ease, display .3s linear;
}
#logo, #logo-sm{
	height: 50px;
	font-weight: bold;
	font-size: 1.2em;
	color: white;
	line-height: 40px;
	text-align: center;
}
#page-wrapper.nav-open .menu-text{
	display: inline-block;
}
#page-wrapper.nav-open #logo,
#page-wrapper:not(.nav-open) #logo-sm{
	display: block;
}
#page-wrapper:not(.nav-open) #logo,
#page-wrapper:not(.nav-open) .menu-text,
#page-wrapper.nav-open #logo-sm{
	display: none;
}


</style>
</head>
<body>
	<div id="page-wrapper" class="nav-open">
		<header id="page-header">
			<button id="sidnav-toggle">
				<i class="fa fa-bars"></i>
			</button>
			
		</header>
		<aside id="sidenav">
			<div id="logo-sm">T</div>
			<div id="logo">THYNG</div>
			<ul>
				<li><a class="router-link" href="#dashboard" route-title="Dashboard"><i class="fa fa-tachometer"></i> <span class="menu-text">Dashboard</span></a></li>
				<li><a class="router-link" href="#templates" route-title="Templates"><i class="fa fa-clone"></i> <span class="menu-text">Templates</span></a></li>
				<li><a class="router-link" href="#things" route-title="Things"><i class="fa fa-bell"></i> <span class="menu-text">Things</span></a></li>
				<li><a class="router-link" href="#alerts" route-title="Alerts"><i class="fa fa-bell"></i> <span class="menu-text">Alerts</span></a></li>
				<li><a class="router-link" href="#users" route-title="Users"><i class="fa fa-user"></i> <span class="menu-text">Users</span></a></li>
			</ul>
		</aside>
		<main id="page-container">main</main>
	</div>
	<script src="libs/jquery/dist/jquery.min.js"></script>
	<script src="libs\jquery\dist\jquery-migrate-1.0.0.min.js"></script>
	<script src="libs/jquery-tiny-pubsub/ba-tiny-pubsub.min.js"></script>
	<script src="libs/loadTemplate/jquery.loadTemplate.js"></script>
	<script src="libs/bootstrap/dist/js/bootstrap.min.js"></script>
	<script src="libs/datatables.net/js/jquery.dataTables.min.js"></script>
	<script src="libs/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
	<script src="libs/jquery-modal/0.9.1/jquery.modal.min.js"></script>
	<script src="libs/jquery-hashchange/jquery.ba-hashchange.min.js"></script>
	<script src="plugins/jquery/jQuery-router.js"></script>
	<script src="pages/dashboard/service.js"></script>
	<script src="pages/templates/service.js"></script>
	
	<script>
		const user = {
			name: "Parag Paralikar",
			email: "parag.paralikar@bnymellon.com",
			authorities: ["LIST_TEMPLATES"]
		};
		$(function(){
			//loadTemplates
			$.addTemplateFormatter("MapFormatter", function(value, template) {
	            return $.map(value, function(val,key){
	        		return key + "=" + val;
	        	}).join("\n");
	        });	
			
			// Sidebar
			$("#sidnav-toggle").click(function(){
				$("#page-wrapper").toggleClass("nav-open");
			});	
			$("#sidenav > ul > li > a").click(function(){
				$("#sidenav > ul > li > a").removeClass("active");
				$(this).toggleClass("active");
			});
	        
	        //Router
			$.router.start();
			$(window).hashchange();
		});
	</script>
</body>

</html>