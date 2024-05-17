$(document).ready(function () {

    $("#joinBtn").on("click", function () {
        const id = $("#id").val();
        const pw = $("#pw").val();
        const name = $("#name").val();
        const addr = $("#addr").val();
        const tel = $("#tel").val();

        if (!isVerifyInputFields(id, pw, name, addr, tel)) {
            alert('모든 정보를 입력해주세요.');
            return;
        }

        $.ajax({
            url: "/api/user/signUp",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({
                loginId: id,
                pw: pw,
                name: name,
                addr: addr,
                tel: tel
            }),
            success: function (response) {
                alert('성공적으로 회원가입이 되셨습니다.');
                window.location = 'index.html';
            },
            error: function (err) {
                console.error(err);
            }
        });
    });
});

