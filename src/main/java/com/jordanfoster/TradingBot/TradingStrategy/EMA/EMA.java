package com.jordanfoster.TradingBot.TradingStrategy.EMA;

import com.jordanfoster.TradingBot.PriceFeed.Price;

import java.util.ArrayList;

public class EMA {

    private double N = 360.0;
    private double K = 2.0 / (N + 1.0);

    //private double buyThreshold = 1.0015;
    private double buyThreshold = 1.000;
    private int tradingCooldown = 30;
    private int tradingCooldownCounter = 0;

    private int numberOfCurrency = 1;

    private ArrayList<ResultsEMA> resultsEMA = new ArrayList<ResultsEMA>();
    private ArrayList<Price> tradingPairPriceFeed = new ArrayList<Price>();

    public EMA(ArrayList<Price> prices){
        for(int i = 0; i < prices.size(); i++){
            resultsEMA.add(new ResultsEMA());
            double firstPrice = prices.get(i).getPriceList().get(0);
            resultsEMA.get(i).setLastEMA(firstPrice);
        }
    }

    public void updateEMA(ArrayList<Price> tradingPairPriceFeed, boolean startTrading){
        this.tradingPairPriceFeed = tradingPairPriceFeed;

        //Updates the current EMA for each of the currencies
        for(int i = 0; i < tradingPairPriceFeed.size(); i++){
            double currentPrice = tradingPairPriceFeed.get(i).getCurrentPrice();
            double lastEMA = resultsEMA.get(i).getLastEMA();

            double currentEMA = currentPrice * K + lastEMA * (1 - K);
            resultsEMA.get(i).setCurrentEMA(currentEMA);
        }

        if(startTrading){
            if(tradingCooldownCounter <= tradingCooldown){
                tradingCooldownCounter++;
            }else{
                updateBuy();
                updateSell();
            }
        }
    }

    public void updateBuy(){

        //Updates the buy status for each of the currencies
        for(int i = 0; i < numberOfCurrency; i++){

            double currencyPrice = tradingPairPriceFeed.get(i).getCurrentPrice();
            double emaPrice = resultsEMA.get(i).getCurrentEMA();

            if(currencyPrice > (emaPrice * buyThreshold)) {

                //if(resultsEMA.get(i).getBought()){ continue; }

                resultsEMA.get(i).setBuy();
            }else{
                resultsEMA.get(i).resetTimeHoldBuy();
            }
        }
    }

    public void updateSell(){

        //Updates the sell status for each of the currencies
        for(int i = 0; i < numberOfCurrency; i++){

            double currencyPrice = tradingPairPriceFeed.get(i).getCurrentPrice();
            double emaPrice = resultsEMA.get(i).getCurrentEMA();

            if(currencyPrice < emaPrice) {

                //if(!resultsEMA.get(i).getBought()){ continue; }

                resultsEMA.get(i).setSell();
            }else{
                resultsEMA.get(i).resetTimeHoldSell();
            }
        }
    }

    public void setBuyThreshold(double value){
        buyThreshold = value;
    }

    public void setTradingCooldown(int value){
        tradingCooldown = value;
    }

    public ArrayList<ResultsEMA> getEMA(){
        return resultsEMA;
    }
}
