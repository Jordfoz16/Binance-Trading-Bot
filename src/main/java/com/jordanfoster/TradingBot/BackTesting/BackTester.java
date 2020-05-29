package com.jordanfoster.TradingBot.BackTesting;

import com.jordanfoster.Networking.BinanceAPI;
import com.jordanfoster.TradingBot.Indicators.EMA.EMA;
import com.jordanfoster.TradingBot.Indicators.EMA.Strategy.StrategyEMACrossover;
import com.jordanfoster.TradingBot.Indicators.RSI.RSI;
import com.jordanfoster.TradingBot.Indicators.RSI.Strategy.StrategyRSI;
import com.jordanfoster.TradingBot.Indicators.Strategy;
import com.jordanfoster.TradingBot.Indicators.TradingPairIndicator;
import com.jordanfoster.TradingBot.Orderbook.OrderBook;
import com.jordanfoster.TradingBot.PriceFeed.CandleStick.CandleStickFeed;
import com.jordanfoster.TradingBot.TradingBot;

import java.io.IOException;

public class BackTester {

    /*
    BackTester - loops through historical data to test the
    trading strategies. It then produces information on how the
    trading strategy performed.
     */

    private CandleStickFeed candleStickFeed = new CandleStickFeed();
    private OrderBook orderBook = new OrderBook();
    private RSI rsi = new RSI();
    private EMA ema = new EMA();

    public int numberOfTrades = 0;
    public int numberOfProfitable = 0;
    public int numberOfUnprofitable = 0;
    public int rsiIndication = 0;
    public int emaIndication = 0;
    public double largestProfit = 0;
    public double largestLoss = 0;
    public double profit = 0;

    public void setRSIValues(int period, int upperBound, int lowerBound){
        rsi.setValues(period, upperBound, lowerBound);
    }

    public void setEMAValue(int periodShort, int periodMedium, int periodLong){
        ema.setValues(periodShort, periodMedium, periodLong);
    }

    public void setInterval(String interval){
        candleStickFeed.setInterval(interval);
    }

    public void run(){

        //Reset values and order book before new backtest
        numberOfTrades = 0;
        numberOfProfitable = 0;
        numberOfUnprofitable = 0;
        rsiIndication = 0;
        emaIndication = 0;
        largestProfit = 0;
        largestLoss = 0;
        profit = 0;
        orderBook.clear();

        //Updates price feed and RSI indicator
        candleStickFeed.update(false);
        rsi.update(candleStickFeed, false);
        ema.update(candleStickFeed, false);

        StrategyRSI strategyRSI = new StrategyRSI();
        StrategyEMACrossover strategyEMACrossover = new StrategyEMACrossover();

        //Loops through each candle stick
        for(int candleIndex = 0; candleIndex < candleStickFeed.limit; candleIndex++){
            //Updates teh strategy for current candle stick
            strategyRSI.update(candleStickFeed, rsi, candleIndex);
            strategyEMACrossover.update(candleStickFeed, ema, candleIndex);

            //Loops through each coin
            for(int coinIndex = 0; coinIndex < candleStickFeed.getTradingPairs().size(); coinIndex++){

                String symbol = candleStickFeed.getTradingPair(coinIndex).getSymbol();
                double price = candleStickFeed.getTradingPair(coinIndex).getCandleStick(candleIndex).close;

                //Checks if the coin has already been bought
                if(!orderBook.isBought(candleStickFeed.getTradingPair(coinIndex).getSymbol())){

                    if(strategyEMACrossover.getState(symbol) == Strategy.State.BUY){
                        emaIndication++;
                        if(strategyRSI.getState(symbol) == Strategy.State.BUY){
                            rsiIndication++;
                            orderBook.buyOrder(symbol, price, 1);
                        }
                    }

                }else{

                    if(strategyEMACrossover.getState(symbol) == Strategy.State.SELL){
                        if(strategyRSI.getState(symbol) == Strategy.State.SELL){
                            orderBook.sellOrder(symbol, price);
                        }
                    }
                }
            }
        }

        //Loops over the closed orders to get data for the performance
        for(int i = 0; i < orderBook.getClosedOrder().size(); i++){
            profit += orderBook.getClosedOrder(i).getProfit();
            numberOfTrades++;

            if(orderBook.getClosedOrder(i).getProfit() > 0){
                numberOfProfitable++;
            }else if(orderBook.getClosedOrder(i).getProfit() < 0){
                numberOfUnprofitable++;
            }

            if(orderBook.getClosedOrder(i).getProfit() > largestProfit){
                largestProfit = orderBook.getClosedOrder(i).getProfit();
            }else if(orderBook.getClosedOrder(i).getProfit() < largestLoss){
                largestLoss = orderBook.getClosedOrder(i).getProfit();
            }
        }
    }

}
