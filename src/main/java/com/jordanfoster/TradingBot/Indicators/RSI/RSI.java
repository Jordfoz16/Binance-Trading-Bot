package com.jordanfoster.TradingBot.Indicators.RSI;

import com.jordanfoster.TradingBot.Indicators.Indicator;
import com.jordanfoster.TradingBot.PriceFeed.TradingPair;

public class RSI extends Indicator {

    @Override
    public void updateIndicator(TradingPair currentCoin) {

        for(int currentCandle = 0; currentCandle < currentCoin.getCandleStickData().size(); currentCandle++){

            //Add the initial value
            if(currentCandle == 0){
                long date = currentCoin.getCandleStick(currentCandle).closeTime;
                double close = currentCoin.getCandleStick(currentCandle).close;

                tradingPairIndicator.add(new TradingPairRSI(date, close));
                continue;
            }

            //Add the basic info for each candle
            long date = currentCoin.getCandleStick(currentCandle).closeTime;
            double close = currentCoin.getCandleStick(currentCandle).close;

            double change = close - currentCoin.getCandleStick(currentCandle - 1).close;
            double gain = 0;
            double loss = 0;

            if(change > 0){
                gain = Math.abs(change);
            }else if(change < 0){
                loss = Math.abs(change);
            }

            //If there isn't enough data to calculate RSI
            if(currentCandle < period && currentCandle != 0){

                tradingPairIndicator.add(new TradingPairRSI(date, close, change, gain, loss));
                continue;
            }

            //Average Gains
            double avgGain = gain;
            for(int i = 1; i < period; i++){
                avgGain += ((TradingPairRSI) tradingPairIndicator.get(currentCandle - i)).gain;
            }
            //Average Losses
            double avgLoss = loss;
            for(int i = 1; i < period; i++){
                avgLoss += ((TradingPairRSI) tradingPairIndicator.get(currentCandle - i)).loss;
            }

            //If the amount of data is more than the period
            if(currentCandle > period){
                //Places more emphasis on the recent values
                double prevAvgGain = ((TradingPairRSI) tradingPairIndicator.get(currentCandle - 1)).avgGain;
                avgGain = ((prevAvgGain * (period - 1)) + gain) / period;

                double prevAvgLoss = ((TradingPairRSI) tradingPairIndicator.get(currentCandle - 1)).avgLoss;
                avgLoss = ((prevAvgLoss * (period - 1)) + loss) / period;
            }

            //RSI calculation
            double RS = avgGain / avgLoss;
            double RSI = 100 - (100 / (1 + RS));

            tradingPairIndicator.add(new TradingPairRSI(date, close, change, gain, loss, avgGain, avgLoss, RSI));
        }
    }
}
