package com.jordanfoster.TradingBot.Indicators.RSI;

import com.jordanfoster.TradingBot.TradingBot;

import java.util.ArrayList;

public class RSIValue {

    private double averageGains = 0;
    private double averageLosses = 0;

    private ArrayList<Double> gains = new ArrayList<Double>();
    private ArrayList<Double> losses = new ArrayList<Double>();

    private ArrayList<Double> rsiValues = new ArrayList<Double>();

    public void addGains(double value){
        gains.add(value);
        checkGainsListSize();
    }

    public void addLosses(double value){
        losses.add(value);
        checkLossesListSize();
    }

    public void addRSIValue(double value){
        rsiValues.add(value);
        checkListSize();
    }

    private void checkGainsListSize(){
        int listSize = RSI.rsiPeriod;

        if(gains.size() > listSize){
            gains.remove(0);
        }
    }

    private void checkLossesListSize(){
        int listSize = RSI.rsiPeriod;

        if(losses.size() > listSize){
            losses.remove(0);
        }
    }

    private void checkListSize(){
        int listSize = Integer.parseInt(TradingBot.fileConfig.getElement("price-feed", "price-history-size"));

        if(rsiValues.size() > listSize){
            rsiValues.remove(0);
        }
    }

    public ArrayList<Double> getGains(){
        return gains;
    }

    public ArrayList<Double> getLosses(){
        return losses;
    }

    public ArrayList<Double> getRsiValues(){
        return rsiValues;
    }

}
