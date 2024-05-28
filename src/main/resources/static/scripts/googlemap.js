let map;

function loadGoogleMap() {

    $.ajax({
        url: '/api/third/getGoogleApiKey',
        type: "POST",
        success: function (key) {
            let script = document.createElement('script');
            script.src = `https://maps.googleapis.com/maps/api/js?key=${key}&callback=initMap`;
            script.async = true;

            document.head.appendChild(script);
        },
        error: function (err) {
            console.error(err);
        }
    });
}

async function initMap(lat = 35.1595454, lng = 126.8526012) {
    lat *= 1;
    lng *= 1;

    const {Map} = await google.maps.importLibrary("maps");
    const {AdvancedMarkerElement, PinElement} = await google.maps.importLibrary("marker");

    map = new Map(document.getElementById("map"), {
        center: {
            lat: lat,
            lng: lng
        },
        zoom: 11,
        mapId: 'map'
    });
}

function mapMarking(lat, lng, tradeInfo, color) {
    lat *= 1;
    lng *= 1;

    let marker = new google.maps.marker.AdvancedMarkerElement({
        position: {
            lat: lat,
            lng: lng
        },
        map,
        content: buildContent(tradeInfo),
        title: 'apt_list'
    });

    marker.addListener("click", () => {
        toggleHighlight(marker);
    });

    /*
    let tradeInfoContent = new google.maps.InfoWindow({
        content: `${tradeInfo.name}<br/>면적 : ${tradeInfo.exclusiveArea}${String.fromCodePoint(0x33A0)}<br/>건축연도 : ${tradeInfo.constructionYear}<br/>가격 : ${tradeInfo.dealAmount}`,
        size: new google.maps.Size(50, 50)
    });

    google.maps.event.addListener(marker, 'click', function () { // 이벤트 등록
        tradeInfoContent.open(map, marker);
    });

     */
}

function getCoordinate(address) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: `/api/third/google/geocoding?address=${address}`,
            type: "GET",
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

function buildContent(tradeInfo) {
    const content = document.createElement("div");
    content.classList.add('property');

    const icon = $("<div></div>").addClass("icon");
    $("<i></i>").attr('aria-hidden', 'true').attr('title', 'home')
        .addClass("fa").addClass("fa-icon").addClass("fa-solid").addClass(tradeInfo.apartmentTrading ? "fa-building" : "fa-home")
        .appendTo(icon);
    $("<span></span>").addClass('sr-only').html('home').appendTo(icon);

    icon.appendTo(content);

    const details = $("<div></div>").addClass("details");
    $("<div></div>").addClass("name").html(tradeInfo.name).appendTo(details);
    $("<div></div>").addClass("address").html(tradeInfo.formattedAddress).appendTo(details);
    $("<div></div>").addClass("price").html(tradeInfo.dealAmount).appendTo(details);
    $("<div></div>").addClass("dealDay").html(`${tradeInfo.dealYear}.${tradeInfo.dealMonth}.${tradeInfo.dealDay}`).appendTo(details);

    const features = $("<div></div>").addClass("features");
    // constructionYear exclusiveArea floor
    const constructionYear = $("<div></div>").addClass("constructionYear");
    $("<i></i>").attr('aria-hidden','true').attr('title','constructionYear')
        .addClass('fa').addClass('fa-icons').addClass('fa-calendar').addClass('fa-regular')
        .appendTo(constructionYear);
    $("<span></span>").addClass('sr-only').html(tradeInfo.constructionYear).appendTo(constructionYear);
    $("<span></span>").html(tradeInfo.constructionYear).appendTo(constructionYear);
    constructionYear.appendTo(features);

    const exclusiveArea = $("<div></div>").addClass("exclusiveArea");
    $("<i></i>").attr('aria-hidden','true').attr('title','exclusiveArea')
        .addClass('fa').addClass('fa-icons').addClass('fa-ruler')
        .appendTo(exclusiveArea);
    $("<span></span>").addClass('sr-only').html(tradeInfo.exclusiveArea).appendTo(exclusiveArea);
    $("<span></span>").html(tradeInfo.exclusiveArea).appendTo(exclusiveArea);
    exclusiveArea.appendTo(features);

    const floor = $("<div></div>").addClass("floor");
    $("<i></i>").attr('aria-hidden','true').attr('title','floor')
        .addClass('fa').addClass('fa-icons').addClass('fa-layer-group')
        .appendTo(floor);
    $("<span></span>").addClass('sr-only').html(tradeInfo.floor).appendTo(floor);
    $("<span></span>").html(tradeInfo.floor).appendTo(floor);
    floor.appendTo(features);

    features.appendTo(details);
    details.appendTo(content);

    return content;
}