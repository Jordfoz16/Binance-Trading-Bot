package com.jordanfoster.TradingBot.PriceFeed;

import java.util.ArrayList;

public class TradingPair {

    private String symbol;
    private ArrayList<Double> priceList = new ArrayList<Double>();
    private int listSize = 10;

    public TradingPair(String symbol, double price, int listSize){
        this.symbol = symbol;
        this.listSize = listSize;
        priceList.add(price);
    }

    public void addPrice(double price){
        priceList.add(price);
        checkListSize();
    }

    private void checkListSize(){
        //Keep the list size to the current size
        if(priceList.size() > listSize){
            //Removes the oldest price from the list
            priceList.remove(0);
        }
    }

    public String getSymbol(){
        return symbol;
    }

    public double getCurrentPrice(){
        return priceList.get(priceList.size() - 1);
    }

    public ArrayList<Double> getPriceList(){
        return priceList;
    }
}
