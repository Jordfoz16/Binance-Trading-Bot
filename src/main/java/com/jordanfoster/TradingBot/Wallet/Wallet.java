package com.jordanfoster.TradingBot.Wallet;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.JSONHandler.JSONHandler;
import com.jordanfoster.UserInterface.Logging.Log;
import com.jordanfoster.Networking.BinanceAPI;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Wallet extends Thread{

    private int pollingRate = 10000;

    private BinanceAPI binanceAPI = new BinanceAPI();
    private JSONHandler jsonHandler = new JSONHandler();

    ArrayList<Crypto> availableBalance = new ArrayList<Crypto>();

    public Wallet(){
        super("walletThread");
    }

    public void run(){

        new Log("Loading Wallet");

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

            BinanceTradingBot.mainController.updateAvailableBalance(printBalance());
            new Log("Wallet Updated");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String printBalance(){

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < availableBalance.size(); i++){

            String symbol = availableBalance.get(i).getSymbol();
            String free = String.format("%.8f", availableBalance.get(i).getAvailableBalance());
            String locked = String.format("%.8f", availableBalance.get(i).getLockedBalance());

            stringBuilder.append("Symbol:\t" + symbol + "\t\tAvailable:\t" + free + "\n");
        }

        return stringBuilder.toString();
    }
}
