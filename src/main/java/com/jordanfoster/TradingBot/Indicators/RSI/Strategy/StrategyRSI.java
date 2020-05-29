package com.jordanfoster.TradingBot.Indicators.RSI.Strategy;

import com.jordanfoster.TradingBot.Indicators.Indicator;
import com.jordanfoster.TradingBot.Indicators.RSI.RSI;
import com.jordanfoster.TradingBot.Indicators.RSI.TradingPair.TradingPairRSI;
import com.jordanfoster.TradingBot.Indicators.Strategy;
import com.jordanfoster.TradingBot.Indicators.TradingPairIndicator;
import com.jordanfoster.TradingBot.PriceFeed.PriceFeed;
import com.jordanfoster.TradingBot.PriceFeed.TradingPair;

import static com.jordanfoster.TradingBot.Indicators.Strategy.State.BUY;

public class StrategyRSI extends Strategy {

    /*
    StrategyRSI - decides whether a trading pair should be bought,
    sold or held. It loops through each trading pair at a certain
    candle to provide a state of BUY, SELL, HOLD.

    If the RSI for a trading pair is below the lower bound it is considered
    over sold, this will result in the strategy indicating a buy. If the RSI
    for a trading pair is above the upper bound it is considered over bought
    which will result in the strategy indicating a sell.
     */

    @Override
    public void update(PriceFeed priceFeed, Indicator indicatorData, int candleIndex) {

        for(int coinIndex = 0; coinIndex < priceFeed.getTradingPairs().size(); coinIndex++){

            TradingPair currentTradingPair = priceFeed.getTradingPair(coinIndex);
            TradingPairRSI currentRSIPair = (TradingPairRSI) indicatorData.getIndicator(coinIndex);

            if (candleIndex < ((RSI) indicatorData).period) continue;

            int upperBound = ((RSI) indicatorData).upperBound;
            int lowerBound = ((RSI) indicatorData).lowerBound;

            if(currentRSIPair.getCandle(candleIndex).RSI < lowerBound){
                setState(currentTradingPair.getSymbol(), State.BUY);
                continue;
            }

            if(currentRSIPair.getCandle(candleIndex).RSI > upperBound){
                setState(currentTradingPair.getSymbol(), State.SELL);
                continue;
            }

            //setState(currentTradingPair.getSymbol(), State.HOLD);
        }
    }
}
