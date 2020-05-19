package com.jordanfoster.TradingBot;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.FileManagement.FileConfig;
import com.jordanfoster.FileManagement.FileOrders;
import com.jordanfoster.FileManagement.FileTradingPairs;
import com.jordanfoster.TradingBot.Indicators.EMA.EMA;
import com.jordanfoster.TradingBot.Indicators.EMA.EMAValue;
import com.jordanfoster.TradingBot.Indicators.RSI.RSI;
import com.jordanfoster.TradingBot.Indicators.RSI.RSIValue;
import com.jordanfoster.TradingBot.PriceFeed.PriceFeed;

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

    public static PriceFeed priceFeed;
    public static EMA ema;
    public static RSI rsi;

    private int intervalRate = 10000;

    public TradingBot(){
        fileConfig = new FileConfig();
        fileTradingPairs = new FileTradingPairs();
        fileOrders = new FileOrders();
        priceFeed = new PriceFeed();
        ema = new EMA();
        rsi = new RSI();
    }

    public void run(){

        long lastTime = System.currentTimeMillis();

        while(true){
            long nowTime = System.currentTimeMillis();

            if(nowTime - lastTime > intervalRate){
                lastTime = nowTime;

                priceFeed.update();
                ema.update();
                rsi.update();
                update();

                //Update line chart data
                BinanceTradingBot.mainController.updateOverview(priceFeed.getTradingPairs(), ema.getEmaValues(), rsi.getRsiValues(),0);
                intervalRate = Integer.parseInt(fileConfig.getElement("price-feed", "interval-rate"));
            }else{
                try {
                    Thread.sleep(intervalRate / 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    boolean bitcoinBought = false;

    public void update(){

        if(bitcoinBought == false){
            if(ema.getEmaValues().get(0).getState() == State.BUY){
                if(rsi.getRsiValues().get(0).getState() == State.BUY){
                    System.out.println("Bought: " + priceFeed.getTradingPair(0).getCurrentPrice());
                    bitcoinBought = true;
                }
            }
        }

        if(bitcoinBought){
            if(ema.getEmaValues().get(0).getState() == State.SELL){
                if(rsi.getRsiValues().get(0).getState() == State.SELL){
                    System.out.println("Sold: " + priceFeed.getTradingPair(0).getCurrentPrice());
                    bitcoinBought = false;
                }
            }
        }
    }
}
