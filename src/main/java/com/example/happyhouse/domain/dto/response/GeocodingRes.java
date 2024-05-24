package com.example.happyhouse.domain.dto.response;

import lombok.Getter;

@Getter
public class GeocodingRes {
    String lat;
    String lng;
    String formattedAddress;

    public GeocodingRes(String lat, String lng, String formattedAddress) {
        this.lat = lat;
        this.lng = lng;
        this.formattedAddress = formattedAddress;
    }

    public GeocodingRes(GeocodingRes geocodingRes) {
        this.lat = geocodingRes.getLat();
        this.lng = geocodingRes.getLng();
        this.formattedAddress = geocodingRes.getFormattedAddress();
    }
}
