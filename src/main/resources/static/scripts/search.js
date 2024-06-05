$(document).ready(async function () {
    const params = loadQueryParams("category", "legalCode", "si", "gu", "dong", "year", "month");

    setHeader();
    initSearchForm(params);
    loadGoogleMap(false,params);

    //getTrade(params);
    getInformation(params);
});