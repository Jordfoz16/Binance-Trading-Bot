package com.jordanfoster.TradingBot.PriceFeed;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.JSONHandler.JSONHandler;
import com.jordanfoster.Networking.BinanceAPI;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class PriceFeed extends Thread{

    //Polling rate in milliseconds
    private int pollingRate = 1000;

    private BinanceAPI binanceAPI;
    private JSONHandler jsonHandler;

    private ArrayList<Price> tradingPairs = new ArrayList<Price>();

    private boolean init = false;

    public PriceFeed(){
        super("priceThread");
        binanceAPI = new BinanceAPI();
        jsonHandler = new JSONHandler();
    }

    public void run(){

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

        try {

            String result = binanceAPI.getTickerPrice();

            ArrayList<JSONObject> jsonObjects = jsonHandler.parseJSON(result);

            for(int i = 0; i < jsonObjects.size(); i++){

                //Gets the last 4 character of the trading pair to find all USDT pairs
                String symbol = jsonObjects.get(i).get("symbol").toString();
                String tradingSymbol = symbol.substring(symbol.length() - 4);
                float price = Float.parseFloat(jsonObjects.get(i).get("price").toString());

                boolean foundPrice = false;

                if(tradingSymbol.equals("USDT")){

                    for(int j = 0; j < tradingPairs.size(); j++){
                        if(tradingPairs.get(j).getSymbol().equals(symbol)){
                            foundPrice = true;
                            tradingPairs.get(j).addPrice(price);
                        }else{
                            foundPrice = false;

                        }
                    }

                    if(foundPrice == false && init == false){
                        tradingPairs.add(new Price(symbol));
                        tradingPairs.get(tradingPairs.size() - 1).addPrice(price);
                    }
                }
            }

            init = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Double> getPriceSymbol(String symbol){
        for(int i = 0; i < tradingPairs.size(); i++){
            if(tradingPairs.get(i).getSymbol().equals(symbol)){
                return tradingPairs.get(i).getPriceList();
            }
        }
        return null;
    }

    public void printPriceFeed(String symbol){
        ArrayList<Double> prices = getPriceSymbol(symbol);

        System.out.print(symbol + ": ");

        for(int i = 0; i < prices.size(); i++){
            String price = String.format("%.8f", prices.get(i));

            System.out.print("\t");
            System.out.print(price);
        }

        System.out.println();
    }

    public ArrayList<Price> getTradingPairs(){
        return tradingPairs;
    }
}
