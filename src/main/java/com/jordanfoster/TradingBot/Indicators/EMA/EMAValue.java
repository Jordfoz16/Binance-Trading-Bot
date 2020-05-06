package com.jordanfoster.TradingBot.Indicators.EMA;

import com.jordanfoster.TradingBot.TradingBot;

import java.util.ArrayList;

public class EMAValue {

    enum State{
        BUY,
        SELL,
        NONE,
        HOLD,
        CALIBRATION
    }

    public int buyWaitCounter = 0;
    public int sellWaitCounter = 0;
    public int calirationCounter = 0;

    private ArrayList<Double> emaValue = new ArrayList<Double>();

    private State state = State.NONE;

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

    public void setState(State state){
        this.state = state;
    }

    public Double get(int index){
        return emaValue.get(index);
    }

    public Double getCurrent(){
        return emaValue.get(emaValue.size() - 1);
    }

    public Double getPrev(){
        if(emaValue.size() > 2){
            return emaValue.get(emaValue.size() - 2);
        }
        return emaValue.get(emaValue.size() - 1);
    }

    public State getState(){
        return state;
    }

    public String getStateString(){
        return state.toString();
    }
}
