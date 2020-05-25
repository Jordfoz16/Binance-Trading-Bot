package com.jordanfoster.TradingBot.Indicators.EMA;

import com.jordanfoster.TradingBot.Indicators.Indicator;
import com.jordanfoster.TradingBot.Indicators.TradingPairIndicator;
import com.jordanfoster.TradingBot.PriceFeed.TradingPair;

import java.util.ArrayList;

public class EMA extends Indicator {

    public static int periodSmall = 9;
    public static int periodMed = 21;
    public static int periodLarge = 200;

    private double kSmall = 2.0 / (periodSmall + 1.0);
    private double kMed = 2.0 / (periodMed + 1.0);
    private double kLarge = 2.0 / (periodLarge + 1.0);

    @Override
    public void updateIndicator(TradingPair currentCoin) {
        TradingPairEMA currentTradingPairEMA = new TradingPairEMA(currentCoin.getSymbol());

        for(int currentCandle = 0; currentCandle < currentCoin.getCandleStickData().size(); currentCandle++){

            double close = currentCoin.getCandleStick(currentCandle).close;

            //Use simple moving average when under the period
            if(currentCandle < periodLarge){
                double periodSum = 0;

                for(int i = 0; i <= currentCandle; i++){
                    periodSum += currentCoin.getCandleStick(i).close;

                }

                double SMA = periodSum / (currentCandle + 1);

                if(currentCandle < periodSmall){
                    currentTradingPairEMA.addEMASmall(close, SMA);
                }

                if(currentCandle < periodMed){
                    currentTradingPairEMA.addEMAMed(close, SMA);
                }

                currentTradingPairEMA.addEMALarge(close, SMA);
            }

            if(currentCandle < periodSmall) continue;

            double emaLast;

            if(currentCandle >= periodSmall){
                emaLast = currentTradingPairEMA.getCandleSmall(currentCandle - 1).EMA;
                double EMA = close * kSmall + emaLast * (1.0 - kSmall);

                currentTradingPairEMA.addEMASmall(close, EMA);
            }

            if(currentCandle >= periodMed){
                emaLast = currentTradingPairEMA.getCandleMed(currentCandle - 1).EMA;
                double EMA = close * kMed + emaLast * (1.0 - kMed);

                currentTradingPairEMA.addEMAMed(close, EMA);
            }

            if(currentCandle >= periodLarge){
                emaLast = currentTradingPairEMA.getCandleLarge(currentCandle - 1).EMA;
                double EMA = close * kLarge + emaLast * (1.0 - kLarge);

                currentTradingPairEMA.addEMALarge(close, EMA);
            }
        }

//        for(int i = 0; i < currentTradingPairEMA.emaSmall.size(); i++){
//            System.out.println("Price, " + currentTradingPairEMA.emaSmall.get(i).close + ", EMA9, " + currentTradingPairEMA.emaSmall.get(i).EMA +
//                    ", EMA21, " + currentTradingPairEMA.emaMed.get(i).EMA +
//                    ", EMA200, " + currentTradingPairEMA.emaLarge.get(i).EMA);
//        }
        coinIndicators.add(currentTradingPairEMA);
    }
}
