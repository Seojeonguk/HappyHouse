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
function apart_search(si, gu, dong) {
    $.ajax({
        url: "/api/third/getApartTrade",
        type: "GET",
        success: function (response) {

        },
        error: function (err) {
            console.error(err);
        }
    });
}

function house_search(si, gu, dong) {
    $.ajax({
        url: "api/third/getHouseTrade",
        type: "GET",
        success: function (response) {

        },
        error: function (err) {
            console.error(err);
        }
    })
}

/* 다세대주택 서칭 */
/*
function housing_search(si, gu, dong) {
    var dupl = [];

    $.ajax({
        url: 'housing.jsp',
        type: 'post',
        data: "code=" + code + "&dong=" + dong + "&name=",
        success: function (response) {
            var arr = response.split('\n');
            $.each(arr, function (index, item) {
                if (item.trim() != "") {
                    var item_list = item.split("|");
                    if (dupl.indexOf(item_list[3]) == -1) {
                        google_geocoding(si, gu, dong, item_list, 1);
                        var $div = $('<div class="item"></div>');
                        $('<p class="item-name">' + item_list[3] + '</p>').appendTo($div);
                        $('<p class="item-money">' + '가격 : ' + item_list[1] + '</p>').appendTo($div);
                        $('<p class="item-square">' + '토지면적 : ' + item_list[2] + String.fromCodePoint(0x33A0) + '</p>').appendTo($div);
                        $('<p class="item-category">' + '거래구분 : 다세대' + '</p>').appendTo($div);
                        $div.appendTo(".content-left");
                        dupl.push(item_list[3]);
                    }
                }
            });
        },
        error: function () {
            console.log('apertment.jsp load error!');
        }
    });
}


function detail_apart_search(name) {
    $("#loading-page").css("display", "block");
    $.ajax({
        url: 'apartment.jsp',
        type: 'post',
        data: "code=" + code + "&name=" + name,
        success: function (response) {
            $(".content-left").eq(0).children().not(".content-left-title").remove();

            var $name = $('<div class="item item-name">' + name + '</div>');
            $name.appendTo('.content-left');

            var arr = response.split('\n');
            $.each(arr, function (index, item) {
                if (item.trim() != "") {
                    var item_list = item.split("|");
                    var $div = $('<div class="item"></div>');
                    $('<p class="item-money">' + '가격 : ' + item_list[1] + '</p>').appendTo($div);
                    $('<p class="item-square">' + '토지면적 : ' + item_list[2] + String.fromCodePoint(0x33A0) + '</p>').appendTo($div);
                    $('<p class="item-category">' + '거래구분 : 아파트' + '</p>').appendTo($div);
                    $('<p class="item-trade">' + item_list[4] + "." + item_list[5] + "." + item_list[6] + '</p>').appendTo($div);
                    $div.appendTo(".content-left");
                }
            });
            $("#loading-page").css("display", "none");
        },
        error: function () {
            console.log('apertment.jsp load error!');
        }
    });
}

function detail_house_search(name) {
    $("#loading-page").css("display", "block");
    var detail_dong = str.split(' ')[2];
    $.ajax({
        url: 'housing.jsp',
        type: 'post',
        data: "code=" + code + "&dong=" + detail_dong + "&name=" + name,
        success: function (response) {
            $(".content-left").eq(0).children().not(".content-left-title").remove();

            var $name = $('<div class="item item-name">' + name + '</div>');
            $name.appendTo('.content-left');

            var arr = response.split('\n');
            $.each(arr, function (index, item) {
                if (item.trim() != "") {
                    var item_list = item.split("|");
                    var $div = $('<div class="item"></div>');
                    $('<p class="item-money">' + '가격 : ' + item_list[1] + '</p>').appendTo($div);
                    $('<p class="item-square">' + '토지면적 : ' + item_list[2] + String.fromCodePoint(0x33A0) + '</p>').appendTo($div);
                    $('<p class="item-category">' + '거래구분 : 다세대' + '</p>').appendTo($div);
                    $('<p class="item-trade">' + item_list[4] + "." + item_list[5] + "." + item_list[6] + '</p>').appendTo($div);
                    $div.appendTo(".content-left");
                }
            });
            $("#loading-page").css("display", "none");
        },
        error: function () {
            console.log('apertment.jsp load error!');
        }
    });
}

 */