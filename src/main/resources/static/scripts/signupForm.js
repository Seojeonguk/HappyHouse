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

        const body = JSON.stringify({
            loginId: id,
            pw: pw,
            name: name,
            addr: addr,
            tel: tel
        });

        fetch('http://localhost:8080/user/signUp',
            {
                method: "POST", body: body, headers: {
                    'Content-Type': 'application/json'
                },
            })
            .then(res => res.text())
            .then(res => console.log(res))
            .catch(err => console.error(err));

    });
});

