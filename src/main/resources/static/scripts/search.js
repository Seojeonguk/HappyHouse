$(function () {
    /*
    $("#search-btn").click(async function () {
        $('.content-left').children().not('.content-left-title').remove();

        var category = $("select[name='category']").val();
        if (category == "전체" || category == "아파트") {
            apart_search(si, gu, dong);
        }
        if (category == "전체" || category == "연립다세대") {
            house_search(si, gu, dong);
        }
    });

    var domain_parameter = window.location.search.substring(1).split("&");
    if (domain_parameter != "") {
        $("#category option").each(function () {
            if ($(this).val() == decodeURIComponent(domain_parameter[0].split("=")[1])) {
                $(this).attr("selected", "selected");
            }
        });
        $("#si option").not("[name='si_init']").each(function () {
            if ($(this).val() == decodeURIComponent(domain_parameter[1].split("=")[1])) {
                $(this).attr("selected", "selected");
            }
        });
        load_gu_data();
        $("#gu option").not("[name='gu_init']").each(function () {
            if ($(this).val() == decodeURIComponent(domain_parameter[2].split("=")[1])) {
                $(this).attr("selected", "selected");
            }
        });
        load_dong_data();
        $("#dong option").not("[name='dong_init']").each(function () {
            if ($(this).val() == decodeURIComponent(domain_parameter[3].split("=")[1])) {
                $(this).attr("selected", "selected");
            }
        });
        $("#search-btn").trigger("click");
    } else {
        init_gu();
        init_dong();
    }
     */
});

$(document).ready(function () {
    initSearch();

    loadGoogleMap();

    const params = loadQueryParam();
    getCoordinates(params);
})

function loadQueryParam() {
    const params = new URLSearchParams(window.location.search);

    const legalCode = params.get('legalCode');
    const si = params.get('si');
    const gu = params.get('gu');
    const dong = params.get('dong');

    return { legalCode, si,gu, dong };
}

function getCoordinates({si,gu,dong}) {
    if(!isVerifyInputFields(si,gu,dong)) {
        return;
    }

    $.ajax({
        url: `/api/third/google/geocoding?address=${si} ${gu} ${dong}`,
        type: "GET",
        success: async function (coordinate) {
            await initMap(coordinate.lat, coordinate.lng);
        },
        error: function (err) {
            console.error(err);
        }
    });
}