package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CurrencyRate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private String changeDay;
    private LocalDateTime captureDate;

    // Геттеры, сеттеры, конструктор (для краткости пропущено, сгенерируй в IDEA)
    public CurrencyRate() {}
    public CurrencyRate(String name, Double price, String change, LocalDateTime date) {
        this.name = name; this.price = price; this.changeDay = change; this.captureDate = date;
    }
    public String getName() { return name; }
    public Double getPrice() { return price; }
}