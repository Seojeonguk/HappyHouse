$(function () {
    setHeader();

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
                const {accessToken, refreshToken, grantType, accessTokenExpire} = res;
                localStorage.setItem("accessToken",accessToken);
                localStorage.setItem("refreshToken",refreshToken);
                localStorage.setItem("grantType",grantType);
                localStorage.setItem("accessTokenExpire",accessTokenExpire);

                window.location = "index.html";
            },
            error: function (err) {
                console.error(err);
            }
        });
    });
});