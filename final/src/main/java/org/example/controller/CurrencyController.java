package org.example.controller;

import org.example.dto.CurrencyRateDto;
import org.example.service.ParserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/answer")
public class CurrencyController {

    private final ParserService parserService;

    public CurrencyController(ParserService parserService) {
        this.parserService = parserService;
    }

    @GetMapping
    public List<CurrencyRateDto> getRates(@RequestParam(required = false) String name) {
        return parserService.getFilteredRateDtos(name);
    }
}

