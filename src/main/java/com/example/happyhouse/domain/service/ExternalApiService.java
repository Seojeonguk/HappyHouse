package com.example.happyhouse.domain.service;

import com.example.happyhouse.domain.dto.response.ApartTradeRes;
import com.example.happyhouse.domain.dto.response.GeocodingRes;
import com.example.happyhouse.domain.dto.response.HouseTradeRes;

import java.io.IOException;
import java.util.List;

public interface ExternalApiService {
    GeocodingRes getGeocoding(String address) throws IOException;
    String getGoogleApiKey();
    List<ApartTradeRes> getApartTrade() throws IOException;
    List<HouseTradeRes> getHouseTrade() throws IOException;
}
