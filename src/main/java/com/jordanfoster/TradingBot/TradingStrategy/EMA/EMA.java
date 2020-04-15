package com.jordanfoster.TradingBot.TradingStrategy.EMA;

import com.jordanfoster.TradingBot.PriceFeed.Price;

import java.util.ArrayList;

public class EMA {

    private double N = 360;
    private double K = 2 / (N + 1);

    private ArrayList<ValueEMA> valueEMA = new ArrayList<ValueEMA>();

    public EMA(ArrayList<Price> prices){
        for(int i = 0; i < prices.size(); i++){
            valueEMA.add(new ValueEMA());
            double firstPrice = prices.get(i).getPriceList().get(0);
            valueEMA.get(i).setLastEMA(firstPrice);
        }
    }

    public void updateEMA(ArrayList<Price> prices){
        for(int i = 0; i < prices.size(); i++){
            double currentPrice = prices.get(i).getLastPrice();
            double lastEMA = valueEMA.get(i).getLastEMA();

            double EMA = currentPrice * K + lastEMA * (1 - K);
            valueEMA.get(i).setCurrentEMA(EMA);
        }
    }

    public ArrayList<ValueEMA> getEMA(){
        return valueEMA;
    }
}
