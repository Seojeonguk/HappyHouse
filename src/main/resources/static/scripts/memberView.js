$(function () {
    setHeader();
    getMyInfo();

    // 확인버튼
    $("#btnClose").on("click", function () {
        window.location.href = "index.html";
    });

    // 수정버튼
    $("#btnChange").on("click", function () {
        window.location.href = "memberUpdate.html";
    });

    // 탈퇴버튼
    $("#btnDelete").on("click", function () {
        const accessToken = localStorage.getItem("accessToken");
        var res = confirm("탈퇴하시겠습니까?");
        if (res) { //확인
            localStorage.removeItem(myID);
            localStorage.removeItem('loginID');
            alert("탈퇴 되었습니다. 감사합니다.")
        }

        window.location.href = "index.html";
    });
});

function getMyInfo() {
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
            console.error(err);
        }
    })
}