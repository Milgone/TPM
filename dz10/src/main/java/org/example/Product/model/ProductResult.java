package org.example.model;

public record ProductResult(String id, String name, double price, String info) {
    @Override
    public String toString() {
        return String.format("ID: %s | %s | Price: %.2f | %s", id, name, price, info);
    }
}