package com.jordanfoster.TradingBot.PriceFeed;

import com.jordanfoster.TradingBot.TradingBot;

import java.util.ArrayList;

public class TradingPair {

    private String symbol;
    private ArrayList<Double> priceList = new ArrayList<Double>();

    public TradingPair(String symbol){
        this.symbol = symbol;
    }

    public TradingPair(String symbol, double price){
        this.symbol = symbol;
        priceList.add(price);
    }

    public void addPrice(double price){
        priceList.add(price);
        checkListSize();
    }

    private void checkListSize(){
        int listSize = Integer.parseInt(TradingBot.fileConfig.getElement("price-feed", "price-history-size"));
        //Keep the list size to the current size
        if(priceList.size() > listSize){
            //Removes the oldest price from the list
            priceList.remove(0);
        }
    }

    public String getSymbol(){
        return symbol;
    }

    public double get(int index){
        return priceList.get(index);
    }

    public double getCurrentPrice(){
        return priceList.get(priceList.size() - 1);
    }

    public ArrayList<Double> getPriceList(){
        return priceList;
    }
}
