$(function () {
    setHeader();
    getMyInfo();
    closeBtn();
    modifyUserBtn();
    deleteUserBtn();
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
            $("#name").html(name);
            $("#tel").html(tel);
            $("#addr").html(addr);
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

function deleteUserBtn(isCallback = false) {
    const accessToken = localStorage.getItem("accessToken");
    if (isEmpty(accessToken)) {
        window.location.href = "index.html";
    }

    $("#btnDelete").on("click", function () {
        $.ajax({
            url: `/api/user`,
            type: "DELETE",
            beforeSend: function (xhr) {
                const grantType = localStorage.getItem("grantType");
                if (isEmpty(grantType)) {
                    return;
                }
                xhr.setRequestHeader("Authorization", `${grantType} ${accessToken}`);
            },
            success: function (res) {
                localStorage.clear();
                alert("정상적으로 탈퇴 되었습니다.");
                window.location.href = "index.html";
            },
            error: function (err) {
                if (isCallback) {
                    console.error(err);
                    return;
                }

                refreshAccessToken(deleteUserBtn);
            }
        });
    });
}

function closeBtn() {
    $("#btnClose").on("click", function () {
        window.location.href = "index.html";
    });
}

function modifyUserBtn() {
    $("#btnChange").on("click", function () {
        window.location.href = "memberUpdate.html";
    });
}