package com.jordanfoster.TradingBot.Indicators;

import com.jordanfoster.TradingBot.PriceFeed.CandleStick.CandleStickData;

public abstract class TradingPairIndicator {

    protected String symbol;

    public TradingPairIndicator(String symbol){
        this.symbol = symbol;
    }

    public String getSymbol(){
        return symbol;
    }
}
