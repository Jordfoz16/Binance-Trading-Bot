package com.jordanfoster.TradingBot.Orderbook;

public class BoughtTradingPair{

    /*
    BoughtTradingPair - is a object data structure for all
    open positions. It stores the price and the amount of the position.
     */

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
