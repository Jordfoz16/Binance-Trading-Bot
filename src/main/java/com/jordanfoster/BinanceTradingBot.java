package com.jordanfoster;

import com.jordanfoster.HTTPHandler.BinanceAPI;

public class BinanceTradingBot{

    private BinanceAPI binanceAPI = new BinanceAPI();

    public static void main(String[] arg) {
        BinanceTradingBot btb = new BinanceTradingBot();
        btb.start();
    }

    public void start(){
        //binanceAPI.getServerTime();
        System.out.println(binanceAPI.getTickerPrice());
    }

    public void stop(){

    }
}
