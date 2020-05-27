package com.jordanfoster.TradingBot.Indicators.RSI.Strategy;

import com.jordanfoster.TradingBot.Indicators.Indicator;
import com.jordanfoster.TradingBot.Indicators.RSI.RSI;
import com.jordanfoster.TradingBot.Indicators.RSI.TradingPair.TradingPairRSI;
import com.jordanfoster.TradingBot.Indicators.Strategy;
import com.jordanfoster.TradingBot.Indicators.TradingPairIndicator;
import com.jordanfoster.TradingBot.PriceFeed.PriceFeed;
import com.jordanfoster.TradingBot.PriceFeed.TradingPair;

public class StrategyRSI extends Strategy {

    @Override
    public void update(PriceFeed priceFeed, Indicator indicatorData, int candleIndex) {

        for(int coinIndex = 0; coinIndex < priceFeed.getTradingPairs().size(); coinIndex++){

            TradingPair currentTradingPair = priceFeed.getTradingPair(coinIndex);
            TradingPairRSI currentRSIPair = (TradingPairRSI) indicatorData.getIndicator(coinIndex);

            if (candleIndex < ((RSI) indicatorData).period) continue;

            int upperBound = ((RSI) indicatorData).upperBound;
            int lowerBound = ((RSI) indicatorData).lowerBound;

//            if(upperBound < currentRSIPair.getCandle(candleIndex).RSI){
//                state = State.BUY;
//            }else if(lowerBound > currentRSIPair.getCandle(candleIndex).RSI){
//                state = State.SELL;
//            }else{
//                state = State.HOLD;
//            }

            if(currentRSIPair.getCandle(candleIndex).RSI < lowerBound){
                currentRSIPair.setState(TradingPairIndicator.State.BUY);
                continue;
            }

            if(currentRSIPair.getCandle(candleIndex).RSI > upperBound){
                currentRSIPair.setState(TradingPairIndicator.State.SELL);
                continue;
            }

            currentRSIPair.setState(TradingPairIndicator.State.HOLD);
        }
    }
}
