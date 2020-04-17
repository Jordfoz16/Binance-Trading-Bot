package com.jordanfoster.TradingBot.TradingStrategy.EMA;

import com.jordanfoster.TradingBot.BoughtCurrency;
import com.jordanfoster.TradingBot.PriceFeed.Price;

import java.util.ArrayList;

public class EMA {

    private double N = 360;
    private double K = 2 / (N + 1);

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

    public void updateEMA(ArrayList<Price> tradingPairPriceFeed){
        this.tradingPairPriceFeed = tradingPairPriceFeed;

        for(int i = 0; i < tradingPairPriceFeed.size(); i++){
            double currentPrice = tradingPairPriceFeed.get(i).getLastPrice();
            double lastEMA = resultsEMA.get(i).getLastEMA();

            double currentEMA = currentPrice * K + lastEMA * (1 - K);
            resultsEMA.get(i).setCurrentEMA(currentEMA);
        }

        updateBuy();
        updateSell();
    }

    public void updateBuy(){
        for(int i = 0; i < numberOfCurrency; i++){

            double currencyPrice = tradingPairPriceFeed.get(i).getLastPrice();
            double emaPrice = resultsEMA.get(i).getCurrentEMA();

            if(currencyPrice > emaPrice) {

                if(resultsEMA.get(i).getBought()){ return; }

                resultsEMA.get(i).setBuy(true);
            }
        }
    }

    public void updateSell(){
        for(int i = 0; i < numberOfCurrency; i++){

            double currencyPrice = tradingPairPriceFeed.get(i).getLastPrice();
            double emaPrice = resultsEMA.get(i).getCurrentEMA();

            if(currencyPrice < emaPrice) {

                if(!resultsEMA.get(i).getBought()){ return; }

                resultsEMA.get(i).setSell(true);
            }
        }
    }

    public ArrayList<ResultsEMA> getEMA(){
        return resultsEMA;
    }
}
