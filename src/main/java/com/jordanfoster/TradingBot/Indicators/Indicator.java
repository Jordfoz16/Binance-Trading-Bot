package com.jordanfoster.TradingBot.Indicators;

public abstract class Indicator {

    public abstract void refreshValues();

    public abstract void update();
    protected abstract void updateState(int index);
}
