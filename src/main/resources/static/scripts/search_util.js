function initSearchForm({year, month, category, si, gu, dong} = {}) {
    makeYear({selectedYear: year});
    makeMonth({selectedMonth: month});

    if (!isEmpty(category)) {
        $('#category').val(category);
    }

    if (!populateSelectOptions('si', 'siOptions', si)) {
        fetchLegalCodesByRegion("si");
    }

    init_gu(gu);
    init_dong(dong);

    $("#si").change(function () {
        localStorage.removeItem('guOptions');
        localStorage.removeItem('dongOptions');

        init_gu();
        init_dong();

        const upperRegionCode = $("#si").find(":selected").attr('legalCode');
        if (isEmpty(upperRegionCode)) {
            return;
        }

        fetchLegalCodesByRegion("gu", upperRegionCode);
    })

    $("#gu").change(function () {
        localStorage.removeItem('dongOptions');

        init_dong();

        const upperRegionCode = $("#gu").find(":selected").attr('legalCode');
        if (isEmpty(upperRegionCode)) {
            return;
        }

        fetchLegalCodesByRegion("dong", upperRegionCode);
    })

    $("#dong").change(function () {
        saveForm();
        $("#legalCode").val($(this).find(":selected").attr('legalCode'));
        $("#searching-form").submit();
    })
}

function fetchLegalCodesByRegion(selectId, upperRegionCode = "0000000000") {
    if (isEmpty(selectId)) {
        return;
    }

    $.ajax(({
        url: "/api/file/legalCode",
        type: "GET",
        data: `upperRegionCode=${upperRegionCode}`,
        success: function (response) {
            $.each(response, function (idx, legalCode) {
                $(`#${selectId}`).append(`<option value=${legalCode.legalName} legalCode="${legalCode.legalCode}">${legalCode.legalName}</option>`);
            });
        }
    }));
}

/* 구 옵션 초기화 */
function init_gu(gu) {
    $("#gu").html('<option name="gu_init" value="">시/군/구</option>');
    populateSelectOptions('gu', 'guOptions', gu);
}

/* 동 옵션 초기화 */
function init_dong(dong) {
    $("#dong").html('<option name="dong_init" value="">읍/면/동</option>');
    populateSelectOptions('dong', 'dongOptions', dong);
}

/* 거래 내역 조회 */
function getTrade({category, legalCode, si, gu, dong, year, month}) {
    $.ajax({
        url: "api/third/getTrade",
        type: "GET",
        data: `category=${category}&legalCode=${legalCode}&year=${year}&month=${month}`,
        success: function (response) {
            console.log(response);
            $.each(response, async function (idx, res) {
                const {apartmentTrading, dealAmount, dealYear, dealMonth, dealDay, exclusiveArea, name} = res;
                const coordinate = await getCoordinate(`${si} ${gu} ${dong} ${name}`);
                mapMarking(coordinate.lat, coordinate.lng, res, apartmentTrading ? "red" : "blue");

                let div = $(`<div class="item"></div>`);
                $(`<p class="item-name">이름 : ${name}</p>`).appendTo(div);
                $(`<p class="item-money">가격 : ${dealAmount}</p>`).appendTo(div);
                $(`<p class="item-area">면적 : ${exclusiveArea} ${String.fromCodePoint(0x33A0)}</p>`).appendTo(div);
                $(`<p class="item-category">거래구분 : ${apartmentTrading ? "아파트" : "연립다세대"}</p>`).appendTo(div);
                $(`<p class="item-deal">${dealYear}.${dealMonth}.${dealDay}</p>`).appendTo(div);
                div.appendTo(".content-left");
            });
        },
        error: function (err) {
            console.error(err);
        }
    })
}

function makeYear({selectedYear = new Date().getFullYear(), start = 2010, end = new Date().getFullYear()}) {
    const yearSelect = $('#year');
    yearSelect.empty(); // 기존 옵션 제거
    for (let year = end; year >= start; year--) {
        const option = $('<option></option>').attr('value', year).text(year);
        if (selectedYear && year === selectedYear) {
            option.attr('selected', 'selected');
        }
        yearSelect.append(option);
    }
}

function makeMonth({selectedMonth = (new Date().getMonth() + 1).toString().padStart(2, '0'), start = 1, end = 12}) {
    const monthSelect = $('#month');
    monthSelect.empty();
    for (let month = start; month <= end; month++) {
        let monthStr = month.toString().padStart(2, '0');
        const option = $('<option></option>').attr('value', monthStr).text(monthStr);
        if (selectedMonth && monthStr === selectedMonth) {
            option.attr('selected', 'selected');
        }
        monthSelect.append(option);
    }
}

function saveForm() {
    saveSelectOptions('si', 'siOptions');
    saveSelectOptions('gu', 'guOptions');
    saveSelectOptions('dong', 'dongOptions');
}