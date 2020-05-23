package com.jordanfoster.TradingBot.PriceFeed;

import com.jordanfoster.TradingBot.PriceFeed.CandleStick.CandleStickData;

import java.util.ArrayList;

public class TradingPair {

    private String symbol;
    private ArrayList<CandleStickData> candleStickData = new ArrayList<CandleStickData>();

    public TradingPair(String symbol){
        this.symbol = symbol;
    }

    public void add(long openTime, double open, double high, double low, double close, double volume, long closeTime){
        candleStickData.add(new CandleStickData(openTime, open, high, low, close, volume, closeTime));
    }

    public CandleStickData getCandleStick(int index){
        return candleStickData.get(index);
    }

    public ArrayList<CandleStickData> getCandleStickData(){
        return candleStickData;
    }
}

