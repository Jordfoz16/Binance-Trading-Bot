package com.jordanfoster.TradingBot.Indicators.RSI;

import com.jordanfoster.TradingBot.Indicators.EMA.EMAValue;
import com.jordanfoster.TradingBot.TradingBot;

import java.util.ArrayList;

public class RSIValue {

    public enum State{
        BUY,
        SELL,
        NONE,
        HOLD,
        CALIBRATION
    }

    private double prevAverageGains = 0;
    private double prevAverageLosses = 0;

    private ArrayList<Double> rsiValues = new ArrayList<Double>();

    private State state = State.NONE;

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

    public void setState(RSIValue.State state) {
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

    public RSIValue.State getState(){
        return state;
    }

    public String getStateString(){
        return state.toString();
    }

}
