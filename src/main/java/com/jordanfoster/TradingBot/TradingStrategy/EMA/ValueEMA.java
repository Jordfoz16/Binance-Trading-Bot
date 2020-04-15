package com.jordanfoster.TradingBot.TradingStrategy.EMA;

public class ValueEMA {

    private double lastEMA = 0;
    private double currentEMA = 0;

    public void setCurrentEMA(double currentEMA){
        if(this.currentEMA != 0){
            this.lastEMA = this.currentEMA;
        }
        this.currentEMA = currentEMA;
    }

    public void setLastEMA(double lastEMA){
        this.lastEMA = lastEMA;
    }

    public double getLastEMA(){
        return lastEMA;
    }

    public double getCurrentEMA(){
        return currentEMA;
    }
}
