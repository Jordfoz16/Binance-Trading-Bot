package com.jordanfoster;

import com.jordanfoster.FileHandler.WatchListFile;
import com.jordanfoster.HTTPHandler.BinanceAPI;
import com.jordanfoster.JSONHandler.PriceFeedJSON;

public class BinanceTradingBot{

    private BinanceAPI binanceAPI = new BinanceAPI();
    private PriceFeedJSON priceFeedJSON = new PriceFeedJSON();
    private WatchListFile watchListFile = new WatchListFile();

    public static void main(String[] arg) {
        BinanceTradingBot btb = new BinanceTradingBot();
        btb.start();
    }

    public void start(){
        //binanceAPI.getServerTime();
//        String priceFeed = binanceAPI.getTickerPrice();
//
//        priceFeedJSON.readPriceJSON(priceFeed);

        watchListFile.getContent();
    }

    public void stop(){

    }
}
