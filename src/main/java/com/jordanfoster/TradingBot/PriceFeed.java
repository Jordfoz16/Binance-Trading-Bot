package com.jordanfoster.TradingBot;

import com.jordanfoster.FileHandler.WatchListFile;
import com.jordanfoster.HTTPHandler.BinanceAPI;
import com.jordanfoster.JSONHandler.PriceFeedJSON;
import com.jordanfoster.JSONHandler.WatchListJSON;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class PriceFeed {

    private BinanceAPI binanceAPI = new BinanceAPI();
    private PriceFeedJSON priceFeedJSON = new PriceFeedJSON();
    private WatchListJSON watchListJSON = new WatchListJSON();

    private ArrayList<JSONObject> filteredPriceFeed = new ArrayList<JSONObject>();

    public PriceFeed(){

    }

    public void updatePriceFeed(){
        priceFeedJSON.readPriceJSON(binanceAPI.getTickerPrice());
        filterPriceFeed();
    }

    public void filterPriceFeed(){

        for(int priceFeedIndex = 0; priceFeedIndex < priceFeedJSON.getPriceFeed().size(); priceFeedIndex++){
            JSONObject priceItem = priceFeedJSON.getPriceFeed().get(priceFeedIndex);

            for(int watchListIndex = 0; watchListIndex < watchListJSON.getWatchlist().size(); watchListIndex++){
                JSONObject watchItem = watchListJSON.getWatchlist().get(watchListIndex);

                if(priceItem.get("symbol").equals(watchItem.get("symbol"))){
                    filteredPriceFeed.add(priceItem);
                }
            }
        }

        System.out.println(filteredPriceFeed);
    }
}
