package com.jordanfoster.TradingBot.BackTesting;

import com.jordanfoster.Networking.BinanceAPI;

import java.io.IOException;

public class BackTester {

    private BinanceAPI binanceAPI;

    public BackTester(){
        binanceAPI = new BinanceAPI();

        try {
            String result = binanceAPI.getCandlestick("BTCUSDT", "5m", 500);

            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
