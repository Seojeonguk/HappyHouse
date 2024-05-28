package com.example.happyhouse.domain.repository;

import com.example.happyhouse.domain.entity.Geocoding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeocodingRepository extends JpaRepository<Geocoding, Long> {
    Optional<Geocoding> findByAddress(String address);
}
