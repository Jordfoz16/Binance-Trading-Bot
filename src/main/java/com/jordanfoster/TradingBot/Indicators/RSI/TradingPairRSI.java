package com.jordanfoster.TradingBot.Indicators.RSI;

import com.jordanfoster.TradingBot.Indicators.TradingPairIndicator;
import com.jordanfoster.TradingBot.PriceFeed.CandleStick.CandleStickData;

import java.util.ArrayList;

public class TradingPairRSI extends TradingPairIndicator {

    public String symbol;

    private ArrayList<RSIData> rsiData = new ArrayList<>();

    public TradingPairRSI(String symbol){
        super(symbol);
    }

    public void addRSI(long date, double close){
        add(new RSIData(date, close));
    }

    public void addRSI(long date, double close, double change, double gain, double loss){
        add(new RSIData(date, close, change, gain, loss));
    }

    public void addRSI(long date, double close, double change, double gain, double loss, double avgGain, double avgLoss, double RSI){
        add(new RSIData(date, close, change, gain, loss, avgGain, avgLoss, RSI));
    }

    private void add(RSIData newData){
        rsiData.add(newData);
    }

    public RSIData getCandle(int index){
        return rsiData.get(index);
    }

}

