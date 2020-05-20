package com.jordanfoster.TradingBot;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.FileManagement.FileConfig;
import com.jordanfoster.FileManagement.FileOrders;
import com.jordanfoster.FileManagement.FileTradingPairs;
import com.jordanfoster.TradingBot.Indicators.EMA.EMA;
import com.jordanfoster.TradingBot.Indicators.Indicator;
import com.jordanfoster.TradingBot.Indicators.RSI.RSI;
import com.jordanfoster.TradingBot.PriceFeed.LivePriceFeed;

import java.util.ArrayList;

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

    public static LivePriceFeed livePriceFeed;
    public static EMA ema;
    public static RSI rsi;

    private static boolean isTrading = false;

    private boolean initialised = false;
    private int intervalRate = 10000;

    public TradingBot(){
        fileConfig = new FileConfig();
        fileTradingPairs = new FileTradingPairs();
        fileOrders = new FileOrders();
        livePriceFeed = new LivePriceFeed();
        ema = new EMA();
        rsi = new RSI();
    }

    public void run(){

        long lastTime = System.currentTimeMillis();

        while(true){

            if(isTrading){
                long nowTime = System.currentTimeMillis();

                if(nowTime - lastTime > intervalRate || initialised == false){
                    if(initialised == false) initialised = true;
                    lastTime = nowTime;

                    livePriceFeed.update();
                    ema.update(livePriceFeed);
                    rsi.update(livePriceFeed);
                    update();

                    //Update line chart data
                    BinanceTradingBot.mainController.updateOverview(livePriceFeed.getTradingPairs(), ema.getData(), rsi.getData(),0);

                    intervalRate = Integer.parseInt(fileConfig.getElement("price-feed", "interval-rate"));
                }else{
                    try {
                        Thread.sleep(intervalRate / 5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else{

                livePriceFeed.clear();
                ema.clear();
                rsi.clear();

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

        if(rsi.getData().get(0).getBought() == false){
            if(rsi.getData().get(0).getState() == State.BUY){
                System.out.println("Bought: " + livePriceFeed.getTradingPair(0).getCurrentPrice());
                rsi.getData().get(0).setBought(true);
            }
        }

        if(rsi.getData().get(0).getBought()){
            if(rsi.getData().get(0).getState() == State.SELL){
                System.out.println("Sold: " + livePriceFeed.getTradingPair(0).getCurrentPrice());
                rsi.getData().get(0).setBought(false);
            }
        }
    }

    public static synchronized void setTrading(boolean value){
        isTrading = value;
    }
}
