package com.jordanfoster.TradingBot.Indicators.RSI;

import com.jordanfoster.TradingBot.PriceFeed.TradingPair;
import com.jordanfoster.TradingBot.TradingBot;

import java.util.ArrayList;

public class RSI {

    public static int rsiPeriod = 10;

    private ArrayList<RSIValue> rsiValues = new ArrayList<RSIValue>();

    private boolean initialized = false;

    public void update(){

        for(int index = 0; index < TradingBot.priceFeed.getTradingPairs().size(); index++){

            TradingPair currentPair = TradingBot.priceFeed.getTradingPair(index);

            if(initialized){

                RSIValue currentRSI = rsiValues.get(index);

                //Check if there is enough data to calculate RSI
                if(currentPair.getPriceList().size() >= rsiPeriod){
                    for(int i = rsiPeriod; i >= 0; i--){
                        
                    }
                }
            }else{
                rsiValues.add(new RSIValue());
            }

            initialized = true;
        }
    }

    private void calculateRSI(){

    }
}
