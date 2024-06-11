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
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            category,
            legalCode,
            year,
            month,
            si
        }),
        success: function (response) {
            console.log(response);
            $.each(response, async function (idx, res) {
                const {apartmentTrading, dealAmount, dealYear, dealMonth, dealDay, exclusiveArea, name, lat, lng} = res;
                mapMarking(lat, lng, res, apartmentTrading ? "red" : "blue");

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

function getInformation({category, legalCode, si, year, month}) {
    $.ajax({
        url: "api/third/getInformation",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            category,
            legalCode,
            year,
            month,
            si
        }),
        beforeSend: function (xhr) {
            $(".spinner-div").removeClass("hide");
            const accessToken = localStorage.getItem("accessToken");
            const grantType = localStorage.getItem("grantType");
            if (isEmpty(grantType)) {
                return;
            }
            xhr.setRequestHeader("Authorization", `${grantType} ${accessToken}`);
        },
        success: function (response) {
            console.log(response);
            $.each(response, async function (idx, res) {
                const {
                    lat,
                    lng,
                    complexCode,
                    householdCount,
                    approvalDate,
                    formattedAddress,
                    roadNameAddress,
                    complexName,
                    buildingCount
                } = res;
                mapMarking(lat, lng, res);

                const div = $(`<div class="item"></div>`).attr('complexCode', complexCode);
                $("<p></p>").addClass("name").text(complexName).appendTo(div);

                const addr = $("<div></div>").appendTo(div);
                const a = $("<a></a>")
                    .addClass("addr")
                    .attr('href', '#')
                    .attr('target', '_self')
                    .text(formattedAddress)
                    .appendTo(addr);
                const icon = createIcon("roadAddr", "fa-caret-down", a, false, true);

                a.popover({
                    title: '',
                    content: `<div><span class="tag">도로명</span>${roadNameAddress}</div> <div><span class="tag">지번</span>${formattedAddress}</div>`,
                    placement: "bottom",
                    html: true,
                    container: a
                });

                a.on("click", function (e) {
                    $(icon).find("i").toggleClass("fa-caret-down").toggleClass("fa-caret-up");
                    return false;
                })

                const infos = $("<div></div>").addClass("infos");
                $("<p></p>").addClass("approvalDate").text(`${approvalDate.substring(0, 4)}년`).appendTo(infos);
                $("<p></p>").addClass("buildingCount").text(`${buildingCount}개 동`).appendTo(infos);
                $("<p></p>").addClass("householdCount").text(`${householdCount}세대`).appendTo(infos);
                infos.appendTo(div);

                $(div).on("click", function () {
                    getDetailInformation(complexCode, res);
                });

                div.appendTo(".search-list");
            });
        },
        error: function (err) {
            console.error(err);
        },
        complete: function () {
            $(".spinner-div").addClass("hide");
        }
    })
}

function getDetailInformation(complexCode, res) {
    $.ajax({
        url: "api/third/getDetailInformation",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            complexCode
        }),
        beforeSend: function (xhr) {
            const accessToken = localStorage.getItem("accessToken");
            const grantType = localStorage.getItem("grantType");
            if (isEmpty(grantType)) {
                return;
            }
            xhr.setRequestHeader("Authorization", `${grantType} ${accessToken}`);
        },
        success: function (response) {
            console.log(response);
            showDetailInformation(response, res);
        },
        error: function (err) {
            console.error(err);
        }
    })
}

function showDetailInformation({
                                   auxiliaryFacilities,
                                   busStopDistance,
                                   convenienceFacilities,
                                   complexName,
                                   educationalFacilities,
                                   groundParkingSpaces,
                                   numberOfCCTVs,
                                   numberOfElevators,
                                   subwayLine,
                                   subwayStationDistance,
                                   subwayStationName,
                                   undergroundParkingSpaces
                               },
                               res) {
    const {
        formattedAddress,
        managementOfficeContact,
        managementOfficeFax,
        website,
        roadNameAddress,
        heatingType,
        constructor
    } = res;
    const searchList = $(".search-list");

    const div = $("<div></div>").addClass("detail-info");

    const topDiv = $("<div></div>").addClass("title").appendTo(div);
    $("<p></p>").addClass("name").html(complexName).appendTo(topDiv);
    const icon = createIcon("back", "fa-times", topDiv, false);

    $(icon).on("click", function () {
        $(".detail-info").remove();
        searchList.removeClass("hide");
    });

    const content = $("<div></div>").addClass("detail-content").appendTo(div);
    createDetailDiv("place", [
        createDetailObjects("place", "fa-map-marker-alt", formattedAddress)
    ], content);

    createDetailDiv("transport", [
        createDetailObjects("subway", "fa-subway", subwayLine, subwayStationDistance, subwayStationName),
        createDetailObjects("bus", "fa-bus", busStopDistance)
    ], content);

    createDetailDiv("contact", [
        createDetailObjects("phone", "fa-phone", managementOfficeContact),
        createDetailObjects("fax", "fa-fax", managementOfficeFax),
        createDetailObjects("website", "fa-globe", website)
    ], content);

    createDetailDiv("detail", [
        createDetailObjects("heatingType", "fa-fire", heatingType),
        createDetailObjects("constructor", "fa-tractor", constructor),
        createDetailObjects("parking", "fa-parking", `지상 : ${groundParkingSpaces}`, `지하 : ${undergroundParkingSpaces}`),
        createDetailObjects("cctv", "fa-video", `총 : ${numberOfCCTVs}`),
        createDetailObjects("elevator", "fa-tram", `총 : ${numberOfElevators}`),
        createDetailObjects("auxiliary", "fa-place-of-worship", auxiliaryFacilities),
        createDetailObjects("convenience", "fa-store", convenienceFacilities),
        createDetailObjects("education", "fa-school", educationalFacilities)
    ], content);

    searchList.addClass("hide");

    const search = $(".search");
    div.appendTo(search);
}

function createDetailObjects(iconName, iconPath, ...args) {
    return {
        "icon": {
            "name": iconName,
            "path": iconPath
        },
        "contents": args.filter(arg => !isEmpty(arg))
    }
}

function createDetailDiv(className, items, parent) {
    const div = $("<div></div>").addClass(className).addClass("detail").appendTo(parent);

    items.forEach(function (item, index) {
        const {icon, contents} = item;
        const {name, path} = icon;

        if (isEmpty(contents)) {
            return;
        }

        const rowDiv = $("<div></div>").addClass("row").appendTo(div);
        createIcon("icon", path, rowDiv, false);
        const pDiv = $("<div></div>").appendTo(rowDiv);
        contents.forEach(function (content, index) {
            const childDiv = $("<div></div>").appendTo(pDiv);
            $("<p></p>").html(content).appendTo(childDiv);
        })
    });

    return div;
}
