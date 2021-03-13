<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Happy House | 회원 정보</title>

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link
	href="https://fonts.googleapis.com/css2?family=Jua&family=Noto+Sans+KR&display=swap"
	rel="stylesheet">
<!-- Custom fonts for this template -->
<link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet">
<link href="vendor/simple-line-icons/css/simple-line-icons.css"
	rel="stylesheet" type="text/css">

<!-- Custom styles for this template -->
<link href="css/landing-page.min.css" rel="stylesheet">
<link href="./css/memberView.css" rel="stylesheet">

</head>
<body>
<div class="header fixed-top shadow">
	<nav class="navbar navbar-light bg-light static-top">
		<div class="container">
			<a class="navbar-brand" href="index.html">
			<i class="fa fa-home pr-2"></i>Happy House</a>
			<div class="btn-zone">
			<div class="btn-group">
				<a class="btn btn-sm btn-primary mr-2 navBtn" id="nav-logoutBtn" href="login.html">
				<i class="fa fa-unlock pr-2"></i>Logout</a>
			</div>
			<div class="btn-group">
				<a class="btn btn-sm btn-primary navBtn" id="nav-meminfoBtn" href="signupForm.html">
				<i class="fa fa-user pr-2"></i>회원정보</a>
			</div>
			</div>
		</div>
	</nav>
</div>
	<!-- Call to Action -->
	<section class="call-to-action text-white text-center houseImg">
		<div class="overlay"></div>
		<div class="container">
			<div class="row">
				<div class="col-xl-9 mx-auto">
					<h2 class="mb-4">Welcome, Join Us!</h2>
				</div>
			</div>
		</div>
	</section>
	
	<section class="showcase">
		<div class="container signupBg mt-4 mb-7">
			<div class="row justify-content-center">
				<div class="col-7 signupBox">
					<form method="post" action="index.html">
						<div class="row mb-5 justify-content-center">
							<h2>회원 정보 확인</h2>
						</div>
						<div class="form-group row">
							<label for="id">아이디<p>*</p></label>
							<div class="col-md-8"> </div>
						</div>
						<div class="form-group row">
							<label for="pw">비밀번호<p>*</p></label> 
							<div class="col-md-8"> </div>
						</div>
						<div class="form-group row">
							<label for="name">이름<p>*</p></label>
							<div class="col-md-8"> </div>
						</div>
						<div class="form-group row">
							<label for="addr">주소<p>*</p></label>
							<div class="col-md-8"> </div>
						</div>
						<div class="form-group row mb-4">
							<label for="tel">전화번호<p>*</p></label>
							<div class="col-md-8"> </div>
						</div class="text-center">
						<div class="row">
							<div class="col btn-group">
								<input type="button" class="btn btn-primary" id="btnClose" value="확인">
								<input type="button" class=" btn btn-primary" id="btnChange" value="수정">
								<input type="button" class="btn btn-primary" id="btnDelete" value="삭제">
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>
	<!-- Footer -->
	<footer class="footer bg-light">
		<div class="container">
			<div class="row">
				<div class="col-lg-6 h-100 text-center text-lg-left my-auto">
					<ul class="list-inline mb-2">
						<li class="list-inline-item"><a href="#">About</a></li>
						<li class="list-inline-item">&sdot;</li>
						<li class="list-inline-item"><a href="#">Contact</a></li>
						<li class="list-inline-item">&sdot;</li>
						<li class="list-inline-item"><a href="#">Terms of Use</a></li>
						<li class="list-inline-item">&sdot;</li>
						<li class="list-inline-item"><a href="#">Privacy Policy</a></li>
					</ul>
					<p class="text-muted small mb-4 mb-lg-0">&copy; Your Website
						2020. All Rights Reserved.</p>
				</div>
				<div class="col-lg-6 h-100 text-center text-lg-right my-auto">
					<ul class="list-inline mb-0">
						<li class="list-inline-item mr-3"><a href="#"> <i
								class="fab fa-facebook fa-2x fa-fw"></i>
						</a></li>
						<li class="list-inline-item mr-3"><a href="#"> <i
								class="fab fa-twitter-square fa-2x fa-fw"></i>
						</a></li>
						<li class="list-inline-item"><a href="#"> <i
								class="fab fa-instagram fa-2x fa-fw"></i>
						</a></li>
					</ul>
				</div>
			</div>
		</div>
	</footer>
</body>
</html>