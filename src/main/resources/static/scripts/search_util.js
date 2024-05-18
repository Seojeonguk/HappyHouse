function initSearch() {
    load_si_data();
    init_gu();
    init_dong();

    $("#si").change(function () {
        load_gu_data();
    })

    $("#gu").change(function () {
        load_dong_data();
    })

    $("#dong").change(function () {
        $("#legalCode").val($(this).find(":selected").attr('legalCode'));
        $("#searching-form").submit();
    })
}

function load_si_data() {
    const upperRegionCode = "0000000000";
    $.ajax({
        url: "/api/file/legalCode",
        type: "GET",
        data: `upperRegionCode=${upperRegionCode}`,
        success: function (response) {
            $.each(response, function (idx, legalCode) {
                $("#si").append(`<option value=${legalCode.legalName} legalCode="${legalCode.legalCode}">${legalCode.legalName}</option>`);
            });
        },
        error: function (err) {
            console.error(err);
        }
    });
}

/* 시/도 버튼 변경시 구 데이터 불러오기 */
function load_gu_data() {
    init_gu();
    init_dong();

    const upperRegionCode = $("#si").find(":selected").attr('legalCode');
    if (isEmpty(upperRegionCode)) {
        return;
    }

    $.ajax(({
        url: "/api/file/legalCode",
        type: "GET",
        data: `upperRegionCode=${upperRegionCode}`,
        success: function (response) {
            $.each(response, function (idx, legalCode) {
                $("#gu").append(`<option value=${legalCode.legalName} legalCode="${legalCode.legalCode}">${legalCode.legalName}</option>`);
            });
        }
    }));
}

/* 구 버튼 변경시 동 데이터 가져오기 */
function load_dong_data() {
    init_dong();

    const upperRegionCode = $("#gu").find(":selected").attr('legalCode');
    if (isEmpty(upperRegionCode)) {
        return;
    }

    $.ajax(({
        url: "/api/file/legalCode",
        type: "GET",
        data: `upperRegionCode=${upperRegionCode}`,
        success: function (response) {
            $.each(response, function (idx, legalCode) {
                $("#dong").append(`<option value=${legalCode.legalName} legalCode="${legalCode.legalCode}">${legalCode.legalName}</option>`);
            });
        }
    }));
}

/* 구 옵션 초기화 */
function init_gu() {
    $("#gu").html('<option name="gu_init" value="">시/군/구</option>');
}

/* 동 옵션 초기화 */
function init_dong() {
    $("#dong").html('<option name="dong_init" value="">읍/면/동</option>');
}

/* 아파트 데이터 서칭 */
function apart_search({si, gu, dong}) {
    $.ajax({
        url: "/api/third/getApartTrade",
        type: "GET",
        success: function (response) {
            console.log(response);
            $.each(response, function (idx,res) {
                $.ajax({
                    url: `/api/third/google/geocoding?address=${si} ${gu} ${dong} ${res.apartmentName}`,
                    type: "GET",
                    success: async function (coordinate) {
                        mapMarking(coordinate.lat, coordinate.lng, res, 0);
                    },
                    error: function (err) {
                        console.error(err);
                    }
                });

                let div = $(`<div class="item"></div>`);
                $(`<p class="item-money">가격 : ${res.dealAmount}</p>`).appendTo(div);
                $(`<p class="item-area">면적 : ${res.exclusiveArea} ${String.fromCodePoint(0x33A0)}</p>`).appendTo(div);
                $(`<p class="item-category">거래구분 : 아파트</p>`).appendTo(div);
                $(`<p class="item-deal">${res.dealYear}.${res.dealMonth}.${res.dealDay}</p>`).appendTo(div);
                div.appendTo(".content-left");
            });
        },
        error: function (err) {
            console.error(err);
        }
    });
}

function house_search({si, gu, dong}) {
    $.ajax({
        url: "api/third/getHouseTrade",
        type: "GET",
        success: function (response) {
            console.log(response);
            $.each(response, function (idx,res) {
                $.ajax({
                    url: `/api/third/google/geocoding?address=${si} ${gu} ${dong} ${res.apartmentName}`,
                    type: "GET",
                    success: async function (coordinate) {
                        mapMarking(coordinate.lat, coordinate.lng, res, 1);
                    },
                    error: function (err) {
                        console.error(err);
                    }
                });

                let div = $(`<div class="item"></div>`);
                $(`<p class="item-money">가격 : ${res.dealAmount}</p>`).appendTo(div);
                $(`<p class="item-area">면적 : ${res.exclusiveArea} ${String.fromCodePoint(0x33A0)}</p>`).appendTo(div);
                $(`<p class="item-category">거래구분 : 주택</p>`).appendTo(div);
                $(`<p class="item-deal">${res.dealYear}.${res.dealMonth}.${res.dealDay}</p>`).appendTo(div);
                div.appendTo(".content-left");
            });
        },
        error: function (err) {
            console.error(err);
        }
    })
}