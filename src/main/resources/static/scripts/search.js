$(document).ready(async function () {
    initSearch();
    loadGoogleMap();

    makeYear();
    makeMonth();

    const params = loadQueryParams("category", "legalCode", "si", "gu", "dong", "year", "month");
    const coordinate = await getCoordinate(`${params.si} ${params.gu} ${params.dong}`);
    await initMap(coordinate.lat, coordinate.lng);

    getTrade(params);
});