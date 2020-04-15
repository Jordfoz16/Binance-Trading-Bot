package com.jordanfoster;

import com.jordanfoster.TradingBot.PriceFeed.PriceFeed;
import com.jordanfoster.TradingBot.TradingBot;
import com.jordanfoster.TradingBot.Wallet.Wallet;

public class BinanceTradingBot {

    public static boolean priceFeedRunning = true;

    TradingBot tradingBot = new TradingBot();

    public void start(){
        tradingBot.start();
    }

    public static void main(String[] arg){
        BinanceTradingBot btb = new BinanceTradingBot();
        btb.start();
    }
}
