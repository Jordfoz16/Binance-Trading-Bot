package com.jordanfoster.TradingBot.Indicators;

import com.jordanfoster.TradingBot.Indicators.RSI.TradingPair.TradingPairRSI;
import com.jordanfoster.TradingBot.PriceFeed.PriceFeed;
import com.jordanfoster.TradingBot.PriceFeed.TradingPair;

import java.util.ArrayList;

public abstract class Indicator {

    public boolean isEnabled = true;

    protected ArrayList<TradingPairIndicator> coinIndicators = new ArrayList<>();

    public void update(PriceFeed priceFeed, boolean loadValues){
        if(!isEnabled) return;
        if(loadValues){
            loadValues();
        }
        coinIndicators.clear();

        //Cycle through each coin
        for(int coinIndex = 0; coinIndex < priceFeed.getTradingPairs().size(); coinIndex++){
            TradingPair currentCoin = priceFeed.getTradingPair(coinIndex);
            updateIndicator(currentCoin);
        }
    }

    public abstract void loadValues();

    public abstract void updateIndicator(TradingPair currentCoin);

    public ArrayList<TradingPairIndicator> getIndicator(){
        return coinIndicators;
    }

    public TradingPairIndicator getIndicator(int index){ return coinIndicators.get(index); }
}
