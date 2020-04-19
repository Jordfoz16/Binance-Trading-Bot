package com.jordanfoster.TradingBot;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.TradingBot.PriceFeed.PriceFeed;
import com.jordanfoster.TradingBot.TradingStrategy.EMA.EMA;
import com.jordanfoster.TradingBot.Wallet.Wallet;
import com.jordanfoster.UserInterface.Logging.Log;

public class TradingBot extends Thread{

    //Polling rate in milliseconds
    private int pollingRate = 1000;

    private double totalProfit = 0;

    private Wallet wallet;
    private PriceFeed priceFeed;
    private EMA ema;

    public TradingBot(){
        super("tradingBotThread");
        priceFeed = new PriceFeed();
        wallet = new Wallet();
        ema = new EMA(priceFeed.getTradingPairs());
    }

    public void run(){

        new Log("Starting Trading Bot");
        //BinanceTradingBot.mainController.updatePriceFeed(priceFeed.getTradingPairs());

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
        BinanceTradingBot.mainController.updatePriceFeed(priceFeed.getTradingPairs());
        BinanceTradingBot.mainController.updateIndicator(ema.getEMA());

        buy();
        sell();

        new Log().setProfit(totalProfit);
    }

    public void buy(){

        for(int i = 0; i < priceFeed.getTradingPairs().size(); i++){
            if(ema.getEMA().get(i).getBuy()){

                //SEND BUY CALL
                ema.getEMA().get(i).setBought(true, priceFeed.getTradingPairs().get(i).getCurrentPrice());

                String symbol = priceFeed.getTradingPairs().get(i).getSymbol();
                double boughtPrice = priceFeed.getTradingPairs().get(i).getCurrentPrice();

                new Log("Buy - " + symbol + ", Price - " + String.format("%.4f",boughtPrice));
            }
        }
    }

    public void sell(){

        for(int i = 0; i < priceFeed.getTradingPairs().size(); i++){
            if(ema.getEMA().get(i).getSell()){

                //SEND SELL CALL

                double boughtPrice = ema.getEMA().get(i).getBoughtPrice();
                double soldPrice = priceFeed.getTradingPairs().get(i).getCurrentPrice();
                double profit = soldPrice - boughtPrice;

                String symbol = priceFeed.getTradingPairs().get(i).getSymbol();

                new Log("Sold - " + symbol + ", Price -  " + String.format("%.4f",soldPrice));

                totalProfit += profit;

                ema.getEMA().get(i).setBought(false);
            }
        }
    }
}
