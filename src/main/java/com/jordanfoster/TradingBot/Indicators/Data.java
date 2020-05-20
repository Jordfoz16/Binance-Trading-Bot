package com.jordanfoster.TradingBot.Indicators;

import com.jordanfoster.TradingBot.TradingBot;

import java.util.ArrayList;

public abstract class Data {

    protected ArrayList<Double> data = new ArrayList<Double>();
    protected TradingBot.State state = TradingBot.State.NONE;

    public void addValue(double value){
        data.add(value);
        checkListSize();
    }

    protected void checkListSize(){

        int listSize = Integer.parseInt(TradingBot.fileConfig.getElement("price-feed", "price-history-size"));

        if(data.size() > listSize){
            data.remove(0);
        }
    }

    public void setState(TradingBot.State state){
        this.state = state;
    }

    public Double get(int index){
        return data.get(index);
    }

    public Double getCurrent(){
        return data.get(data.size() - 1);
    }

    public ArrayList<Double> getValues(){
        return data;
    }

    public TradingBot.State getState(){
        return state;
    }

    public String getStateString(){
        return state.toString();
    }
}
