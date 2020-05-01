package com.jordanfoster.TradingBot.Indicators.EMA;

import com.jordanfoster.TradingBot.TradingBot;

import java.util.ArrayList;

public class EMA {

    private int n = 10;
    private int buyWaitTime = 0;
    private int sellWaitTime = 0;
    private int calibrationTime = 10;

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

        for (int index = 0; index < TradingBot.priceFeed.getTradingPairs().size(); index++){

            double currentPrice = TradingBot.priceFeed.getTradingPair(index).getCurrentPrice();

            if (initialized){

                double prevEMA = emaValues.get(index).getPrev();
                double ema = calculateEMA(currentPrice, prevEMA);

                emaValues.get(index).addValue(ema);
            }else{
                //Setting up the array list on the first loop
                emaValues.add(new EMAValue(currentPrice));
            }
        }

        initialized = true;
    }

    private double calculateEMA(double currentPrice, double previousEMA){
        //k is the weighted multiplier
        double k = 2.0 / ((double) n + 1.0);

        double ema = currentPrice * k + previousEMA * (1.0 - k);

        return ema;
    }

    public ArrayList<EMAValue> getEmaValues(){
        return emaValues;
    }
}
