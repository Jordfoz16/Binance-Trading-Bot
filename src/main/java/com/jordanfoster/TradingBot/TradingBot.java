package com.jordanfoster.TradingBot;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.FileManagement.FileManagement;
import com.jordanfoster.TradingBot.PriceFeed.PriceFeed;
import com.jordanfoster.TradingBot.TradingStrategy.EMA.EMA;
import com.jordanfoster.TradingBot.Wallet.Wallet;
import com.jordanfoster.UserInterface.Logging.Log;

import java.util.ArrayList;

public class TradingBot extends Thread{

    private boolean isTrading = false;

    //Polling rate in milliseconds
    private int pollingRate = 1000;

    private double totalProfit = 0;

    private Wallet wallet;
    private PriceFeed priceFeed;
    private EMA ema;

    private ArrayList<BoughtCurrency> boughtCurrencyArrayList = new ArrayList<BoughtCurrency>();

    public TradingBot(String apiKey, String secretKey){
        super("tradingBotThread");
        priceFeed = new PriceFeed(apiKey, secretKey);
        wallet = new Wallet(apiKey, secretKey);
        ema = new EMA(priceFeed.getTradingPairs());
    }

    public void startTrading(){
        if(!isTrading){
            new Log("Starting Trading");
            isTrading = true;
        }
    }

    public void stopTrading(){
        if(isTrading){
            new Log("Stopping Trading");
            isTrading = false;
        }
    }

    public void run(){

        wallet.start();
        long lastTime = System.currentTimeMillis();

        while(true){
            long nowTime = System.currentTimeMillis();

            if(nowTime - lastTime > pollingRate){
                lastTime = nowTime;
                update();
            }
        }

    }

    public void update(){
        priceFeed.update();
        ema.updateEMA(priceFeed.getTradingPairs(), isTrading);
        BinanceTradingBot.mainController.updatePriceFeed(priceFeed.getTradingPairs());
        BinanceTradingBot.mainController.updateIndicator(ema.getEMA());
        BinanceTradingBot.mainController.updateTableView(boughtCurrencyArrayList);

        updateTrades();
    }

    public void updateTrades(){

        if(isTrading){
            buy();
            sell();
            new Log().setProfit(totalProfit);
        }
    }

    public void buy(){

        System.out.println(ema.getEMA().get(0).timeHoldBuyCounter);

        for(int i = 0; i < priceFeed.getTradingPairs().size(); i++){
            if(ema.getEMA().get(i).getBuy()){

                //SEND BUY CALL
                ema.getEMA().get(i).setBought(true, priceFeed.getTradingPairs().get(i).getCurrentPrice());

                String symbol = priceFeed.getTradingPairs().get(i).getSymbol();
                double boughtPrice = priceFeed.getTradingPairs().get(i).getCurrentPrice();

                boughtCurrencyArrayList.add(new BoughtCurrency(symbol, boughtPrice, 1));
                new Log("Buy - " + symbol + ", Price - " + String.format("%.4f",boughtPrice));
            }
        }
    }

    public void sell(){

        for(int i = 0; i < priceFeed.getTradingPairs().size(); i++){
            if(ema.getEMA().get(i).getSell()){

                //SEND SELL CALL

                ema.getEMA().get(i).setBought(false);

                String symbol = priceFeed.getTradingPairs().get(i).getSymbol();

                for(int j = 0; j < boughtCurrencyArrayList.size(); j++){
                    if(boughtCurrencyArrayList.get(j).getSymbol().equals(symbol)){
                        boughtCurrencyArrayList.remove(j);
                    }
                }

                double boughtPrice = ema.getEMA().get(i).getBoughtPrice();
                double soldPrice = priceFeed.getTradingPairs().get(i).getCurrentPrice();
                double profit = soldPrice - boughtPrice;

                totalProfit += profit;

                new Log("Sold - " + symbol + ", Price -  " + String.format("%.4f",soldPrice));
            }
        }
    }

    public EMA getEMA(){
        return ema;
    }
}