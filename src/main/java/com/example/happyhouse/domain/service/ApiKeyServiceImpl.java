package com.example.happyhouse.domain.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {

    @Value("${google.api-key}")
    private String googleMapApiKey;

    @Override
    public String provideGoogleMapApiKey() {
        return googleMapApiKey;
    }
}