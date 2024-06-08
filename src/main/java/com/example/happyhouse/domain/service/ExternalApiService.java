package com.example.happyhouse.domain.service;

import com.example.happyhouse.domain.dto.request.ApartmentDetailInfoReq;
import com.example.happyhouse.domain.dto.request.TradeReq;
import com.example.happyhouse.domain.dto.response.ApartmentBaseInfoRes;
import com.example.happyhouse.domain.dto.response.ApartmentDetailInfoRes;
import com.example.happyhouse.domain.dto.response.GeocodingRes;
import com.example.happyhouse.domain.dto.response.TradeRes;

import java.io.IOException;
import java.util.List;

public interface ExternalApiService {
    GeocodingRes getGeocoding(String address) throws IOException;

    String getGoogleApiKey();

    List<TradeRes> getTrade(TradeReq tradeReq) throws IOException;

    List<ApartmentBaseInfoRes> getBaseInfo(TradeReq tradeReq) throws IOException;

    ApartmentDetailInfoRes getDetailInfo(ApartmentDetailInfoReq apartmentDetailInfoReq) throws IOException;
}
