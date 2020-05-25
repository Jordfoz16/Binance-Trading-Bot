package com.jordanfoster.TradingBot.Indicators.EMA;

import com.jordanfoster.TradingBot.Indicators.TradingPairIndicator;

import java.util.ArrayList;

public class TradingPairEMA extends TradingPairIndicator {

    public ArrayList<EMAData> emaSmall = new ArrayList<>();
    public ArrayList<EMAData> emaMed = new ArrayList<>();
    public ArrayList<EMAData> emaLarge = new ArrayList<>();

    public TradingPairEMA(String symbol) {
        super(symbol);
    }

    public void addEMASmall(double close, double EMA){
        emaSmall.add(new EMAData(close, EMA));
    }

    public void addEMAMed(double close, double EMA){
        emaMed.add(new EMAData(close, EMA));
    }

    public void addEMALarge(double close, double EMA){
        emaLarge.add(new EMAData(close, EMA));
    }

    public EMAData getCandleSmall(int index){
        return emaSmall.get(index);
    }
}

class EMAData{

    public double close;
    public double EMA;

    public EMAData(double close, double EMA){
        this.close = close;
        this.EMA = EMA;
    }
}
