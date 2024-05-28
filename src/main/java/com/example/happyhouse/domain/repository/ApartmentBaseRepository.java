package com.example.happyhouse.domain.repository;

import com.example.happyhouse.domain.entity.ApartmentBaseInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentBaseRepository extends JpaRepository<ApartmentBaseInformation, Long> {
    List<ApartmentBaseInformation> findByComplexCode(String complexCode);
}
