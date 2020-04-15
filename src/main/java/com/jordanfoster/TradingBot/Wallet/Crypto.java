package com.jordanfoster.TradingBot.Wallet;

public class Crypto {

    private String symbol = "";
    private float avilableBalance = 0.0f;
    private float lockedBalance = 0.0f;

    public Crypto(String symbol, float avilableBalance, float lockedBalance){
        this.symbol = symbol;
        this.avilableBalance = avilableBalance;
        this.lockedBalance = lockedBalance;
    }

    public String getSymbol(){
        return symbol;
    }

    public float getAvailableBalance(){
        return avilableBalance;
    }

    public float getLockedBalance(){
        return lockedBalance;
    }
}
