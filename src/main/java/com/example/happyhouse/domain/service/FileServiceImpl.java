package com.example.happyhouse.domain.service;

import com.example.happyhouse.domain.dto.response.LegalCodeRes;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public List<LegalCodeRes> getLegalCodes(String upperRegionCode) throws IOException {
        ClassPathResource resource = new ClassPathResource("static/data/legalCode.txt");
        InputStream inputStream = resource.getInputStream();

        List<LegalCodeRes> legalCodeResList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("\t");
                if (upperRegionCode.equals(split[2])) {
                    legalCodeResList.add(new LegalCodeRes(split[0], split[3]));
                }
            }
        }

        legalCodeResList = legalCodeResList.stream()
                .sorted(Comparator.comparing(LegalCodeRes::getLegalName))
                .toList();

        return legalCodeResList;
    }
}
