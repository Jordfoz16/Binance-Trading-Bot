package com.jordanfoster.TradingBot.TradingStrategy.EMA;

public class ResultsEMA {

    private double lastEMA = 0;
    private double currentEMA = 0;
    private double boughtPrice = 0;

    //How many intervals before the order to execute
    public int timeHoldBuy = 5;
    private int timeHoldSell = 0;
    public int timeHoldBuyCounter = 0;
    private int timeHoldSellCounter = 0;

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

    public void setBuy(){

        if(bought) return;

        if(timeHoldBuyCounter >= timeHoldBuy){
            this.buy = true;
            bought = true;
            timeHoldBuyCounter = 0;
        }else{
            this.buy = false;
            timeHoldBuyCounter++;
        }
    }

    public void setSell(){

        if(bought == false) return;

        if(timeHoldSellCounter >= timeHoldSell){
            this.sell = true;
            bought = false;
            timeHoldSellCounter = 0;
        }else{
            this.sell = false;
            timeHoldSellCounter++;
        }
    }

    public void resetTimeHoldBuy(){
        timeHoldBuyCounter = 0;
    }

    public void resetTimeHoldSell(){
        timeHoldSellCounter = 0;
    }

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

    public void setTimeHoldBuy(int value){
        timeHoldBuy = value;
    }

    public void setTimeHoldSell(int value){
        timeHoldSell = value;
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
