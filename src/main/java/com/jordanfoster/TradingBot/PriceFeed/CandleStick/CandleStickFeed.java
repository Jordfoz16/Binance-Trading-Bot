package com.jordanfoster.TradingBot.PriceFeed.CandleStick;

import com.jordanfoster.JSONHandler.JSONHandler;
import com.jordanfoster.TradingBot.PriceFeed.PriceFeed;
import com.jordanfoster.TradingBot.PriceFeed.TradingPair;
import com.jordanfoster.TradingBot.TradingBot;
import org.json.simple.JSONArray;

import java.io.IOException;

public class CandleStickFeed extends PriceFeed {

    /*

    CandleStickFeed - sorts through the data that binance sends
    about the candle sticks for a each of the trading pairs.


    Information that binance sends
    1: Open Time
    2: Open
    3: High
    4: Low
    5: Close
    6: Volume
    7: Close Time
    8: Quote asset volume
    9: Number of trades
    10: Taker buy base asset volume
    11: Taker buy quote asset volume
    12: Ignore
     */

    private int selectedCoin = 0;
    private String interval = "30m";

    public int limit = 1000;

    @Override
    public void update(boolean loadValues) {
        JSONHandler jsonHandler = new JSONHandler();

            tradingPairs.clear();

            if(loadValues){
                interval = TradingBot.fileConfig.getElement("price-feed", "interval-rate");
            }

            for(int index = 0; index < TradingBot.fileTradingPairs.getAvailableTradingPairs().size(); index++){
                String apiResponse = null;
                try {
                    apiResponse = binanceAPI.getCandlestick(TradingBot.fileTradingPairs.getSymbol(index), interval, limit);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(apiResponse.equals(null)) return;

                JSONArray candleSticks = jsonHandler.parseJSON(apiResponse);

                tradingPairs.add(new TradingPair(TradingBot.fileTradingPairs.getSymbol(index)));

                for(int i = 0; i < candleSticks.size(); i++){
                    JSONArray currentCandle = (JSONArray) candleSticks.get(i);
                    long openTime = (long) currentCandle.get(0);
                    double open = Double.parseDouble((String) currentCandle.get(1));
                    double high = Double.parseDouble((String) currentCandle.get(2));
                    double low = Double.parseDouble((String) currentCandle.get(3));
                    double close = Double.parseDouble((String) currentCandle.get(4));
                    double volume = Double.parseDouble((String) currentCandle.get(5));
                    long closeTime = (long) currentCandle.get(0);

                    tradingPairs.get(index).add(openTime, open, high, low, close, volume, closeTime);
                }
            }
    }

    public void setInterval(String interval){
        this.interval = interval;
    }
}
