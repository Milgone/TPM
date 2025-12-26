package org.example.parser.model; // Убедитесь, что пакет такой!

public class CurrencyTask implements Runnable {
    private final String currencyName;
    private final int sequenceNumber;

    public CurrencyTask(String currencyName, int sequenceNumber) {
        this.currencyName = currencyName;
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public void run() {
        System.out.println("Поток: " + Thread.currentThread().getName() +
                " | Задача №" + sequenceNumber +
                " | Валюта: " + currencyName);
    }
}