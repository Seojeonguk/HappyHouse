let map;

function loadGoogleMap(isCallback = false, params = {}) {

    $.ajax({
        url: '/api/third/getGoogleApiKey',
        type: "POST",
        beforeSend: function (xhr) {
            const accessToken = localStorage.getItem("accessToken");
            const grantType = localStorage.getItem("grantType");
            if (isEmpty(grantType)) {
                return;
            }
            xhr.setRequestHeader("Authorization", `${grantType} ${accessToken}`);
        },
        success: function (key) {
            let script = document.createElement('script');
            script.src = `https://maps.googleapis.com/maps/api/js?key=${key}&callback=initMap&loading=async`;
            script.async = true;

            if (!isEmpty(params)) {
                script.onload = async function () {
                    const coordinate = await getCoordinate(`${params.si} ${params.gu} ${params.dong}`);
                    await initMap(coordinate.lat, coordinate.lng);
                }
            }

            document.head.appendChild(script);
        },
        error: function (err) {
            if (isCallback) {
                console.error(err);
                return;
            }

            refreshAccessToken(loadGoogleMap);
        }
    });
}

async function initMap(lat = 35.1595454, lng = 126.8526012) {
    lat *= 1;
    lng *= 1;

    const {Map} = await google.maps.importLibrary("maps");
    const {AdvancedMarkerElement, PinElement} = await google.maps.importLibrary("marker");

    map = new Map(document.getElementById("map"), {
        center: {lat: lat, lng: lng},
        zoom: 11,
        mapId: 'map'
    });
}

function mapMarking(lat, lng, data) {
    lat *= 1;
    lng *= 1;

    let marker = new google.maps.marker.AdvancedMarkerElement({
        position: {
            lat: lat,
            lng: lng
        },
        map,
        content: buildContent(data),
        title: 'apt_list'
    });

    marker.addListener("click", () => {
        toggleHighlight(marker);
    });
}

function getCoordinate(address) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: `/api/third/google/geocoding?address=${address}`,
            type: "GET",
            beforeSend: function (xhr) {
                const accessToken = localStorage.getItem("accessToken");
                const grantType = localStorage.getItem("grantType");
                if (isEmpty(grantType)) {
                    return;
                }
                xhr.setRequestHeader("Authorization", `${grantType} ${accessToken}`);
            },
            success: function (coordinate) {
                resolve(coordinate);
            },
            error: function (err) {
                console.error(err);
                reject(err);
            }
        });
    });
}

function toggleHighlight(markerView) {
    if (markerView.content.classList.contains("highlight")) {
        markerView.content.classList.remove("highlight");
        markerView.zIndex = null;
    } else {
        markerView.content.classList.add("highlight");
        markerView.zIndex = 1;
    }
}

function buildContent(data) {
    const {complexName, complexCategory, householdCount, formattedAddress, approvalDate, buildingCount} = data;
    const approvalYear = approvalDate.substring(0, 4);
    const content = document.createElement("div");
    content.classList.add('property');

    createIcon('icon', complexCategory === "아파트" ? "fa-building" : "fa-home", content, false);

    const details = $("<div></div>").addClass("details");
    $("<div></div>").addClass("name").html(complexName).appendTo(details);
    $("<div></div>").addClass("address").html(formattedAddress).appendTo(details);

    const features = $("<div></div>").addClass("features");
    createIcon(approvalYear, 'fa-calendar', features, true);
    createIcon(buildingCount, 'fa-layer-group', features, true);
    createIcon(householdCount, 'fa-calendar', features, true);

    features.appendTo(details);
    details.appendTo(content);

    return content;
}

