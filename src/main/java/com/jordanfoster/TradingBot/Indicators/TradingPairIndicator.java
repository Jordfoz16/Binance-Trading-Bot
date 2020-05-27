package com.jordanfoster.TradingBot.Indicators;

import com.jordanfoster.TradingBot.PriceFeed.CandleStick.CandleStickData;

public abstract class TradingPairIndicator {

    public enum State{
        BUY,
        SELL,
        HOLD,
    }

    protected String symbol;
    protected State state;

    public TradingPairIndicator(String symbol){
        this.symbol = symbol;
    }

    public void setState(State state){
        this.state = state;
    }

    public String getSymbol(){
        return symbol;
    }

    public State getState(){
        return state;
    }
}
