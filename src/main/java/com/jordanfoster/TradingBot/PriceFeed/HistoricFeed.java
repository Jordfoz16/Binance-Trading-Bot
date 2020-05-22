package com.jordanfoster.TradingBot.PriceFeed;

import com.jordanfoster.JSONHandler.JSONHandler;
import com.jordanfoster.TradingBot.Indicators.EMA.EMA;
import com.jordanfoster.TradingBot.Indicators.RSI.RSI;
import com.jordanfoster.TradingBot.TradingBot;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class HistoricFeed extends PriceFeed{

    /*
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
    private String interval = "5m";

    @Override
    public void update() {
        JSONHandler jsonHandler = new JSONHandler();

        try {

            tradingPairs.clear();

            String apiResponse = binanceAPI.getCandlestick(TradingBot.fileTradingPairs.getSymbol(selectedCoin), interval);

            JSONArray candleSticks = jsonHandler.parseJSON(apiResponse);

            tradingPairs.add(new TradingPair("BTCUSDT"));

            for(int i = 0; i < candleSticks.size(); i++){
                JSONArray currentCandle = (JSONArray) candleSticks.get(i);
                tradingPairs.get(0).addPrice(Double.parseDouble((String) currentCandle.get(4)));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
