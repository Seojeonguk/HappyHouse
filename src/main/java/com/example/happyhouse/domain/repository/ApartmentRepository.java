package com.example.happyhouse.domain.repository;

import com.example.happyhouse.domain.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    List<Apartment> findByLegalDongCodeStartingWith(String legalDongCode);
}
