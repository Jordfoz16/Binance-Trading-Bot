package com.jordanfoster.TradingBot.Indicators.EMA;

import com.jordanfoster.TradingBot.TradingBot;

import java.util.ArrayList;

public class EMAValue {

    public int buyWaitCounter = 0;
    public int sellWaitCounter = 0;

    private ArrayList<Double> emaValue = new ArrayList<Double>();

    private TradingBot.State state = TradingBot.State.NONE;

    public EMAValue(double value){
        addValue(value);
    }

    public void addValue(double value){
        emaValue.add(value);
        checkListSize();
    }

    private void checkListSize(){

        int listSize = Integer.parseInt(TradingBot.fileConfig.getElement("price-feed", "price-history-size"));

        if(emaValue.size() > listSize){
            emaValue.remove(0);
        }
    }

    public void setState(TradingBot.State state){
        this.state = state;
    }

    public Double get(int index){
        return emaValue.get(index);
    }

    public Double getCurrent(){
        return emaValue.get(emaValue.size() - 1);
    }

    public ArrayList<Double> getEmaValue(){
        return emaValue;
    }

    public Double getPrev(){
        if(emaValue.size() > 2){
            return emaValue.get(emaValue.size() - 2);
        }
        return emaValue.get(emaValue.size() - 1);
    }

    public TradingBot.State getState(){
        return state;
    }

    public String getStateString(){
        return state.toString();
    }
}
