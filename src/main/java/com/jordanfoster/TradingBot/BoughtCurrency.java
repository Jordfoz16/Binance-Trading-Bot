package com.jordanfoster.TradingBot;

public class BoughtCurrency {

    private String symbol;
    private double price;

    public BoughtCurrency(String symbol, double price){
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol(){
        return symbol;
    }

    public double getPrice(){
        return price;
    }

    public void printBought(){
        System.out.print(symbol + " - " + price + " | ");
    }
}
