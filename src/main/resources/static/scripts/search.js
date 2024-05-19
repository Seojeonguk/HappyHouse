$(document).ready(async function () {
    initSearch();
    loadGoogleMap();

    const params = loadQueryParams("category","legalCode","si","gu","dong");
    const coordinate = await getCoordinate(`${params.si} ${params.gu} ${params.dong}`);
    await initMap(coordinate.lat, coordinate.lng);

    getTrade(params);
});