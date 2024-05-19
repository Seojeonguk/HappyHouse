package com.example.happyhouse.domain.service;

import com.example.happyhouse.domain.dto.response.GeocodingRes;
import com.example.happyhouse.domain.dto.response.TradeRes;

import java.io.IOException;
import java.util.List;

public interface ExternalApiService {
    GeocodingRes getGeocoding(String address) throws IOException;
    String getGoogleApiKey();
    List<TradeRes> getTrade(String category,String legalCode) throws IOException;
}
