package com.example.happyhouse.domain.controller;

import com.example.happyhouse.domain.dto.response.ApartTradeRes;
import com.example.happyhouse.domain.dto.response.GeocodingRes;
import com.example.happyhouse.domain.dto.response.HouseTradeRes;
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

    @GetMapping("/getApartTrade")
    public ResponseEntity<List<ApartTradeRes>> getApartTrade() throws IOException {
        return ResponseEntity.ok(externalApiService.getApartTrade());
    }

    @GetMapping("/getHouseTrade")
    public ResponseEntity<List<HouseTradeRes>> getHouseTrade() throws IOException {
        return ResponseEntity.ok(externalApiService.getHouseTrade());
    }
}
