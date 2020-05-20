package com.jordanfoster.TradingBot.PriceFeed;

import com.jordanfoster.Networking.BinanceAPI;

import java.util.ArrayList;

public abstract class PriceFeed {

    protected BinanceAPI binanceAPI;

    protected boolean initialized = false;

    protected ArrayList<TradingPair> tradingPairs = new ArrayList<TradingPair>();

    public PriceFeed(){
        binanceAPI = new BinanceAPI();
    }

    public abstract void update();

    public void clear(){
        tradingPairs.clear();
        initialized = false;
    }

    public ArrayList<TradingPair> getTradingPairs(){
        return tradingPairs;
    }

    public TradingPair getTradingPair(int index){
        return tradingPairs.get(index);
    }
}
