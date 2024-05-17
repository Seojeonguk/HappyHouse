$(function () {
    load_si_data();

    $("#search-btn").click(async function () {
        let si = $("select[name='si']").val();
        let gu = $("select[name='gu']").val();
        let dong = $("select[name='dong']").val();

        let str = si;
        if (!isEmpty(gu)) {
            str += " " + gu;
        }

        if (!isEmpty(dong)) {
            str += " " + dong;
        }

        $('.content-left').children().not('.content-left-title').remove();

        const geocoding = await getGoogleGeocoding(str);
        await initMap(geocoding.lat, geocoding.lng);

        var category = $("select[name='category']").val();
        code_search();
        if (category == "전체" || category == "아파트") {
            apart_search(si, gu, dong);
        }
        if (category == "전체" || category == "연립다세대") {
            housing_search(si, gu, dong);
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

    $("select[name='si']").change(function () {
        load_gu_data();
    });

    $("select[name='gu']").change(function () {
        load_dong_data();
    });
});

$(document).ready(function () {
    loadGoogleMap()
        .then(() => console.log('success loading google map.'))
        .catch((err) => console.error(err));
})