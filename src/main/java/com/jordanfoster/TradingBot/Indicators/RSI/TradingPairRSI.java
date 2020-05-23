package com.jordanfoster.TradingBot.Indicators.RSI;

import com.jordanfoster.TradingBot.Indicators.TradingPairIndicator;

import java.util.ArrayList;

public class TradingPairRSI extends TradingPairIndicator {

    public long date;
    public double close;
    public double change;
    public double gain;
    public double loss;
    public double avgGain;
    public double avgLoss;
    public double RSI;

    public TradingPairRSI(long date, double close){
        this.date = date;
        this.close = close;
    }

    public TradingPairRSI(long date, double close, double change, double gain, double loss){
        this.date = date;
        this.close = close;
        this.change = change;
        this.gain = gain;
        this.loss = loss;
    }

    public TradingPairRSI(long date, double close, double change, double gain, double loss, double avgGain, double avgLoss, double RSI){
        this.date = date;
        this.close = close;
        this.change = change;
        this.gain = gain;
        this.loss = loss;
        this.avgGain = avgGain;
        this.avgLoss = avgLoss;
        this.RSI = RSI;
    }
//    private DataRSI dataRSI = new DataRSI();
//
//    public void add(long date, double close){
//        dataRSIArrayList.add(new DataRSI(date, close));
//    }
//
//    public void add(long date, double close, double change, double gain, double loss){
//        dataRSIArrayList.add(new DataRSI(date, close, change, gain, loss));
//    }
//
//    public void add(long date, double close, double change, double gain, double loss, double avgGain, double avgLoss, double RSI){
//        dataRSIArrayList.add(new DataRSI(date, close, change, gain, loss, avgGain, avgLoss, RSI));
//    }
//
//    public DataRSI get(int index){
//        return dataRSIArrayList.get(index);
//    }
}

