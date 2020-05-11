package com.jordanfoster.TradingBot;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.FileManagement.FileConfig;
import com.jordanfoster.FileManagement.FileTradingPairs;
import com.jordanfoster.TradingBot.Indicators.EMA.EMA;
import com.jordanfoster.TradingBot.Indicators.RSI.RSI;
import com.jordanfoster.TradingBot.PriceFeed.PriceFeed;

public class TradingBot extends Thread{

    public static String apiKey = "";
    public static String secretKey = "";

    public static FileConfig fileConfig;
    public static FileTradingPairs fileTradingPairs;

    public static PriceFeed priceFeed;
    public static EMA ema;
    public static RSI rsi;

    public TradingBot(){
        fileConfig = new FileConfig();
        fileTradingPairs = new FileTradingPairs();
        priceFeed = new PriceFeed();
        ema = new EMA();
        rsi = new RSI();
    }

    public void run(){

        long lastTime = System.currentTimeMillis();

        while(true){
            long nowTime = System.currentTimeMillis();

            if(nowTime - lastTime > 1000){
                lastTime = nowTime;

                priceFeed.update();
                ema.update();
                rsi.update();

                //Update line chart data
                BinanceTradingBot.mainController.updateOverview(priceFeed.getTradingPairs(), ema.getEmaValues(), rsi.getRsiValues(),0);
            }
        }
    }
}
