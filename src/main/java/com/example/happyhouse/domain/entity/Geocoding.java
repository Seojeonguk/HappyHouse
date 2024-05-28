package com.example.happyhouse.domain.entity;

import com.example.happyhouse.domain.dto.response.GeocodingRes;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Geocoding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private String lat;
    private String lng;
    private String formattedAddress;

    public Geocoding(String address, String lat, String lng, String formattedAddress) {
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.formattedAddress = formattedAddress;
    }

    public GeocodingRes toResponse() {
        return new GeocodingRes(this.lat, this.lng, this.formattedAddress);
    }
}
