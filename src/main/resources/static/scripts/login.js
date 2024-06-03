$(function () {

    $("#loginBtn").on("click", function () {

        const id = $("#id").val();
        const pw = $("#pw").val();

        if (!isVerifyInputFields(id, pw)) {
            alert('모든 정보를 입력해주세요.');
            return;
        }

        $.ajax({
            url: '/api/user/login',
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({
                loginId: id,
                pw: pw
            }),
            success: function (res) {
                console.log(res);
                //window.location = "index.html";
            },
            error: function (err) {
                console.error(err);
            }
        });
    });
});