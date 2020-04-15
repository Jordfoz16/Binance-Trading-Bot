package com.jordanfoster.TradingBot.Wallet;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.JSONHandler.JSONHandler;
import com.jordanfoster.Networking.BinanceAPI;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Wallet extends Thread{

    private int pollingRate = 60000;

    private BinanceAPI binanceAPI = new BinanceAPI();
    private JSONHandler jsonHandler = new JSONHandler();

    ArrayList<Crypto> availableBalance = new ArrayList<Crypto>();

    public Wallet(){
        super("walletThread");
    }

    public void run(){
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

        availableBalance.clear();

        try {
            String result = binanceAPI.getAllCoinInformation();

            ArrayList<JSONObject> jsonObjects = jsonHandler.parseJSON(result);

            for(int i = 0; i < jsonObjects.size(); i++){
                JSONObject item = jsonObjects.get(i);

                float freeBalance = Float.parseFloat(item.get("free").toString());
                float lockBalance = Float.parseFloat(item.get("locked").toString());

                if(freeBalance > 0){

                    String symbol = item.get("coin").toString();

                    Crypto crypto = new Crypto(symbol, freeBalance, lockBalance);

                    availableBalance.add(crypto);
                }
            }

            System.out.println("Updated Wallet");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printBalance(){
        for(int i = 0; i < availableBalance.size(); i++){

            String symbol = availableBalance.get(i).getSymbol();
            String free = String.format("%.8f", availableBalance.get(i).getAvailableBalance());
            String locked = String.format("%.8f", availableBalance.get(i).getLockedBalance());

            System.out.println("Symbol: " + symbol + "  \t Free: " + free + "  \t Locked: " + locked);
        }
    }
}
