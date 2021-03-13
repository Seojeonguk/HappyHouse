$(function() {

	var id = localStorage.getItem('id');
	var pw = localStorage.getItem('pw');
	var name = localStorage.getItem('name');
	var addr = localStorage.getItem('addr');
	var tel = localStorage.getItem('tel');

	var idArr = JSON.parse(id);
	var pwArr = JSON.parse(pw);
	var nameArr = JSON.parse(name);
	var addrArr = JSON.parse(addr);
	var telArr = JSON.parse(tel);

	var idx;
	var myID = localStorage.getItem('loginID');
	$.each(idArr, function(index) { // id 배열 돌면서 현재 loginID 랑 같은 아이디의 인덱스값 받아서
		if (idArr[index] === myID) {
			idx = index;
		}
	});
	
	// 그 인덱스에 해당하는 나머지 배열의 정보를 불러서
	// 각 #id에 맞는 div에 텍스트로 뿌려줌
	$("#id").text(idArr[idx]);
	$("#pw").text(pwArr[idx]);
	$("#name").text(nameArr[idx]);
	$("#addr").text(addrArr[idx]);
	$("#tel").text(telArr[idx]);

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
			var indx;
			$.each(idArr, function(index) { // 배열을 돌면서
				if(idArr[index] === loginID) { // 로그인한 아이디와 같다면
					indx = index;
					
				}
			});
			localStorage.removeItem('idArr'+[idx]);
			localStorage.removeItem('pwArr'+[idx]);
			localStorage.removeItem('nameArr[idx]');
			localStorage.removeItem('addrArr[idx]');
			localStorage.removeItem('telArr[idx]');
//			localStorage.removeItem('loginID');
			
			alert("탈퇴 되었습니다. 감사합니다.")
		}
		
		window.location.href="index.html";
	});
});