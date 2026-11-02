package com.pluralsight;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String date;
    private String time;
    private String item;
    private String vendor;
    private double amount;


    public Transaction(String date, String time, String item, String vendor, double amount){
        this.date = date;
        this.time = time;
        this.item = item;
        this.vendor = vendor;
        this.amount = amount;
    }

    public String date() {
        return date;
    }

    public String time() {
        return time;
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
        return LocalDateTime.parse(date + "|" + time, DateTimeFormatter.ISO_DATE_TIME);
    }

    @Override
    public String toString() {
        return date + "|" + time + "|" + item + "|" + vendor + "|" + amount;
    }
}

