package com.jordanfoster;

import com.jordanfoster.FileHandler.WatchListFile;
import com.jordanfoster.HTTPHandler.BinanceAPI;
import com.jordanfoster.JSONHandler.PriceFeedJSON;
import com.jordanfoster.TradingBot.PriceFeed;

public class BinanceTradingBot{

    private PriceFeed priceFeed = new PriceFeed();

    public static void main(String[] arg) {
        BinanceTradingBot btb = new BinanceTradingBot();
        btb.start();
    }

    public void start(){
        priceFeed.updatePriceFeed();
    }

    public void stop(){

    }
}
