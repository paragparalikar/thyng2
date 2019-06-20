<%@ page import="com.thyng.user.UserDTO" %>
<%@ page import="com.thyng.aspect.security.Authority" %>
<% final UserDTO user = (UserDTO)session.getAttribute("user"); %>
<html>
<head>
<title>Thyng</title>
<link rel="shortcut icon" type="image/png" href="../public/images/favicon.ico"/>
<link rel="stylesheet" href="../public/libs/fontawesome/css/solid.min.css">
<link rel="stylesheet" href="../public/libs/fontawesome/css/fontawesome.min.css">
<link rel="stylesheet" href="../public/libs/bootstrap/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="../public/libs/bootstrap/dist/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="../public/libs/jquery-modal/0.9.1/jquery.modal.min.css">
<link rel="stylesheet" href="../public/libs/datatables.net-bs/css/dataTables.bootstrap.min.css">
<link rel="stylesheet" href="../public/libs/jquery-toast/jquery.toast.min.css">
<link rel="stylesheet" href="../public/index.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
</head>
<body>
	<div id="page-wrapper" class="nav-open">
		<aside id="sidenav">
			<div id="logo-sm">T</div>
			<div id="logo">THYNG</div>
			<ul>
				<li><a class="router-link" href="#dashboard" route-title="Dashboard"><i class="fas fa-tachometer-alt"></i> <span class="menu-text">Dashboard</span></a></li>
				<% if(user.hasAuthority(Authority.TENANT_LIST)){ %>
				<li><a class="router-link" href="#tenants" route-title="Tenants"><i class="fa fa-clone"></i> <span class="menu-text">Tenants</span></a></li>
				<% } if(user.hasAuthority(Authority.GATEWAY_LIST)){ %>
				<li><a class="router-link" href="#gateways" route-title="Gateways"><i class="fa fa-network-wired"></i> <span class="menu-text">Gateways</span></a></li>
				<% } if(user.hasAuthority(Authority.THING_LIST)){ %>
				<li><a class="router-link" href="#things" route-title="Things"><i class="fa fa-bell"></i> <span class="menu-text">Things</span></a></li>
				<% } if(user.hasAuthority(Authority.USER_LIST)){ %>
				<li><a class="router-link" href="#users" route-title="Users"><i class="fa fa-user"></i> <span class="menu-text">Users</span></a></li>
				<% } %> 
			</ul>
		</aside>
		<main id="page-container"> 
			<header id="page-header" class="container-fluid">
				<button id="sidnav-toggle">
					<i class="fa fa-bars"></i>
				</button>
				<h2 id="page-title" >				
				</h2>
				<a href="/logout" class="btn btn-secondary pull-right" id="logout-link">
					<i class="fas fa-sign-out-alt"></i>
				</a>
				
			</header>
			<div id="template-container" class="container-fluid"></div>
		</main>
	</div>
	<div id="modal-container"></div>
	<script src="../public/libs/jquery/dist/jquery.min.js"></script>
	<script src="../public/libs/jquery/dist/jquery-migrate-1.0.0.min.js"></script>
	<script src="../public/libs/moment/moment.js"></script>
	<script src="../public/libs/jquery-validate/jquery.validate.min.js"></script>
	<script src="../public/libs/jquery-tiny-pubsub/ba-tiny-pubsub.min.js"></script>
	<script src="../public/libs/loadTemplate/jquery.loadTemplate.js"></script>
	<script src="../public/libs/bootstrap/dist/js/bootstrap.min.js"></script>
	<script src="../public/libs/bootstrap/dist/js/transition.min.js"></script>
	<script src="../public/libs/bootstrap/dist/js/collapse.js"></script>
	<script src="../public/libs/bootstrap/dist/js/bootstrap-datetimepicker.min.js"></script>
	<script src="../public/libs/datatables.net/js/jquery.dataTables.min.js"></script>
	<script src="../public/libs/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
	<script src="../public/libs/jquery-modal/0.9.1/jquery.modal.min.js"></script>
	<script src="../public/libs/jquery-hashchange/jquery.ba-hashchange.min.js"></script>
	<script src="../public/libs/jquery-toast/jquery.toast.min.js"></script>
	<script>
		user = <%=session.getAttribute("user-json")%>;
		user.hasAuthority = function(authority){
			return authority && user && user.authorities && -1 < user.authorities.indexOf(authority);
		};
	</script>
	
	<script src="../public/plugins/jquery/jQuery-router.js"></script>
	<script src="../public/index.js"></script>
	<script src="../public/pages/utils/utils.js"></script>
	<script src="../public/pages/dashboard/service.js"></script>
	<script src="../public/pages/tenants/service.js"></script>
	<script src="../public/pages/gateways/service.js"></script>
	<script src="../public/pages/things/service.js"></script>
	
</body>

</html>