package com.jordanfoster.TradingBot;

import com.jordanfoster.FileManagement.FileConfig;
import com.jordanfoster.FileManagement.FileOrders;
import com.jordanfoster.FileManagement.FileTradingPairs;
import com.jordanfoster.TradingBot.Indicators.EMA.EMA;
import com.jordanfoster.TradingBot.Indicators.RSI.RSI;
import com.jordanfoster.TradingBot.PriceFeed.CandleStick.CandleStickFeed;

public class TradingBot extends Thread{

    public enum State{
        BUY,
        SELL,
        NONE,
        HOLD,
        CALIBRATION
    }

    public static String apiKey = "";
    public static String secretKey = "";

    public static FileConfig fileConfig;
    public static FileTradingPairs fileTradingPairs;
    public static FileOrders fileOrders;

    private static boolean isTrading = true;

    private CandleStickFeed candleStickFeed = new CandleStickFeed();
    private RSI rsi = new RSI();
    private EMA ema = new EMA();

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
                    candleStickFeed.update();
                    ema.update(candleStickFeed);
                    rsi.update(candleStickFeed);

                    //Update line chart data
                    //BinanceTradingBot.mainController.updateOverview(livePriceFeed.getTradingPairs(), ema.getData(), rsi.getData(),0);

                    intervalRate = Integer.parseInt(fileConfig.getElement("price-feed", "interval-rate"));
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

    }

    public static synchronized void setTrading(boolean value){
        isTrading = value;
    }
}
