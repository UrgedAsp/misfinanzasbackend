package com.jcastillo.misfinanzas.controller;

import com.jcastillo.misfinanzas.dto.Summary.MonthlySummaryResponse;
import com.jcastillo.misfinanzas.service.SummaryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/summary")
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping
    public ResponseEntity<MonthlySummaryResponse> monthlySummary(
            @RequestParam @Min(1) @Max(12) Integer month,
            @RequestParam @Min(2000) Integer year
    ) {
        return ResponseEntity.ok(summaryService.monthlySummary(month, year));
    }
}