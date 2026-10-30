package com.pluralsight;

public class Transaction {
    private String item;
    private String vendor;
    private double amount;

    public Transaction(String item, String vendor, double amount){
        this.item = item;
        this.vendor = vendor;
        this.amount = amount;
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

}

