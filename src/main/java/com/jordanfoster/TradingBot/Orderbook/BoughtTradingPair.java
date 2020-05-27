package com.jordanfoster.TradingBot.Orderbook;

public class BoughtTradingPair{

    private double price;
    private double amount;

    public BoughtTradingPair(double price, double amount){
        this.price = price;
        this.amount = amount;
    }

    public double getPrice(){
        return price;
    }

    public double getAmount(){
        return amount;
    }
}
