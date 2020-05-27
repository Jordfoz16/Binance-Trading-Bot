package com.jordanfoster.TradingBot.Orderbook;

public class SoldTradingPair {

    private double boughtPrice;
    private double soldPrice;
    private double amount;
    private double change;

    public SoldTradingPair(double boughtPrice, double soldPrice, double amount){
        this.boughtPrice = boughtPrice;
        this.soldPrice = soldPrice;
        this.amount = amount;

        change = boughtPrice - soldPrice;
    }

    public double getProfit(){
        return change;
    }
}
