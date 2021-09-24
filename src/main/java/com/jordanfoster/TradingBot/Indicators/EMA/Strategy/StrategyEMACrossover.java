package com.jordanfoster.TradingBot.Indicators.EMA.Strategy;

import com.jordanfoster.TradingBot.Indicators.EMA.EMA;
import com.jordanfoster.TradingBot.Indicators.EMA.TradingPair.TradingPairEMA;
import com.jordanfoster.TradingBot.Indicators.Indicator;
import com.jordanfoster.TradingBot.Indicators.Strategy;
import com.jordanfoster.TradingBot.Indicators.TradingPairIndicator;
import com.jordanfoster.TradingBot.PriceFeed.PriceFeed;
import com.jordanfoster.TradingBot.PriceFeed.TradingPair;

import java.util.HashMap;

public class StrategyEMACrossover extends Strategy {

    @Override
    public void update(PriceFeed priceFeed, Indicator indicatorData, int candleIndex) {

        for(int coinIndex = 0; coinIndex < priceFeed.getTradingPairs().size(); coinIndex++){

            TradingPair currentTradingPair = priceFeed.getTradingPair(coinIndex);
            TradingPairEMA currentEMAPair = (TradingPairEMA) indicatorData.getIndicator(coinIndex);

            double shortEMA = currentEMAPair.getCandleSmall(candleIndex).EMA;
            double mediumEMA = currentEMAPair.getCandleMed(candleIndex).EMA;
            double longEMA = currentEMAPair.getCandleLarge(candleIndex).EMA;

            //Both EMA's have to be above the trend EMA
            if(shortEMA > longEMA && mediumEMA > longEMA){

                if(shortEMA > mediumEMA){
                    setState(currentTradingPair.getSymbol(), State.BUY);
                }else if(shortEMA < mediumEMA){
                    setState(currentTradingPair.getSymbol(), State.SELL);
                }else{
                    setState(currentTradingPair.getSymbol(), State.HOLD);
                }

            }else{
                setState(currentTradingPair.getSymbol(), State.SELL);
            }
        }
    }
}
