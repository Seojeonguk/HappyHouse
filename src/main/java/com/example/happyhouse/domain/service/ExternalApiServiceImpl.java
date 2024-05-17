package com.example.happyhouse.domain.service;

import com.example.happyhouse.domain.dto.response.GeocodingRes;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class ExternalApiServiceImpl implements ExternalApiService {

    @Value("${google.api-key}")
    private String googleApiKey;

    @Value("${google.geocoding.endpoint}")
    private String googleGeocodingEndpoint;

    @Override
    public GeocodingRes getGeocoding(String address) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(googleGeocodingEndpoint);
        urlBuilder.append("/json");
        urlBuilder.append("?" + URLEncoder.encode("address", StandardCharsets.UTF_8) + "=" + address.replaceAll(" ","+"));
        urlBuilder.append("&" + URLEncoder.encode("key", StandardCharsets.UTF_8) + "=" + URLDecoder.decode(googleApiKey, StandardCharsets.UTF_8));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        JSONObject response = new JSONObject(sb.toString());
        JSONObject location = response
                .getJSONArray("results")
                .getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONObject("location");

        String lat = String.valueOf(location.getBigDecimal("lat"));
        String lng = String.valueOf(location.getBigDecimal("lng"));

        return new GeocodingRes(lat, lng);
    }
}
