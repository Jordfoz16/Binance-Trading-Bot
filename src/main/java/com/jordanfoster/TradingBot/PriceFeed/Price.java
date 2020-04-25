package com.jordanfoster.TradingBot.PriceFeed;

import java.util.ArrayList;

public class Price {

    private String symbol = "";
    private int listLength = 600;

    private ArrayList<Double> priceHistory = new ArrayList<Double>();

    public Price(String symbol){
        this.symbol = symbol;
    }

    public void addPrice(double price){
        if(priceHistory.size() > listLength){
            priceHistory.remove(0);
        }

        priceHistory.add(price);
    }

    public ArrayList<Double> getPriceHistory(){
        return priceHistory;
    }

    public double getCurrentPrice(){
        return priceHistory.get(priceHistory.size() - 1);
    }

    public String getSymbol(){
        return symbol;
    }
}
