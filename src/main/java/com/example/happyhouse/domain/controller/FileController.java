package com.example.happyhouse.domain.controller;

import com.example.happyhouse.domain.dto.response.LegalCodeRes;
import com.example.happyhouse.domain.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;

    @GetMapping("/legalCode")
    public ResponseEntity<List<LegalCodeRes>> getLegalCode(@RequestParam String upperRegionCode) throws IOException {
        return ResponseEntity.ok(fileService.getLegalCodes(upperRegionCode));
    }
}
