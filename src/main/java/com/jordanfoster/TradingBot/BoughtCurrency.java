package com.jordanfoster.TradingBot;

public class BoughtCurrency {

    private String symbol;
    private double price;
    private double amount;

    public BoughtCurrency(String symbol, double price, double amount){
        this.symbol = symbol;
        this.price = price;
        this.amount = amount;
    }

    public void setSymbol(String symbol){
        this.symbol = symbol;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setAmount(double amount){
        this.amount = amount;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public double getAmount() {
        return amount;
    }
}

