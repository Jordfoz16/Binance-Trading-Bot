package com.jordanfoster.TradingBot.Orderbook;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderBook {

    /*
    OrderBook - keeps track of currently open positions
    as well as all closed positions. It also checks if a
    trading has already been bought.
     */

    private HashMap<String, BoughtTradingPair> openOrders = new HashMap<>();
    private ArrayList<SoldTradingPair> closedOrders = new ArrayList<>();


    public void buyOrder(String symbol, double price, double amount){
        openOrders.put(symbol, new BoughtTradingPair(price, amount));
    }

    public void sellOrder(String symbol, double price){
        BoughtTradingPair sell = openOrders.get(symbol);

        closedOrders.add(new SoldTradingPair(symbol, sell.getPrice(), price, sell.getAmount()));

        openOrders.remove(symbol);
    }

    public BoughtTradingPair getOpenOrder(String symbol){
        return openOrders.get(symbol);
    }

    public ArrayList<SoldTradingPair> getClosedOrder(){
        return  closedOrders;
    }

    public SoldTradingPair getClosedOrder(int index){
        return  closedOrders.get(index);
    }

    public boolean isBought(String symbol){
        boolean bought = false;

        if(openOrders.get(symbol) != null){
            bought = true;
        }

        return bought;
    }

    public void clear(){
        openOrders.clear();
        closedOrders.clear();
    }
}
