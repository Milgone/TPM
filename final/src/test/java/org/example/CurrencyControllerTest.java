package org.example;

import org.example.dto.CurrencyRateDto;
import org.example.service.ParserService;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CurrencyControllerTest {

    @Test
    void getRates_returnsListFromService() {
        ParserService stubService = new ParserService(null) {
            @Override
            public List<CurrencyRateDto> getFilteredRateDtos(String name) {
                return List.of(new CurrencyRateDto("BTC", 100.0, "+1%", null));
            }
        };

        var controller = new org.example.controller.CurrencyController(stubService);

        var result = controller.getRates("BTC");

        org.assertj.core.api.Assertions.assertThat(result)
                .hasSize(1)
                .first()
                .extracting(CurrencyRateDto::getName)
                .isEqualTo("BTC");
    }
}

