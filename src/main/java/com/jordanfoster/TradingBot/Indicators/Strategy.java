package com.jordanfoster.TradingBot.Indicators;

import com.jordanfoster.TradingBot.PriceFeed.PriceFeed;

import java.util.ArrayList;

public abstract class Strategy {

    public enum State{
        BUY,
        SELL,
        HOLD
    }

    public abstract void update(PriceFeed priceFeed, Indicator indicatorData, int candleIndex);
}
