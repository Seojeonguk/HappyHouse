package com.example.happyhouse.domain.service;

import com.example.happyhouse.domain.dto.response.LegalCodeRes;

import java.io.IOException;
import java.util.List;

public interface FileService {
    List<LegalCodeRes> getLegalCodes(String upperRegionCode) throws IOException;
}
