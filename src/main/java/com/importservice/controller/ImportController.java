package com.importservice.controller;

import com.importservice.dto.ImportStatistics;
import com.importservice.service.CSVImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@RequestMapping("/api/import")
public class ImportController {

    private final CSVImportService csvImportService;

    public ImportController(CSVImportService csvImportService) {
        this.csvImportService = csvImportService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<ImportStatistics>> importCsv(@RequestParam("file") MultipartFile file) {
        return csvImportService.importCSV(file)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    log.error("Error during CSV import: {}", ex.getMessage());
                    return ResponseEntity.badRequest().build();
                });
    }

}

