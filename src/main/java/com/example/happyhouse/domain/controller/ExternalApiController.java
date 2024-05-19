package com.example.happyhouse.domain.controller;

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

    @GetMapping("/getTrade")
    public ResponseEntity<List<TradeRes>> getTrade(@RequestParam String category, @RequestParam String legalCode) throws IOException {
        return ResponseEntity.ok(externalApiService.getTrade(category,legalCode));
    }
}
