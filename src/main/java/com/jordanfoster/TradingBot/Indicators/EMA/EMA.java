package com.jordanfoster.TradingBot.Indicators.EMA;

import com.jordanfoster.TradingBot.TradingBot;

import java.util.ArrayList;

public class EMA {

    private int n = 10;

    private int buyWaitTime = 0;
    private int sellWaitTime = 0;
    private int calibrationTime = 10;
    private int calirationCounter = 0;

    private boolean initialized = false;

    private ArrayList<EMAValue> emaValues = new ArrayList<EMAValue>();

    public EMA(){
        refreshValues();
    }

    public void refreshValues(){
        n = Integer.parseInt(TradingBot.fileConfig.getElement("ema", "n-value"));
        buyWaitTime = Integer.parseInt(TradingBot.fileConfig.getElement("ema", "buy-wait"));
        sellWaitTime = Integer.parseInt(TradingBot.fileConfig.getElement("ema", "sell-wait"));
        calibrationTime = Integer.parseInt(TradingBot.fileConfig.getElement("ema", "calibration-time"));
    }

    public void update(){
        refreshValues();

        //Update the EMA value for each currency
        for (int index = 0; index < TradingBot.priceFeed.getTradingPairs().size(); index++) {
            calculateEMA(index);
            updateState(index);
        }

        initialized = true;
    }

    private void updateState(int index){

        EMAValue currentCoin = emaValues.get(index);

        //Checks to make sure calibration time is completed
        if(calibrationTime <= calirationCounter){

            if(TradingBot.priceFeed.getTradingPair(index).getCurrentPrice() > currentCoin.getCurrent()){
                //Doesn't buy unless its above the waiting time
                if(buyWaitTime <= currentCoin.buyWaitCounter){
                    currentCoin.setState(EMAValue.State.BUY);
                }else{
                    currentCoin.setState(EMAValue.State.HOLD);
                    currentCoin.buyWaitCounter++;
                }
                //Resets the counter for selling
                currentCoin.sellWaitCounter = 0;
            }else{
                if(sellWaitTime <= currentCoin.sellWaitCounter){
                    currentCoin.setState(EMAValue.State.SELL);
                }else{
                    currentCoin.setState(EMAValue.State.HOLD);
                    currentCoin.sellWaitCounter++;
                }

                //Resets the counter for buying
                currentCoin.buyWaitCounter = 0;
            }

        }else{
            currentCoin.setState(EMAValue.State.CALIBRATION);
            calirationCounter++;
        }
    }

    private void calculateEMA(int index){
        double currentPrice = TradingBot.priceFeed.getTradingPair(index).getCurrentPrice();

        if (initialized){
            //k is the weighted multiplier
            double k = 2.0 / ((double) n + 1.0);

            double prevEMA = emaValues.get(index).getPrev();

            double ema = currentPrice * k + prevEMA * (1.0 - k);

            emaValues.get(index).addValue(ema);

        }else {
            //Setting up the array list on the first loop
            emaValues.add(new EMAValue(currentPrice));
        }
    }

//    private double calculateEMA(double currentPrice, double previousEMA){
//        //k is the weighted multiplier
//        double k = 2.0 / ((double) n + 1.0);
//
//        double ema = currentPrice * k + previousEMA * (1.0 - k);
//
//        return ema;
//    }

    public ArrayList<EMAValue> getEmaValues(){
        return emaValues;
    }
}
