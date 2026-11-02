package com.pluralsight;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String DATE;
    private String TIME;
    private String item;
    private String vendor;
    private double amount;


    public Transaction(String DATE, String TIME, String item, String vendor, double amount){
        this.DATE = DATE;
        this.TIME = TIME;
        this.item = item;
        this.vendor = vendor;
        this.amount = amount;
    }

    public String DATE() {
        return DATE;
    }

    public String TIME() {
        return TIME;
    }

    public String vendor() {
        return vendor;
    }

    public double amount() {
        return amount;
    }

    public String item() {
        return item;
    }

    public LocalDateTime getDateTime(){
        return LocalDateTime.parse(DATE + "|" + TIME, DateTimeFormatter.ISO_DATE_TIME);
    }

    @Override
    public String toString() {
        return DATE + "|" + TIME + "|" + item + "|" + vendor + "|" + amount;
    }
}

