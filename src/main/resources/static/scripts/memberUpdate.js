$(document).ready(function() {
	
	// 폼에 데이터 입력
	var myID = localStorage.getItem('loginID');
	var data = JSON.parse(localStorage.getItem(myID));
	
	// 로그인 한 아이디에 해당하는 배열의 정보를 불러서
	// 각 #id에 맞는 div에 placehoder로 뿌려줌
	$("#id").text(myID);
	document.querySelector("#pw").setAttribute("placeholder", "'" + data[0] + "'");
	document.querySelector("#name").setAttribute("placeholder", "'" + data[1] + "'");
	document.querySelector("#addr").setAttribute("placeholder", "'" + data[2] + "'");
	document.querySelector("#tel").setAttribute("placeholder", "'" + data[3] + "'");
	
	// 수정
	var id = myID;
	var pw = $("input").eq(0);
	var name = $("input").eq(1);
	var addr = $("input").eq(2);
	var tel = $("input").eq(3);

	var info =[] ;
	
	$("#joinBtn").on("click", function() {
		var uid = localStorage.getItem('loginID');
		//infoList();
			
		if(check()) {
			info.push(pw.val());
			info.push(name.val());
			info.push(addr.val());
			info.push(tel.val());
				
			localStorage.setItem(uid, JSON.stringify(info));
			window.location.href="./memberView.html";
		}
	});
	
	function check() {
		var chk = true;
		if(pw.val().length==0 || name.val().length==0 || addr.val().length==0 || tel.val().length==0) {
			alert("입력되지 않은 항목이 존재합니다.")
			chk=false;
		}
		return chk;
	}
	
});