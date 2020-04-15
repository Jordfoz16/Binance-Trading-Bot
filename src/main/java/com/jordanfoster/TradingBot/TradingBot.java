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
    private int pollingRate = 10000;
    private double totalProft = 0;
    private int numberOfCurrency = 1;

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

        //emergencySell();
        buy();
        sell();
    }

    ArrayList<BoughtCurrency> boughtCurrencies = new ArrayList<BoughtCurrency>();

    public void buy(){

        for(int i = 0; i < numberOfCurrency; i++){

            double currencyPrice = priceFeed.getTradingPairs().get(i).getLastPrice();
            double emaPrice = ema.getEMA().get(i).getCurrentEMA();
            String symbol = priceFeed.getTradingPairs().get(i).getSymbol();

            System.out.println("Bitcoin Price, " + currencyPrice + ", EMA, " + emaPrice);

            if(currencyPrice > emaPrice) {
                boolean bought = false;

                for (int j = 0; j < boughtCurrencies.size(); j++) {
                    if (boughtCurrencies.get(j).getSymbol().equals(symbol)) {
                        bought = true;
                    }
                }

                if (!bought) {
                    //BUY THE CURRENCY
                    System.out.println("Buy: " + symbol + "  \tPrice: " + currencyPrice + "  \tEMA: " + ema.getEMA().get(i).getCurrentEMA());
                    //System.out.println(", Buy");
                    boughtCurrencies.add(new BoughtCurrency(symbol, currencyPrice));
                }else{
                    //System.out.println("");
                }
            }
        }

//        float bitcoinPrice = priceFeed.getTradingPairs().get(0).getLastPrice();
//        float emaPrice = ema.getEMA().get(0).getCurrentEMA();
//
//        if(bitcoinPrice > emaPrice) {
//            if (boughtBitcoin == false) {
//                System.out.println("Buy Bitcoin");
//                boughtPrice = bitcoinPrice;
//                boughtBitcoin = true;
//            }
//        }
    }

    public void sell(){

        for(int i = 0; i < numberOfCurrency; i++){

            double currencyPrice = priceFeed.getTradingPairs().get(i).getLastPrice();
            double emaPrice = ema.getEMA().get(i).getCurrentEMA();
            String symbol = priceFeed.getTradingPairs().get(i).getSymbol();

            if(currencyPrice < emaPrice) {
                boolean bought = false;
                double boughtPrice = 0.0f;

                for (int j = 0; j < boughtCurrencies.size(); j++) {
                    if (boughtCurrencies.get(j).getSymbol().equals(symbol)) {
                        bought = true;
                        boughtPrice = boughtCurrencies.get(j).getPrice();
                        boughtCurrencies.remove(j);
                    }
                }

                if (bought) {
                    totalProft += currencyPrice - boughtPrice;
                    //SELL THE CURRENCY
                    System.out.println("Sell: " + symbol + "  \tPrice: " + currencyPrice + "  \tEMA: " + ema.getEMA().get(i).getCurrentEMA());
                    System.out.println("Total Profit:\t" + totalProft);
                    //System.out.println(", Sell");
                }else{
                    //System.out.println("");
                }
            }
        }

//        float bitcoinPrice = priceFeed.getTradingPairs().get(0).getLastPrice();
//        float emaPrice = ema.getEMA().get(0).getCurrentEMA();
//
//        if(bitcoinPrice < emaPrice){
//            if(boughtBitcoin == true){
//                System.out.println("Sell Bitcoin");
//                soldPrice = bitcoinPrice;
//
//                float profit = soldPrice - boughtPrice;
//                totalProft += profit;
//                System.out.println("Profit: " + profit);
//                System.out.println("Total profit: " + totalProft);
//                boughtPrice = 0;
//                boughtBitcoin = false;
//            }
//        }
    }

    public void emergencySell(){

        for(int i = 0; i < numberOfCurrency; i++){

            double currencyPrice = priceFeed.getTradingPairs().get(i).getLastPrice();
            double emaPrice = ema.getEMA().get(i).getCurrentEMA();
            String symbol = priceFeed.getTradingPairs().get(i).getSymbol();

            for(int j = 0; j < boughtCurrencies.size(); j++){
                if(boughtCurrencies.get(j).getSymbol().equals(symbol)){

                    double boughtPrice = boughtCurrencies.get(j).getPrice();
                    if(currencyPrice < boughtPrice){
                        System.out.println("Emergency Sell: " + symbol + "\tPrice: " + currencyPrice);
                        totalProft += currencyPrice - boughtPrice;
                        boughtCurrencies.remove(j);
                    }
                }
            }
        }

//        float bitcoinPrice = priceFeed.getTradingPairs().get(0).getLastPrice();
//        float emaPrice = ema.getEMA().get(0).getCurrentEMA();
//
//        if(boughtPrice != 0){
//            if(bitcoinPrice < boughtPrice){
//                System.out.println("Emergency Sell Bitcoin");
//                soldPrice = bitcoinPrice;
//
//                float profit = soldPrice - boughtPrice;
//                totalProft += profit;
//                System.out.println("Profit: " + profit);
//                boughtPrice = 0;
//                boughtBitcoin = false;
//            }
//        }
    }
}
