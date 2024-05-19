$(document).ready(async function () {
    initSearch();
    loadGoogleMap();

    const params = loadQueryParams("category","legalCode","si","gu","dong");
    const coordinate = await getCoordinate(`${params.si} ${params.gu} ${params.dong}`);
    await initMap(coordinate.lat, coordinate.lng);

    if (params.category === "전체") {
        apart_search(params);
        house_search(params);
    } else if (params.category === "아파트") {
        apart_search(params);
    } else if (params.category === "다세대") {
        house_search(params);
    }
});