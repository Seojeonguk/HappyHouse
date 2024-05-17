package com.example.happyhouse.domain.service;

import com.example.happyhouse.domain.dto.response.GeocodingRes;

import java.io.IOException;

public interface ExternalApiService {
    GeocodingRes getGeocoding(String address) throws IOException;
    String getGoogleApiKey();
}
