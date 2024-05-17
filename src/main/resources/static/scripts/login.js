$(function () {
    var id = $("input").eq(0);
    var pw = $("input").eq(1);
    var info = [];


    function infoList() {
        var uid = id.val();
        var userid = localStorage.getItem(id);

        if (userid == null) {
            //localStorage.setItem(uid, JSON.stringify([]));
        } else {
            info = JSON.parse(id);
        }
    }


    // 로그인
    // $("#loginBtn").on("click",function() {
    //
    // 		var uid = id.val();
    // 		var data = JSON.parse(localStorage.getItem(uid));
    //
    // 		var chk = false;
    //
    // 		for (var i = 0; i < localStorage.length; i++) { //로컬스토리지만큼 돌면서
    // 			if(uid == localStorage.key(i)){ // uid가 로컬스토리지 key값과 일치하면
    // 				chk = true; // 일단 아이디 통과
    // 			}
    // 		}
    //
    // 		if(chk) { // 아이디는 맞고
    // 			if (data[0] == pw.val()) { //
    // 				//이걸 이용해 다른 페이지에서 로그아웃,로그인,가입,정보 버튼 컨트롤 할 수 있을 듯
    // 				localStorage.setItem('loginID', id.val());
    //
    // 				alert("안녕하세요 :)")
    // 				window.location.href="./index.html";
    // 				// 회원 가입 버튼과 로그인 버튼 없애고
    // 				document.querySelector("#nav-signupBtn").setAttribute("style",
    // 						"display: none ");
    // 				document.querySelector("#nav-loginBtn").setAttribute("style",
    // 						"display: none ");
    // 				// 로그아웃 버튼과 회원정보 버튼 살리기
    // 				document.querySelector("#nav-logoutBtn").setAttribute("style",
    // 						"display: ");
    // 				document.querySelector("#nav-meminfoBtn").setAttribute("style",
    // 						"display: ");
    //
    // 			} else {
    // 				alert("비밀번호가 맞지 않습니다.");
    // 			}
    // 		} else {
    // 			alert("아이디가 맞지 않습니다.");
    // 		}
    //
    // });

    $("#loginBtn").on("click", function () {

        const id = $("#id").val();
        const pw = $("#pw").val();

        if (!isVerifyInputFields(id, pw)) {
            alert('모든 정보를 입력해주세요.');
            return;
        }

        const body = JSON.stringify({
            loginId: id,
            pw: pw,
        });

        fetch('http://localhost:8080/api/user/login',
            {
                method: "POST", body: body, headers: {
                    'Content-Type': 'application/json'
                },
            })
            .then(res => res.text())
            .then(res => {
                window.location="index.html";
            })
            .catch(err => console.error(err));
    });

    // 로그아웃
    $("#nav-logoutBtn").on("click", function () {
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