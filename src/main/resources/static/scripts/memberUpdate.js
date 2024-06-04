$(document).ready(function () {
    setHeader();
    getMyInfo();
    modifyUserBtn();
});

function getMyInfo(isCallback = false) {
    const accessToken = localStorage.getItem("accessToken");
    if (isEmpty(accessToken)) {
        return;
    }

    $.ajax({
        url: `/api/user/me`,
        type: "POST",
        beforeSend: function (xhr) {
            const grantType = localStorage.getItem("grantType");
            if (isEmpty(grantType)) {
                return;
            }
            xhr.setRequestHeader("Authorization", `${grantType} ${accessToken}`);
        },
        success: function (res) {
            const {loginId, addr, name, tel} = res;
            $("#id").html(loginId);
            $("#name").val(name);
            $("#tel").val(tel);
            $("#addr").val(addr);
        },
        error: function (err) {
            if (isCallback) {
                console.error(err);
                return;
            }

            refreshAccessToken(getMyInfo);
        }
    })
}

function modifyUserBtn(isCallback = false) {
    $("#modifyBtn").on("click", function () {
        const accessToken = localStorage.getItem("accessToken");
        if (isEmpty(accessToken)) {
            return;
        }

        const name = $("#name").val();
        const tel = $("#tel").val();
        const addr = $("#addr").val();

        if (!isVerifyInputFields(name, tel, addr)) {
            alert('모든 항목을 필수로 입력바랍니다.');
            return;
        }

        $.ajax({
            url: `/api/user`,
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify({
                name,
                tel,
                addr
            }),
            beforeSend: function (xhr) {
                const grantType = localStorage.getItem("grantType");
                if (isEmpty(grantType)) {
                    return;
                }
                xhr.setRequestHeader("Authorization", `${grantType} ${accessToken}`);
            },
            success: function (res) {
                alert('성공적으로 정보가 변경되었습니다.');
                window.location.href = "index.html";
            },
            error: function (err) {
                if (isCallback) {
                    console.error(err);
                    return;
                }

                refreshAccessToken(getMyInfo);
            }
        })
    });
}