package com.jordanfoster.TradingBot.Indicators.RSI;

import com.jordanfoster.TradingBot.TradingBot;

import java.util.ArrayList;

public class DataRSI {

    private double prevAverageGains = 0;
    private double prevAverageLosses = 0;

    private ArrayList<Double> rsiValues = new ArrayList<Double>();

    private TradingBot.State state = TradingBot.State.NONE;

    public void addRSIValue(double value){
        rsiValues.add(value);
        checkListSize();
    }

    private void checkListSize(){
        int listSize = Integer.parseInt(TradingBot.fileConfig.getElement("price-feed", "price-history-size"));

        if(rsiValues.size() > listSize){
            rsiValues.remove(0);
        }
    }

    public void setPrevAverage(double averageGains, double averageLosses){
        this.prevAverageGains = averageGains;
        this.prevAverageLosses = averageLosses;
    }

    public void setState(TradingBot.State state) {
        this.state = state;
    }

    public ArrayList<Double> getRsiValues(){
        return rsiValues;
    }

    public double get(int index){
        return rsiValues.get(index);
    }

    public double getCurrent(){
        if(rsiValues.size() < 1) return 0;
        return rsiValues.get(rsiValues.size() - 1);
    }

    public double getPrevAverageGains(){
        return prevAverageGains;
    }

    public double getPrevAverageLosses(){
        return prevAverageLosses;
    }

    public TradingBot.State getState(){
        return state;
    }

    public String getStateString(){
        return state.toString();
    }

}
