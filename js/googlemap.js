let map;
	
function initMap(lat=35.1595454,lng=126.8526012) {
	lat *= 1;
	lng *= 1;
	map = new google.maps.Map(document.getElementById("map"), {
	center: {
		lat: lat,
		lng: lng
	},
		zoom: 15
	});
};

function mapMarking(lat,lng,title,color) {
	lat *= 1;
	lng *= 1;
	const blueMarker = {
    path:
      "M10.453 14.016l6.563-6.609-1.406-1.406-5.156 5.203-2.063-2.109-1.406 1.406zM12 2.016q2.906 0 4.945 2.039t2.039 4.945q0 1.453-0.727 3.328t-1.758 3.516-2.039 3.070-1.711 2.273l-0.75 0.797q-0.281-0.328-0.75-0.867t-1.688-2.156-2.133-3.141-1.664-3.445-0.75-3.375q0-2.906 2.039-4.945t4.945-2.039z",
    fillColor: "blue",
    fillOpacity: 0.6,
    strokeWeight: 0,
    rotation: 0,
    scale: 2,
    anchor: new google.maps.Point(15, 30),
  };

  const redMarker = {
    path:
      "M10.453 14.016l6.563-6.609-1.406-1.406-5.156 5.203-2.063-2.109-1.406 1.406zM12 2.016q2.906 0 4.945 2.039t2.039 4.945q0 1.453-0.727 3.328t-1.758 3.516-2.039 3.070-1.711 2.273l-0.75 0.797q-0.281-0.328-0.75-0.867t-1.688-2.156-2.133-3.141-1.664-3.445-0.75-3.375q0-2.906 2.039-4.945t4.945-2.039z",
    fillColor: "red",
    fillOpacity: 0.6,
    strokeWeight: 0,
    rotation: 0,
    scale: 2,
    anchor: new google.maps.Point(15, 30),
  };
	marker = new google.maps.Marker({
		position:{lat:lat, lng: lng},
		map,
		title : title,
		label: title,
		icon : color==1 ? blueMarker : redMarker
	}).addListener("click",function() {
		if(this.getAnimation()!=null) {
			this.setAnimation(null);
		} else {
			this.setAnimation(google.maps.Animation.BOUNCE);
		}
	});
}


function google_geocoding(si,gun,gu,item,title,color) {
	var str = "si=" + si + "&gun=" + gun + "&gu=" + gu + "&item=" + item;
	$.ajax({
		url : 'googlegecodoing.jsp',
		type : 'post',
		data : str,
		success : function(response) {
			var lat_lng = response.trim().split("|");
			mapMarking(lat_lng[0],lat_lng[1],title,color);
		},
		error : function() {
			console.log('google_geocoding error!');
		}
	});
}