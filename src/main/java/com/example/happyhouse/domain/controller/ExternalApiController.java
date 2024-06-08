package com.example.happyhouse.domain.controller;

import com.example.happyhouse.domain.dto.request.ApartmentDetailInfoReq;
import com.example.happyhouse.domain.dto.request.TradeReq;
import com.example.happyhouse.domain.dto.response.ApartmentBaseInfoRes;
import com.example.happyhouse.domain.dto.response.ApartmentDetailInfoRes;
import com.example.happyhouse.domain.dto.response.GeocodingRes;
import com.example.happyhouse.domain.dto.response.TradeRes;
import com.example.happyhouse.domain.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/third")
public class ExternalApiController {

    private final ExternalApiService externalApiService;

    @GetMapping("/google/geocoding")
    public ResponseEntity<GeocodingRes> geocoding(@RequestParam String address) throws IOException {
        return ResponseEntity.ok(externalApiService.getGeocoding(address));
    }

    @PostMapping("/getGoogleApiKey")
    public ResponseEntity<String> getGoogleMapApiKey() {
        return ResponseEntity.ok(externalApiService.getGoogleApiKey());
    }

    @PostMapping("/getTrade")
    public ResponseEntity<List<TradeRes>> getTrade(@RequestBody TradeReq tradeReq) throws IOException {
        return ResponseEntity.ok(externalApiService.getTrade(tradeReq));
    }

    @PostMapping("/getInformation")
    public ResponseEntity<List<ApartmentBaseInfoRes>> getBaseInfo(@RequestBody TradeReq tradeReq) throws IOException {
        return ResponseEntity.ok(externalApiService.getBaseInfo(tradeReq));
    }

    @PostMapping("/getDetailInformation")
    public ResponseEntity<ApartmentDetailInfoRes> getDetailInfo(@RequestBody ApartmentDetailInfoReq apartmentDetailInfoReq) throws IOException {
        return ResponseEntity.ok(externalApiService.getDetailInfo(apartmentDetailInfoReq));
    }
}
