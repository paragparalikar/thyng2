<!--login-->
<html>
<title>Thyng</title>
<head>
<link rel="shortcut icon" type="image/png" href="../public/images/favicon.ico"/>
<link rel="stylesheet" href="../public/libs/bootstrap/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="../public/libs/fontawesome/css/solid.min.css">
<link rel="stylesheet" href="../public/libs/fontawesome/css/fontawesome.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
<style>
	.card{
		border-radius: 5px;
		background-color: #ffffff;
		padding: 1em;
		box-shadow: 6px 6px 6px 0 rgba(0,0,0,0.2);
	  	transition: 0.3s;
	}
	body {
	  display:flex;
	  background-color: #eee;
	}
	form {
		width: 40em;
		margin-right: 3em;
		margin-left: auto;
		margin-top: auto;
		margin-bottom: 3em;
	}
	table{
		width: 100%;
	}
	.form-group > label{
		width: 100%;
	}
	.form-group > label > .error{
		float: right;
	}
	.form-control.error{
		border-color: #ffaaaa;
	}
	.error{
		font-weight: 100;
		font-size: small;
		color: red;
	}
	.error:before{
		font-family:'Font Awesome 5 Free';
		content:"\f071";
		font-weight: 900;
		font-size: small;
		padding-right: 0.5em;
	}
</style>
</head>
<body>
	<form action="/login" method="POST" class="card" id="login-form" onsubmit="loginAction(this);">
		<div>
			<table>
				<tr>
					<td>
						<h2>Thyng</h2>
					</td>
					<td valign="bottom">
						<span class="pull-right" id="message"></span>
					</td>
				</tr>
			</table>
		</div>
		<div>
			<div class="form-group">
				<label for="username">Email</label>
				<input value="0.0@gmail.com" data-rule-email="true" data-rule-required="true" data-rule-minlength="3" data-rule-maxlength="255" maxlength="255"  type="text" name="username" id="username" class="form-control" placeholder="Please enter Email">
			</div>
			<div class="form-group">
				<label for="password">Password</label>
				<input value="thyng" data-rule-required="true" data-rule-minlength="3" data-rule-maxlength="255" maxlength="255"  type="password" name="password" id="password" class="form-control" placeholder="Please enter Password">
			</div>
		</div>
		<div>
			<button type="button" class="btn btn-success pull-left">
				<i class="fas fa-user-plus"></i> Register
			</button>
			<button type="submit" class="btn btn-primary pull-right">
				<i class="fas fa-sign-in-alt"></i> Login
			</button>
			<a href="#" class="btn btn-secondary pull-right">Forgot Password</a>
		</div>
	</form>
	
	<script src="../public/libs/jquery/dist/jquery.min.js"></script>
	<script src="../public/libs/jquery/dist/jquery-migrate-1.0.0.min.js"></script>
	<script src="../public/libs/bootstrap/dist/js/bootstrap.min.js"></script>
	<script src="../public/libs/jquery-validate/jquery.validate.min.js"></script>
	<script>
		$(function(){
			$("#username").focus();
			if(-1 < window.location.search.indexOf("error")){
				$("#message").addClass("error").html("Invalid credencials");
			}else if(-1 < window.location.search.indexOf("logout")){
				$("#message").addClass("text-success").html("<i class='fa fa-check'></i> Successfully logged out");
			}
			$("#login-form").validate({
				errorPlacement: function(error, element) {
					$(element).closest("form").find( "label[for='"+element.attr( "id" ) + "']").append( error );
				},
				errorElement: "span"
			});
		});
		var loginAction = function(form){
			
             form.action = form.action + self.document.location.hash;
             return true;
		};
	</script>
</body>
</html>