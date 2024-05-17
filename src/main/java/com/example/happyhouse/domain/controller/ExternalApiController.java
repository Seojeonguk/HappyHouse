package com.example.happyhouse.domain.controller;

import com.example.happyhouse.domain.dto.response.GeocodingRes;
import com.example.happyhouse.domain.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/third")
public class ExternalApiController {

    private final ExternalApiService externalApiService;

    @GetMapping("/google/geocoding")
    public ResponseEntity<GeocodingRes> geocoding(@RequestParam String address) throws IOException {
        return ResponseEntity.ok(externalApiService.getGeocoding(address));
    }

}