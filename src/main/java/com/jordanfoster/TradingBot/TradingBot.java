package com.jordanfoster.TradingBot;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.TradingBot.PriceFeed.PriceFeed;
import com.jordanfoster.TradingBot.TradingStrategy.EMA.EMA;
import com.jordanfoster.TradingBot.Wallet.Wallet;

import javax.sound.midi.SysexMessage;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class TradingBot extends Thread{

    //Polling rate in milliseconds
    private int pollingRate = 2000;

    Wallet wallet = new Wallet();
    PriceFeed priceFeed = new PriceFeed();
    EMA ema;

    public TradingBot(){
        super("tradingBotThread");
        priceFeed.update();
        ema = new EMA(priceFeed.getTradingPairs());
    }

    public void run(){

        //wallet.update();
        //wallet.printBalance();

        long lastTime = System.currentTimeMillis();

        while(BinanceTradingBot.priceFeedRunning){
            long nowTime = System.currentTimeMillis();

            if(nowTime - lastTime > pollingRate){
                lastTime = nowTime;
                update();
            }
        }

    }

    public void update(){
        priceFeed.update();
        ema.updateEMA(priceFeed.getTradingPairs());

        printBTC();

        System.out.println("Buy: " + ema.getEMA().get(0).getBuy() +
                ", Sell: " + ema.getEMA().get(0).getSell() + ", Bought: " +
                ema.getEMA().get(0).getBought());

        buy();
        sell();
    }

    public void buy(){

        for(int i = 0; i < priceFeed.getTradingPairs().size(); i++){
            if(ema.getEMA().get(0).getBuy()){

                //SEND BUY CALL
                ema.getEMA().get(0).setBought(true, priceFeed.getTradingPairs().get(i).getLastPrice());
            }
        }
    }

    public void sell(){

        for(int i = 0; i < priceFeed.getTradingPairs().size(); i++){
            if(ema.getEMA().get(0).getSell()){

                //SEND SELL CALL
                ema.getEMA().get(0).setBought(false);
            }
        }
    }

    public void printBTC(){
        double btcPrice = priceFeed.getTradingPairs().get(0).getLastPrice();
        double emaPrice = ema.getEMA().get(0).getLastEMA();

        System.out.println("BTC: " + btcPrice + ",\t EMA: " + emaPrice);
    }
}
