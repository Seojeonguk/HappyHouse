function getGoogleMapApiKey() {
    return fetch("/api/third/getGoogleApiKey", {method: "POST"})
        .then(res => res.text());
}

function getGoogleGeocoding(address) {
    return fetch(`/api/third/google/geocoding?address=${address}`, {
        method:"GET"
    }) .then(res=>res.json());
}