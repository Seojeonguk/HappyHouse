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
    const blueMarker = {
        path: "M10.453 14.016l6.563-6.609-1.406-1.406-5.156 5.203-2.063-2.109-1.406 1.406zM12 2.016q2.906 0 4.945 2.039t2.039 4.945q0 1.453-0.727 3.328t-1.758 3.516-2.039 3.070-1.711 2.273l-0.75 0.797q-0.281-0.328-0.75-0.867t-1.688-2.156-2.133-3.141-1.664-3.445-0.75-3.375q0-2.906 2.039-4.945t4.945-2.039z",
        fillColor: "blue",
        fillOpacity: 0.6,
        strokeWeight: 0,
        rotation: 0,
        scale: 2,
        anchor: new google.maps.Point(15, 30)
    };
    const redMarker = {
        path: "M10.453 14.016l6.563-6.609-1.406-1.406-5.156 5.203-2.063-2.109-1.406 1.406zM12 2.016q2.906 0 4.945 2.039t2.039 4.945q0 1.453-0.727 3.328t-1.758 3.516-2.039 3.070-1.711 2.273l-0.75 0.797q-0.281-0.328-0.75-0.867t-1.688-2.156-2.133-3.141-1.664-3.445-0.75-3.375q0-2.906 2.039-4.945t4.945-2.039z",
        fillColor: "red",
        fillOpacity: 0.6,
        strokeWeight: 0,
        rotation: 0,
        scale: 2,
        anchor: new google.maps.Point(15, 30)
    };
    var marker = new google.maps.Marker({
        position: {
            lat: lat,
            lng: lng
        },
        map,
        icon: color == 1 ? blueMarker : redMarker
    });
    attach_message(marker, tradeInfo, color);
}

function attach_message(marker, tradeInfo, color) {
    let inputBtn = `<input type="button" onclick="javascript:detail_btn($(this));" color=${color} id=${tradeInfo.apartmentName} title=${tradeInfo.apartmentName} value="자세히보기"/>`;
    let tradeInfoContent = new google.maps.InfoWindow({
        content: `${tradeInfo.apartmentName}<br/>가격 : ${tradeInfo.dealAmount}<br/>건축연도 : ${tradeInfo.constructionYear}<br/>${inputBtn}`,
        size: new google.maps.Size(50, 50)
    });

    google.maps.event.addListener(marker, 'click', function () { // 이벤트 등록
        tradeInfoContent.open(map, marker);
    });
}

function google_geocoding(si, gun, gu, item_list, color) {
    var parameter = "si=" + si + "&gun=" + gun + "&gu=" + gu + "&item=" + item_list[0];
    $.ajax({
        url: 'googlegecodoing.jsp',
        type: 'post',
        data: parameter,
        success: function (response) {
            var lat_lng = response.trim().split("|");
            mapMarking(lat_lng[0], lat_lng[1], item_list, color);
        },
        error: function () {
            console.log('google_geocoding error!');
        }
    });
}

function detail_btn(item) {
    if (item.attr('color') == 0) {
        detail_apart_search(item.attr('title'));
    } else if (item.attr('color') == 1) {
        detail_house_search(item.attr('title'));
    }
}