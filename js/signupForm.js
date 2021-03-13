$(document).ready(function() {
	
	var id = $("input").eq(0);
	var pw = $("input").eq(1);
	var name = $("input").eq(2);
	var addr = $("input").eq(3);
	var tel = $("input").eq(4);

	var info =[] ;
	
	function infoList() {
		var uid = id.val();
		var userid = localStorage.getItem(id);
		
		if(userid==null) {
			//localStorage.setItem(uid, JSON.stringify([]));
		} else {
			info = JSON.parse(id);
		}
	}
	
	$("#joinBtn").on("click", function() {
		var uid = id.val();
		infoList();
		
		if(check() && Duplicated()) {
			info.push(pw.val());
			info.push(name.val());
			info.push(addr.val());
			info.push(tel.val());
			
			localStorage.setItem(uid, JSON.stringify(info));
			alert("회원가입 완료!")
			
			window.location.href="./index.html";
		}
	});
	
	
	function Duplicated() {
		var chk = true;
		var uid = id.val();
		
		var chkID = localStorage.getItem(uid);
	    if(chkID != null || chkID != undefined)  {
	         chk = false;
	         alert("등록된 아이디입니다.")
	    }
		
		return chk;
		
	}
	
	function check() {
		var chk = true;
		if(id.val().length == 0 || pw.val().length==0 || name.val().length==0 || addr.val().length==0 || tel.val().length==0) {
			alert("입력되지 않은 항목이 존재합니다.")
			chk=false;
		}
		return chk;
	}
});