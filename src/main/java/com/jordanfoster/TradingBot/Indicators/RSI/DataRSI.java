package com.jordanfoster.TradingBot.Indicators.RSI;

import com.jordanfoster.TradingBot.Indicators.Data;

public class DataRSI extends Data {

    private double prevAverageGains = 0;
    private double prevAverageLosses = 0;

    public void setPrevAverage(double averageGains, double averageLosses){
        this.prevAverageGains = averageGains;
        this.prevAverageLosses = averageLosses;
    }

    public double getPrevAverageGains(){
        return prevAverageGains;
    }

    public double getPrevAverageLosses(){
        return prevAverageLosses;
    }
}
