package com.jordanfoster.TradingBot.PriceFeed;

import com.jordanfoster.JSONHandler.JSONHandler;
import com.jordanfoster.TradingBot.TradingBot;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class HistoricFeed extends PriceFeed{

    private int selectedCoin = 0;
    private String interval = "5m";

    @Override
    public void update() {
        JSONHandler jsonHandler = new JSONHandler();

        try {

            String apiResponse = binanceAPI.getCandlestick(TradingBot.fileTradingPairs.getSymbol(selectedCoin), interval);

            jsonHandler.parseJSON(apiResponse);

            //System.out.println(priceFeed);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
