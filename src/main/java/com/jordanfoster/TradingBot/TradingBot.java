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

    private double totalProfit = 0;

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

        //printBTC();

        buy();
        sell();

        System.out.println("Profit: " + totalProfit);
    }

    public void buy(){

        for(int i = 0; i < priceFeed.getTradingPairs().size(); i++){
            if(ema.getEMA().get(i).getBuy()){

                //SEND BUY CALL
                ema.getEMA().get(i).setBought(true, priceFeed.getTradingPairs().get(i).getLastPrice());
            }
        }
    }

    public void sell(){

        for(int i = 0; i < priceFeed.getTradingPairs().size(); i++){
            if(ema.getEMA().get(i).getSell()){

                //SEND SELL CALL

                double boughtPrice = ema.getEMA().get(i).getBoughtPrice();
                double soldPrice = priceFeed.getTradingPairs().get(i).getLastPrice();
                double profit = soldPrice - boughtPrice;

                System.out.println("Sold Price: " + soldPrice + ", Bought Price: " + boughtPrice);

                totalProfit += profit;

                ema.getEMA().get(i).setBought(false);
            }
        }
    }

    public void printBTC(){
        double btcPrice = priceFeed.getTradingPairs().get(0).getLastPrice();
        double emaPrice = ema.getEMA().get(0).getLastEMA();
        boolean bought = ema.getEMA().get(0).getBought();

        System.out.println("BTC: " + btcPrice + ",\t EMA: " + emaPrice + ",\t Bought: " + bought);
    }
}
