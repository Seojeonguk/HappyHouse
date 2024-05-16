const getGoogleMapApiKey = () => {
    return fetch("/api/getGoogleApiKey", {method: "POST"})
        .then(res => res.text());
}