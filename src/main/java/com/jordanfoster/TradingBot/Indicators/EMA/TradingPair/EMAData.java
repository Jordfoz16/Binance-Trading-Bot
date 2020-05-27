package com.jordanfoster.TradingBot.Indicators.EMA.TradingPair;

public class EMAData{

    /*
    EMAData - is an object data structure for storing EMA data
    for each candle stick.
     */

    public double close;
    public double EMA;

    public EMAData(double close, double EMA){
        this.close = close;
        this.EMA = EMA;
    }
}
