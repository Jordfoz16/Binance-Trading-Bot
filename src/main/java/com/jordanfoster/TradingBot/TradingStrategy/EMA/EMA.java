package com.jordanfoster.TradingBot.TradingStrategy.EMA;

import com.jordanfoster.TradingBot.PriceFeed.Price;

import java.util.ArrayList;

public class EMA {

    private double N = 360.0;
    private double K = 2.0 / (N + 1.0);

    //private double buyThreshold = 1.0015;
    private double buyThreshold = 1.000;

    private int numberOfCurrency = 1;

    private ArrayList<ResultsEMA> resultsEMA = new ArrayList<ResultsEMA>();
    private ArrayList<Price> tradingPairPriceFeed = new ArrayList<Price>();

    public void updateEMA(ArrayList<Price> tradingPairPriceFeed, boolean startTrading){
        this.tradingPairPriceFeed = tradingPairPriceFeed;

        if(resultsEMA.size() == 0){
            for(int i = 0; i < tradingPairPriceFeed.size(); i++) {
                resultsEMA.add(new ResultsEMA());
                double firstPrice = tradingPairPriceFeed.get(i).getPriceHistory().get(0);
                resultsEMA.get(i).setCurrentEMA(firstPrice);
            }
        }

        //Updates the current EMA for each of the currencies
        for(int i = 0; i < numberOfCurrency; i++){
            double currentPrice = tradingPairPriceFeed.get(i).getCurrentPrice();
            double lastEMA = resultsEMA.get(i).getPrevEMA();

            double currentEMA = currentPrice * K + lastEMA * (1 - K);
            resultsEMA.get(i).setCurrentEMA(currentEMA);
        }

        System.out.println("Bitcoin Cooldown: " + resultsEMA.get(0).tradingCooldownCounter);

        if(startTrading){
            updateBuy();
            updateSell();
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

    public void setN(double value){
        N = value;
        K = 2.0 / (N + 1.0);
    }

    public ArrayList<ResultsEMA> getEMA(){
        return resultsEMA;
    }
}
