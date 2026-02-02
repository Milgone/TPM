package org.example;

import org.example.dto.CurrencyRateDto;
import org.example.model.CurrencyRate;
import org.example.service.ParserService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserServiceTest {

    @Test
    void getFilteredRateDtos_returnsMappedDtos() {
        CurrencyRate rate = new CurrencyRate("BTC", 123.45, "+1%", null);

        CurrencyRateDto dto = ParserService.toDto(rate);

        assertThat(dto.getName()).isEqualTo("BTC");
        assertThat(dto.getPrice()).isEqualTo(123.45);
        assertThat(dto.getChangeDay()).isEqualTo("+1%");
    }
}


