package com.jordanfoster.TradingBot.Indicators.EMA;

import com.jordanfoster.TradingBot.Indicators.EMA.TradingPair.TradingPairEMA;
import com.jordanfoster.TradingBot.Indicators.Indicator;
import com.jordanfoster.TradingBot.PriceFeed.TradingPair;
import com.jordanfoster.TradingBot.TradingBot;

public class EMA extends Indicator {

    public static int periodShort = 9;
    public static int periodMedium = 21;
    public static int periodLong = 200;

    @Override
    public void updateIndicator(TradingPair currentCoin) {
        TradingPairEMA currentTradingPairEMA = new TradingPairEMA(currentCoin.getSymbol());

        double kSmall = 2.0 / (periodShort + 1.0);
        double kMed = 2.0 / (periodMedium + 1.0);
        double kLarge = 2.0 / (periodLong + 1.0);

        for(int currentCandle = 0; currentCandle < currentCoin.getCandleStickData().size(); currentCandle++){

            double close = currentCoin.getCandleStick(currentCandle).close;

            //Use simple moving average when under the period
            if(currentCandle < periodLong){
                double periodSum = 0;

                for(int i = 0; i <= currentCandle; i++){
                    periodSum += currentCoin.getCandleStick(i).close;

                }

                double SMA = periodSum / (currentCandle + 1);

                if(currentCandle < periodShort){
                    currentTradingPairEMA.addEMASmall(close, SMA);
                }

                if(currentCandle < periodMedium){
                    currentTradingPairEMA.addEMAMed(close, SMA);
                }

                currentTradingPairEMA.addEMALarge(close, SMA);
            }

            if(currentCandle < periodShort) continue;

            double emaLast;

            if(currentCandle >= periodShort){
                emaLast = currentTradingPairEMA.getCandleSmall(currentCandle - 1).EMA;
                double EMA = close * kSmall + emaLast * (1.0 - kSmall);

                currentTradingPairEMA.addEMASmall(close, EMA);
            }

            if(currentCandle >= periodMedium){
                emaLast = currentTradingPairEMA.getCandleMed(currentCandle - 1).EMA;
                double EMA = close * kMed + emaLast * (1.0 - kMed);

                currentTradingPairEMA.addEMAMed(close, EMA);
            }

            if(currentCandle >= periodLong){
                emaLast = currentTradingPairEMA.getCandleLarge(currentCandle - 1).EMA;
                double EMA = close * kLarge + emaLast * (1.0 - kLarge);

                currentTradingPairEMA.addEMALarge(close, EMA);
            }
        }
        coinIndicators.add(currentTradingPairEMA);
    }

    public void loadValues(){
        periodShort = Integer.parseInt(TradingBot.fileConfig.getElement("ema", "short-period-value"));
        periodMedium = Integer.parseInt(TradingBot.fileConfig.getElement("ema", "medium-period-value"));
        periodLong = Integer.parseInt(TradingBot.fileConfig.getElement("ema", "long-period-value"));
    }
}
