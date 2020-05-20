package com.jordanfoster.TradingBot.Indicators;

import com.jordanfoster.TradingBot.Indicators.EMA.DataEMA;
import com.jordanfoster.TradingBot.PriceFeed.TradingPair;
import com.jordanfoster.TradingBot.TradingBot;

import java.util.ArrayList;

public abstract class Indicator {

    protected int calibrationCounter = 0;
    protected boolean initialized = false;

    protected ArrayList<Data> dataArrayList = new ArrayList<Data>();

    public abstract void refreshValues();

    public void update(){
        refreshValues();

        for(int index = 0; index < TradingBot.livePriceFeed.getTradingPairs().size(); index++){
            calculate(index, null);
            updateState(index);
        }

        initialized = true;
    }

    protected abstract void updateState(int index);

    protected abstract void calculate(int index, TradingPair tradingPair);

    public void clear(){
        dataArrayList.clear();
        calibrationCounter = 0;
        initialized = false;
    }

    public ArrayList<Data> getData(){
        return dataArrayList;
    }
}
