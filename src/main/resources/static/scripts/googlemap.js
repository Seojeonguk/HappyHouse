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

    map = new Map(document.getElementById("map"), {
        center: {
            lat: lat,
            lng: lng
        },
        zoom: 15
    });
}

function mapMarking(lat, lng, tradeInfo, color) {
    lat *= 1;
    lng *= 1;

    let marker = new google.maps.Marker({
        position: {
            lat: lat,
            lng: lng
        },
        map,
        icon: {
            path: "M10.453 14.016l6.563-6.609-1.406-1.406-5.156 5.203-2.063-2.109-1.406 1.406zM12 2.016q2.906 0 4.945 2.039t2.039 4.945q0 1.453-0.727 3.328t-1.758 3.516-2.039 3.070-1.711 2.273l-0.75 0.797q-0.281-0.328-0.75-0.867t-1.688-2.156-2.133-3.141-1.664-3.445-0.75-3.375q0-2.906 2.039-4.945t4.945-2.039z",
            fillColor: color,
            fillOpacity: 0.6,
            strokeWeight: 0,
            rotation: 0,
            scale: 2,
            anchor: new google.maps.Point(15, 30)
        }
    });

    let tradeInfoContent = new google.maps.InfoWindow({
        content: `${tradeInfo.apartmentName}<br/>면적 : ${tradeInfo.exclusiveArea}${String.fromCodePoint(0x33A0)}<br/>건축연도 : ${tradeInfo.constructionYear}<br/>가격 : ${tradeInfo.dealAmount}`,
        size: new google.maps.Size(50, 50)
    });

    google.maps.event.addListener(marker, 'click', function () { // 이벤트 등록
        tradeInfoContent.open(map, marker);
    });
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
