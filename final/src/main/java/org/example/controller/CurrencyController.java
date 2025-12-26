package org.example.controller;

import org.example.model.CurrencyRate;
import org.example.service.ParserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/answer")
public class CurrencyController {
    private final ParserService parserService;
    public CurrencyController(ParserService parserService) { this.parserService = parserService; }

    @GetMapping
    public List<CurrencyRate> getRates(@RequestParam(required = false) String name) {
        if (name != null) return parserService.getFilteredRates(name);
        return parserService.getFilteredRates(""); // Вернет пусто или доп. логику
    }
}
