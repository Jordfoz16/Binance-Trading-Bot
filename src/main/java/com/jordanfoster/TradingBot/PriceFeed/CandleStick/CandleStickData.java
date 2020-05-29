package com.jordanfoster.TradingBot.PriceFeed.CandleStick;

public class CandleStickData {

    /*
    CandleStickData - is an object data structure for storing
    candle stick data.
     */

    public long openTime;
    public double open;
    public double high;
    public double low;
    public double close;
    public double volume;
    public long closeTime;

    public CandleStickData(long openTime, double open, double high, double low, double close, double volume, long closeTime){
        this.openTime = openTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.closeTime = closeTime;
    }
}
