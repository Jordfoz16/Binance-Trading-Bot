package com.jordanfoster.TradingBot;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.FileManagement.FileConfig;
import com.jordanfoster.FileManagement.FileOrders;
import com.jordanfoster.FileManagement.FileTradingPairs;
import com.jordanfoster.TradingBot.BackTesting.BackTester;
import com.jordanfoster.TradingBot.Indicators.EMA.EMA;
import com.jordanfoster.TradingBot.Indicators.RSI.RSI;
import com.jordanfoster.TradingBot.Indicators.RSI.Strategy.StrategyRSI;
import com.jordanfoster.TradingBot.Indicators.Strategy;
import com.jordanfoster.TradingBot.Indicators.TradingPairIndicator;
import com.jordanfoster.TradingBot.PriceFeed.CandleStick.CandleStickFeed;

public class TradingBot extends Thread{

    public static String apiKey = "";
    public static String secretKey = "";

    public static FileConfig fileConfig;
    public static FileTradingPairs fileTradingPairs;
    public static FileOrders fileOrders;

    private static boolean isTrading = true;

    public CandleStickFeed candleStickFeed = new CandleStickFeed();
    public RSI rsi = new RSI();
    public EMA ema = new EMA();

    private boolean initialised = false;
    private int intervalRate = 10000;

    public TradingBot(){
        fileConfig = new FileConfig();
        fileTradingPairs = new FileTradingPairs();
        fileOrders = new FileOrders();
    }

    public void run(){

        long lastTime = System.currentTimeMillis();

        while(true){

            if(isTrading){
                long nowTime = System.currentTimeMillis();

                if(nowTime - lastTime > intervalRate || initialised == false){
                    if(initialised == false) initialised = true;
                    lastTime = nowTime;
                    update();
                }else{
                    try {
                        Thread.sleep(intervalRate / 5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else{

                //Pauses between each isTrading check
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void update(){

        candleStickFeed.update(true);
        ema.update(candleStickFeed, true);
        rsi.update(candleStickFeed, true);


        //Update line chart data
        BinanceTradingBot.mainController.updateOverview(candleStickFeed.getTradingPairs(), ema.getIndicator(), rsi.getIndicator());

        intervalRate = Integer.parseInt(fileConfig.getElement("price-feed", "polling-rate"));
    }

    public static synchronized void setTrading(boolean value){
        isTrading = value;
    }
}
