package com.jordanfoster.TradingBot.Indicators.RSI.TradingPair;

public class RSIData{
    public long date;
    public double close;
    public double change;
    public double gain;
    public double loss;
    public double avgGain;
    public double avgLoss;
    public double RSI;

    public RSIData(long date, double close){
        this.date = date;
        this.close = close;
    }

    public RSIData(long date, double close, double change, double gain, double loss){
        this.date = date;
        this.close = close;
        this.change = change;
        this.gain = gain;
        this.loss = loss;
    }

    public RSIData(long date, double close, double change, double gain, double loss, double avgGain, double avgLoss, double RSI){
        this.date = date;
        this.close = close;
        this.change = change;
        this.gain = gain;
        this.loss = loss;
        this.avgGain = avgGain;
        this.avgLoss = avgLoss;
        this.RSI = RSI;
    }
}
