package com.jordanfoster;

import com.jordanfoster.HTTPHandler.BinanceAPI;
import com.jordanfoster.JSONHandler.PriceHandler;

public class BinanceTradingBot{

    private BinanceAPI binanceAPI = new BinanceAPI();
    private PriceHandler priceHandler = new PriceHandler();

    public static void main(String[] arg) {
        BinanceTradingBot btb = new BinanceTradingBot();
        btb.start();
    }

    public void start(){
        //binanceAPI.getServerTime();
        String priceFeed = binanceAPI.getTickerPrice();

        priceHandler.readPriceJSON(priceFeed);
    }

    public void stop(){

    }
}
