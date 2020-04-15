package com.jordanfoster.TradingBot.PriceFeed;

import java.util.ArrayList;

public class Price {

    private String symbol = "";
    private int listLength = 5;

    private ArrayList<Double> priceList = new ArrayList<Double>();

    public Price(String symbol){
        this.symbol = symbol;
    }

    public void addPrice(double price){
        if(priceList.size() > listLength){
            priceList.remove(0);
        }

        priceList.add(price);
    }

    public ArrayList<Double> getPriceList(){
        return priceList;
    }

    public double getLastPrice(){
        return priceList.get(priceList.size() - 1);
    }

    public String getSymbol(){
        return symbol;
    }
}
