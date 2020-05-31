package com.jordanfoster.TradingBot.BackTesting;

import com.jordanfoster.TradingBot.Indicators.EMA.EMA;
import com.jordanfoster.TradingBot.Indicators.EMA.Strategy.StrategyEMACrossover;
import com.jordanfoster.TradingBot.Indicators.RSI.RSI;
import com.jordanfoster.TradingBot.Indicators.RSI.Strategy.StrategyRSI;
import com.jordanfoster.TradingBot.Indicators.Strategy;
import com.jordanfoster.TradingBot.Orderbook.OrderBook;
import com.jordanfoster.TradingBot.PriceFeed.CandleStick.CandleStickFeed;

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

    public double startValue = 1000;
    public double accountValue = startValue;
    public long startTime = 0;
    public long endTime = 0;
    public int dataPoints = 0;
    public int numberOfTrades = 0;
    public int numberOfProfitable = 0;
    public int numberOfUnprofitable = 0;
    public int rsiIndication = 0;
    public int emaIndication = 0;
    public double largestProfit = 0;
    public double largestLoss = 0;
    public double profit = 0;

    public void setStartValue(int startValue){
        this.startValue = startValue;
    }

    public void setRSIValues(int period, int upperBound, int lowerBound){
        rsi.setValues(period, upperBound, lowerBound);
    }

    public void setEMAValue(int periodShort, int periodMedium, int periodLong){
        ema.setValues(periodShort, periodMedium, periodLong);
    }

    public void setInterval(String interval){
        candleStickFeed.setInterval(interval);
    }

    public void updatePriceFeed(){
        candleStickFeed.update(false);
    }

    public void run(){

        //Reset values and order book before new back test
        startTime = 0;
        endTime = 0;
        dataPoints = 0;
        numberOfTrades = 0;
        numberOfProfitable = 0;
        numberOfUnprofitable = 0;
        rsiIndication = 0;
        emaIndication = 0;
        largestProfit = 0;
        largestLoss = 0;
        profit = 0;
        accountValue = startValue;
        orderBook.clear();

        //Updates EMA and RSI indicator
        rsi.update(candleStickFeed, false);
        ema.update(candleStickFeed, false);

        StrategyRSI strategyRSI = new StrategyRSI();
        StrategyEMACrossover strategyEMACrossover = new StrategyEMACrossover();

        startTime = candleStickFeed.getTradingPair(0).getCandleStick(0).openTime;
        endTime = candleStickFeed.getTradingPair(0).getLast().closeTime;

        //Loops through each coin
        for(int coinIndex = 0; coinIndex < candleStickFeed.getTradingPairs().size(); coinIndex++){

            String symbol = candleStickFeed.getTradingPair(coinIndex).getSymbol();

            //Loops through each candle stick
            for(int candleIndex = 0; candleIndex < candleStickFeed.getTradingPair(coinIndex).getSize(); candleIndex++){
                //Updates teh strategy for current candle stick
                strategyRSI.update(candleStickFeed, rsi, candleIndex);
                strategyEMACrossover.update(candleStickFeed, ema, candleIndex);

                double price = candleStickFeed.getTradingPair(coinIndex).getCandleStick(candleIndex).close;

                if(strategyEMACrossover.getState(symbol) == Strategy.State.BUY){
                        emaIndication++;
                }

                if(strategyRSI.getState(symbol) == Strategy.State.BUY){
                    rsiIndication++;
                }

                //Checks if the coin has already been bought
                if(!orderBook.isBought(symbol)){

                    if(strategyEMACrossover.getState(symbol) == Strategy.State.BUY && strategyRSI.getState(symbol) == Strategy.State.BUY){

                        double riskPrice = (accountValue * 0.1);
                        if(riskPrice < 10) riskPrice = 10;
                        double amount = riskPrice / price;

                        accountValue = accountValue - riskPrice;

                        orderBook.buyOrder(symbol, price, amount);
                    }

                }else{
                    if(strategyEMACrossover.getState(symbol) == Strategy.State.SELL && strategyRSI.getState(symbol) == Strategy.State.SELL){

                        accountValue = accountValue + (price * orderBook.getOpenOrder(symbol).getAmount());
                        orderBook.sellOrder(symbol, price);

                    }
                }

                dataPoints++;
            }
        }


        //Closes all remaining open positions
        for(int coinIndex = 0; coinIndex < candleStickFeed.getTradingPairs().size(); coinIndex++){
            if(orderBook.isBought(candleStickFeed.getTradingPair(coinIndex).getSymbol())){

                double currentPrice = candleStickFeed.getTradingPair(coinIndex).getLast().close;

                accountValue = accountValue + (currentPrice * orderBook.getOpenOrder(candleStickFeed.getTradingPair(coinIndex).getSymbol()).getAmount());

                orderBook.sellOrder(candleStickFeed.getTradingPair(coinIndex).getSymbol(), currentPrice);
            }
        }

        //Loops over the closed orders to get data for the performance
        for(int i = 0; i < orderBook.getClosedOrder().size(); i++){
            profit = profit + orderBook.getClosedOrder(i).getProfit();
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
