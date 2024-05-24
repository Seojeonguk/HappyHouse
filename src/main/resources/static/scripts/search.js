$(document).ready(async function () {
    const params = loadQueryParams("category", "legalCode", "si", "gu", "dong", "year", "month");

    initSearchForm(params);
    loadGoogleMap();

    const coordinate = await getCoordinate(`${params.si} ${params.gu} ${params.dong}`);
    await initMap(coordinate.lat, coordinate.lng);

    //getTrade(params);
    getInformation(params);
});