$(document).ready(function () {
    initSearch();

    loadGoogleMap();

    const params = loadQueryParam();
    getCoordinates(params);

    if (params.category === "전체") {
        apart_search(params);
        house_search(params);
    } else if (params.category === "아파트") {
        apart_search(params);
    } else if (params.category === "다세대") {
        house_search(params);
    }

})

function loadQueryParam() {
    const params = new URLSearchParams(window.location.search);

    const category = params.get('category');
    const legalCode = params.get('legalCode');
    const si = params.get('si');
    const gu = params.get('gu');
    const dong = params.get('dong');

    return {category, legalCode, si, gu, dong};
}

function getCoordinates({si, gu, dong}) {
    if (!isVerifyInputFields(si, gu, dong)) {
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