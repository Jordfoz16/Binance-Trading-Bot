package com.jordanfoster.TradingBot;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.TradingBot.PriceFeed.PriceFeed;
import com.jordanfoster.TradingBot.TradingStrategy.EMA.EMA;
import com.jordanfoster.TradingBot.Wallet.Wallet;
import com.jordanfoster.UserInterface.Logging.Log;

public class TradingBot extends Thread{

    //Polling rate in milliseconds
    private int pollingRate = 2000;

    private double totalProfit = 0;

    Wallet wallet;
    PriceFeed priceFeed;
    EMA ema;

    public TradingBot(){
        super("tradingBotThread");
        priceFeed = new PriceFeed();
        wallet = new Wallet();
        priceFeed.update();
        ema = new EMA(priceFeed.getTradingPairs());
    }

    public void run(){

        new Log("Starting Trading Bot");
        BinanceTradingBot.mainController.updatePriceFeed(priceFeed.getTradingPairs());

        wallet.start();
        long lastTime = System.currentTimeMillis();

        while(BinanceTradingBot.isRunning){
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

        buy();
        sell();

        BinanceTradingBot.mainController.updatePriceFeed(priceFeed.getTradingPairs());
        new Log().setProfit(totalProfit);
    }

    public void buy(){

        for(int i = 0; i < priceFeed.getTradingPairs().size(); i++){
            if(ema.getEMA().get(i).getBuy()){

                //SEND BUY CALL
                ema.getEMA().get(i).setBought(true, priceFeed.getTradingPairs().get(i).getLastPrice());

                String symbol = priceFeed.getTradingPairs().get(i).getSymbol();
                double boughtPrice = priceFeed.getTradingPairs().get(i).getLastPrice();

                new Log("Buy - \t" + symbol + "\t\tPrice - " + String.format("%.4f",boughtPrice));
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

                String symbol = priceFeed.getTradingPairs().get(i).getSymbol();

                new Log("Sold - \t" + symbol + "\t\tPrice -  " + String.format("%.4f",soldPrice));

                totalProfit += profit;

                ema.getEMA().get(i).setBought(false);
            }
        }
    }
}
