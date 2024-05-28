package com.example.happyhouse.domain.repository;

import com.example.happyhouse.domain.entity.ApartmentDetailInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentDetailRepository extends JpaRepository<ApartmentDetailInformation, Long> {
    List<ApartmentDetailInformation> findByComplexCode(String complexCode);
}
