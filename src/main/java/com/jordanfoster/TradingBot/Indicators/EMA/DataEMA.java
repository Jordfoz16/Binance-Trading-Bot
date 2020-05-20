package com.jordanfoster.TradingBot.Indicators.EMA;

import com.jordanfoster.TradingBot.Indicators.Data;
import com.jordanfoster.TradingBot.TradingBot;

import java.util.ArrayList;

public class DataEMA extends Data {

    public int buyWaitCounter = 0;
    public int sellWaitCounter = 0;

    public DataEMA(double value){
        addValue(value);
    }

    public Double getPrevEMA(){
        if(data.size() > 2){
            return data.get(data.size() - 2);
        }
        return data.get(data.size() - 1);
    }
}
