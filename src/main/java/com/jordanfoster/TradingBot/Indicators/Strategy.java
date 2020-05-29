package com.jordanfoster.TradingBot.Indicators;

import com.jordanfoster.TradingBot.PriceFeed.PriceFeed;

import java.util.HashMap;

public abstract class Strategy {

    public enum State{
        BUY,
        SELL,
        HOLD
    }

    private HashMap<String, State> tradingPairStates = new HashMap<>();

    public abstract void update(PriceFeed priceFeed, Indicator indicatorData, int candleIndex);

    public void setState(String symbol, State state){
        tradingPairStates.put(symbol, state);
    }

    public State getState(String symbol){
        return tradingPairStates.get(symbol);
    }
}
