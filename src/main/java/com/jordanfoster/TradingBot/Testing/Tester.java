package com.jordanfoster.TradingBot.Testing;

import com.jordanfoster.Networking.BinanceAPI;

import java.io.IOException;

public class Tester {

    private BinanceAPI binanceAPI;

    public Tester(){
        binanceAPI = new BinanceAPI();

        try {
            String result = binanceAPI.getCandlestick("BTCUSDT", "5m");

            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
