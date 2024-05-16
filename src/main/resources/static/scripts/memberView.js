$(function() {

	var myID = localStorage.getItem('loginID');
	var data = JSON.parse(localStorage.getItem(myID));
	
	// 로그인 한 아이디에 해당하는 배열의 정보를 불러서
	// 각 #id에 맞는 div에 텍스트로 뿌려줌
	$("#id").text(myID);
	$("#pw").text(data[0]);
	$("#name").text(data[1]);
	$("#addr").text(data[2]);
	$("#tel").text(data[3]);

	// 확인버튼
	$("#btnClose").on("click", function() {
		window.location.href="index.html";
	});
	
	// 수정버튼
	$("#btnChange").on("click", function() {
		window.location.href="memberUpdate.html";
	});
	
	// 탈퇴버튼
	$("#btnDelete").on("click", function() {
		var res = confirm("탈퇴하시겠습니까?");
		if(res) { //확인
			localStorage.removeItem(myID);
			localStorage.removeItem('loginID');
			alert("탈퇴 되었습니다. 감사합니다.")
		}
		
		window.location.href="index.html";
	});
	
	// 로그아웃
	$("#nav-logoutBtn").on("click",function() {
		localStorage.removeItem('loginID');
		
		// 회원가입 버튼과 로그인 버튼 살리기
		document.querySelector("#nav-signupBtn")
				.setAttribute("style", "display: ");
		document.querySelector("#nav-loginBtn").setAttribute("style", "display: ");
		// 로그아웃 버튼과 회원정보 버튼 없애기
		document.querySelector("#nav-logoutBtn").setAttribute("style",
				"display: none ");
		document.querySelector("#nav-meminfoBtn").setAttribute("style",
				"display: none ");
	});
});