package com.jordanfoster.TradingBot;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.FileManagement.FileConfig;
import com.jordanfoster.FileManagement.FileTradingPairs;
import com.jordanfoster.TradingBot.Indicators.EMA.EMA;
import com.jordanfoster.TradingBot.PriceFeed.PriceFeed;

public class TradingBot extends Thread{

    public static String apiKey = "";
    public static String secretKey = "";

    public static FileConfig fileConfig;
    public static FileTradingPairs fileTradingPairs;

    public static PriceFeed priceFeed;
    public static EMA ema;

    public TradingBot(){
        fileConfig = new FileConfig();
        fileTradingPairs = new FileTradingPairs();
        priceFeed = new PriceFeed();
        ema = new EMA();
    }

    public void run(){

        long lastTime = System.currentTimeMillis();

        while(true){
            long nowTime = System.currentTimeMillis();

            if(nowTime - lastTime > 1000){
                lastTime = nowTime;

                priceFeed.update();
                ema.update();

                //Update line chart data
                BinanceTradingBot.mainController.updatePriceChart(priceFeed.getTradingPairs(), ema.getEmaValues());
            }
        }
    }
}
