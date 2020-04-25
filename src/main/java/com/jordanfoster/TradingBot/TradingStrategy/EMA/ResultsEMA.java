package com.jordanfoster.TradingBot.TradingStrategy.EMA;

import java.util.ArrayList;

public class ResultsEMA {

    private double boughtPrice = 0;

    //How many intervals before the order to execute
    public int timeHoldBuy = 5;
    private int timeHoldSell = 0;
    private int timeHoldBuyCounter = 0;
    private int timeHoldSellCounter = 0;

    //Cool down period before trading starts
    private int tradingCooldown = 30;
    public int tradingCooldownCounter = 0;

    private boolean bought = false;
    private boolean buy = false;
    private boolean sell = false;

    private int listLength = 600;

    private ArrayList<Double> emaHistory = new ArrayList<Double>();

    public void setCurrentEMA(double currentEMA){

        if(emaHistory.size() > listLength){
            emaHistory.remove(0);
        }

        emaHistory.add(currentEMA);
    }

    public void setBuy(){

        if(bought) return;
        if(tradingCooldownCounter <= tradingCooldown) return;

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

        if(tradingCooldownCounter <= tradingCooldown){
            tradingCooldownCounter++;
        }

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

    public void setTradingCooldown(int value){
        tradingCooldown = value;
    }

    public double getPrevEMA(){

        if(emaHistory.size() < 2){
            return emaHistory.get(emaHistory.size() - 1);
        }
        return emaHistory.get(emaHistory.size() - 1);
    }

    public double getCurrentEMA(){
        return emaHistory.get(emaHistory.size() - 1);
    }

    public ArrayList<Double> getEMAHistory() { return emaHistory; }

    public double getBoughtPrice() { return boughtPrice; }

    public boolean getBuy(){ return buy; }

    public boolean getSell(){ return sell; }

    public boolean getBought(){ return bought; }

    public void resetTimeHoldBuy(){
        timeHoldBuyCounter = 0;
    }

    public void resetTimeHoldSell(){
        timeHoldSellCounter = 0;
    }
}
