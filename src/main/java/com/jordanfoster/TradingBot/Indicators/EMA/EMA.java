package com.jordanfoster.TradingBot.Indicators.EMA;

import com.jordanfoster.TradingBot.PriceFeed.LivePriceFeed;
import com.jordanfoster.TradingBot.PriceFeed.TradingPair;
import com.jordanfoster.TradingBot.TradingBot;

import java.util.ArrayList;

public class EMA {

    private int n = 10;

    private int buyWaitTime = 0;
    private int sellWaitTime = 0;
    private int calibrationTime = 10;
    private int calirationCounter = 0;

    private boolean initialized = false;

    private ArrayList<DataEMA> dataEmas = new ArrayList<DataEMA>();

    public EMA(){
        refreshValues();
    }

    public void refreshValues(){
        int currentN = n;

        n = Integer.parseInt(TradingBot.fileConfig.getElement("ema", "n-value"));
        buyWaitTime = Integer.parseInt(TradingBot.fileConfig.getElement("ema", "buy-wait"));
        sellWaitTime = Integer.parseInt(TradingBot.fileConfig.getElement("ema", "sell-wait"));
        calibrationTime = Integer.parseInt(TradingBot.fileConfig.getElement("ema", "calibration-time"));

        if(n != currentN){
            nChanged();
        }
    }

    public void update(){
        refreshValues();

        //Update the EMA value for each currency
        for (int index = 0; index < TradingBot.livePriceFeed.getTradingPairs().size(); index++) {
            calculateEMA(index, null);
            updateState(index);
        }

        initialized = true;
    }

    private void updateState(int index){

        DataEMA currentCoin = dataEmas.get(index);

        //Checks to make sure calibration time is completed
        if(calibrationTime <= calirationCounter){

            if(TradingBot.livePriceFeed.getTradingPair(index).getCurrentPrice() > currentCoin.getCurrent()){
                //Doesn't buy unless its above the waiting time
                if(buyWaitTime <= currentCoin.buyWaitCounter){
                    currentCoin.setState(TradingBot.State.BUY);
                }else{
                    currentCoin.setState(TradingBot.State.HOLD);
                    currentCoin.buyWaitCounter++;
                }
                //Resets the counter for selling
                currentCoin.sellWaitCounter = 0;
            }else{
                if(sellWaitTime <= currentCoin.sellWaitCounter){
                    currentCoin.setState(TradingBot.State.SELL);
                }else{
                    currentCoin.setState(TradingBot.State.HOLD);
                    currentCoin.sellWaitCounter++;
                }

                //Resets the counter for buying
                currentCoin.buyWaitCounter = 0;
            }

        }else{
            currentCoin.setState(TradingBot.State.CALIBRATION);
            calirationCounter++;
        }
    }

    private void calculateEMA(int index, TradingPair tradingPair){

        TradingPair currentPair = TradingBot.livePriceFeed.getTradingPair(index);

        if(tradingPair != null){
            currentPair = tradingPair;
        }

        double currentPrice = currentPair.getCurrentPrice();

        if (initialized){
            //k is the weighted multiplier
            double k = 2.0 / ((double) n + 1.0);

            double prevEMA = dataEmas.get(index).getPrevEMA();

            double ema = currentPrice * k + prevEMA * (1.0 - k);

            dataEmas.get(index).addValue(ema);

        }else {
            //Setting up the array list on the first loop
            dataEmas.add(new DataEMA(currentPrice));
        }
    }

    public void nChanged(){

        LivePriceFeed tempLivePriceFeed = new LivePriceFeed();

        for(int index = 0; index < TradingBot.livePriceFeed.getTradingPairs().size(); index++){
            DataEMA currentEMA = dataEmas.get(index);

            currentEMA.getValues().clear();
            currentEMA.addValue(TradingBot.livePriceFeed.getTradingPair(index).get(0));


            tempLivePriceFeed.getTradingPairs().add(new TradingPair(TradingBot.livePriceFeed.getTradingPair(index).getSymbol(), TradingBot.livePriceFeed.getTradingPair(index).get(0)));

            for(int i = 0; i < TradingBot.livePriceFeed.getTradingPair(index).getPriceList().size(); i++){
                tempLivePriceFeed.getTradingPair(index).addPrice(TradingBot.livePriceFeed.getTradingPair(index).get(i));
                calculateEMA(index, tempLivePriceFeed.getTradingPair(index));
            }
        }

    }


    public void clear(){
        dataEmas.clear();
        calirationCounter = 0;
        initialized = false;
    }

    public ArrayList<DataEMA> getDataEmas(){
        return dataEmas;
    }
}
