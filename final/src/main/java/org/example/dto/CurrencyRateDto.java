package org.example.dto;

import java.time.LocalDateTime;

public class CurrencyRateDto {

    private String name;
    private Double price;
    private String changeDay;
    private LocalDateTime captureDate;

    public CurrencyRateDto() {
    }

    public CurrencyRateDto(String name, Double price, String changeDay, LocalDateTime captureDate) {
        this.name = name;
        this.price = price;
        this.changeDay = changeDay;
        this.captureDate = captureDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getChangeDay() {
        return changeDay;
    }

    public void setChangeDay(String changeDay) {
        this.changeDay = changeDay;
    }

    public LocalDateTime getCaptureDate() {
        return captureDate;
    }

    public void setCaptureDate(LocalDateTime captureDate) {
        this.captureDate = captureDate;
    }
}

