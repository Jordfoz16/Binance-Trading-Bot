package com.jordanfoster.TradingBot.TradingStrategy.EMA;

public class ResultsEMA {

    private double lastEMA = 0;
    private double currentEMA = 0;
    private double boughtPrice = 0;

    private boolean bought = false;
    private boolean buy = false;
    private boolean sell = false;

    public void setCurrentEMA(double currentEMA){
        if(this.currentEMA != 0){
            this.lastEMA = this.currentEMA;
        }
        this.currentEMA = currentEMA;
    }

    public void setLastEMA(double lastEMA){
        this.lastEMA = lastEMA;
    }

    public void setBuy(boolean buy){ this.buy = buy; }

    public void setSell(boolean sell){ this.sell = sell; }

    public void setBought(boolean bought) {
        setBought(bought, 0);
    }

    public void setBought(boolean bought, double boughtPrice) {
        this.bought = bought;
        if(boughtPrice != 0){
            this.boughtPrice = boughtPrice;
        }
        sell = false;
        buy = false;
    }

    public double getLastEMA(){
        return lastEMA;
    }

    public double getCurrentEMA(){
        return currentEMA;
    }

    public double getBoughtPrice() { return boughtPrice; }

    public boolean getBuy(){ return buy; }

    public boolean getSell(){ return sell; }

    public boolean getBought(){ return bought; }
}
