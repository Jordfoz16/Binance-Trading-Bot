package com.jordanfoster.TradingBot.PracticeAccount;

import com.jordanfoster.TradingBot.Indicators.EMA.EMA;
import com.jordanfoster.TradingBot.Indicators.EMA.Strategy.StrategyEMACrossover;
import com.jordanfoster.TradingBot.Indicators.RSI.RSI;
import com.jordanfoster.TradingBot.Indicators.RSI.Strategy.StrategyRSI;
import com.jordanfoster.TradingBot.Indicators.Strategy;
import com.jordanfoster.TradingBot.Orderbook.OrderBook;
import com.jordanfoster.TradingBot.PriceFeed.CandleStick.CandleStickFeed;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PracticeAccount {

    private OrderBook orderBook = new OrderBook();

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

    public boolean bought = false;

    public boolean isTrading = false;

    public void update(CandleStickFeed candleStickFeed, EMA ema, RSI rsi){
        if(!isTrading) return;

        StrategyEMACrossover strategyEMACrossover = new StrategyEMACrossover();
        StrategyRSI strategyRSI = new StrategyRSI();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        System.out.println(dtf.format(now) + " - Running");

        for(int coinIndex = 0; coinIndex < candleStickFeed.getTradingPairs().size(); coinIndex++) {

            String symbol = candleStickFeed.getTradingPair(coinIndex).getSymbol();
            int lastCandle = candleStickFeed.getTradingPair(coinIndex).getCandleStickData().size() - 1;
            double price = candleStickFeed.getTradingPair(coinIndex).getCandleStick(lastCandle).close;


            strategyEMACrossover.update(candleStickFeed, ema, lastCandle);
            strategyRSI.update(candleStickFeed, rsi, lastCandle);

            if (!orderBook.isBought(symbol)) {
                if (strategyEMACrossover.getState(symbol) == Strategy.State.BUY && strategyRSI.getState(symbol) == Strategy.State.BUY) {

                    double riskPrice = (accountValue * 0.1);
                    if(riskPrice < 10) riskPrice = 10;
                    double amount = riskPrice / price;

                    accountValue = accountValue - riskPrice;

                    orderBook.buyOrder(symbol, price, amount);

                    System.out.println(dtf.format(now) + " - BUY - " + symbol + " - " + price);
                } else {
                    if (strategyEMACrossover.getState(symbol) == Strategy.State.SELL && strategyEMACrossover.getState(symbol) == Strategy.State.SELL) {
                        accountValue = accountValue + (price * orderBook.getOpenOrder(symbol).getAmount());
                        orderBook.sellOrder(symbol, price);
                        System.out.println(dtf.format(now) + " - SOLD - " + symbol + " - " + price);
                    }
                }
            }
        }
        System.out.println(dtf.format(now) + " - Account Value - " + accountValue);
    }
}
