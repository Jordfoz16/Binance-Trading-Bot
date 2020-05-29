package com.jordanfoster.TradingBot.Orderbook;

public class SoldTradingPair {

    /*
    SoldTradingPair - is a object data structure for all closed
    positions. It stores the trading pair, bought and sold price,
    amount and the change in bought and sold.
     */

    private String symbol;
    private double boughtPrice;
    private double soldPrice;
    private double amount;
    private double profit;

    public SoldTradingPair(String symbol, double boughtPrice, double soldPrice, double amount){
        this.symbol = symbol;
        this.boughtPrice = boughtPrice;
        this.soldPrice = soldPrice;
        this.amount = amount;

        profit = (soldPrice - boughtPrice) * amount;
    }

    public double getProfit(){
        return profit;
    }
}
