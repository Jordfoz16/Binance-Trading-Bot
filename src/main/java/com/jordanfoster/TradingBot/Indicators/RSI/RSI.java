package com.jordanfoster.TradingBot.Indicators.RSI;

import com.jordanfoster.TradingBot.Indicators.Indicator;
import com.jordanfoster.TradingBot.PriceFeed.TradingPair;
import com.jordanfoster.TradingBot.TradingBot;


public class RSI extends Indicator {

    public int period = 14;

    @Override
    public void updateIndicator(TradingPair currentCoin) {
        TradingPairRSI currentTradingPairRSI = new TradingPairRSI(currentCoin.getSymbol());

        for(int currentCandle = 0; currentCandle < currentCoin.getCandleStickData().size(); currentCandle++){

            String symbol = currentCoin.getSymbol();

            //Add the initial value
            if(currentCandle == 0){
                long date = currentCoin.getCandleStick(currentCandle).closeTime;
                double close = currentCoin.getCandleStick(currentCandle).close;

                currentTradingPairRSI.addRSI(date, close);
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

                currentTradingPairRSI.addRSI(date, close, change, gain, loss);
                continue;
            }

            //Average Gains and Losses
            double avgGain = gain;
            double avgLoss = loss;
            for(int i = 1; i < period; i++){
                avgGain += currentTradingPairRSI.getCandle(currentCandle - i).gain;
                avgLoss += currentTradingPairRSI.getCandle(currentCandle - i).loss;
            }

            //If the amount of data is more than the period
            if(currentCandle > period){
                //Places more emphasis on the recent values
                double prevAvgGain = currentTradingPairRSI.getCandle(currentCandle - 1).avgGain;
                avgGain = ((prevAvgGain * (period - 1)) + gain) / period;

                double prevAvgLoss = currentTradingPairRSI.getCandle(currentCandle - 1).avgLoss;
                avgLoss = ((prevAvgLoss * (period - 1)) + loss) / period;
            }

            //RSI calculation
            double RS = avgGain / avgLoss;
            double RSI = 100 - (100 / (1 + RS));

            currentTradingPairRSI.addRSI(date, close, change, gain, loss, avgGain, avgLoss, RSI);
        }

        coinIndicators.add(currentTradingPairRSI);
    }

    public void loadValues(){
        period = Integer.parseInt(TradingBot.fileConfig.getElement("rsi", "rsi-period"));
    }
}
