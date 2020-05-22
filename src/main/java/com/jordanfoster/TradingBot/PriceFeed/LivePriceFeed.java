package com.jordanfoster.TradingBot.PriceFeed;

import com.jordanfoster.JSONHandler.JSONHandler;
import com.jordanfoster.Networking.BinanceAPI;
import com.jordanfoster.TradingBot.TradingBot;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class LivePriceFeed extends PriceFeed{

    @Override
    public void update(){
        JSONHandler jsonHandler = new JSONHandler();

        try {
            String apiResponse = binanceAPI.getTickerPrice();

            JSONArray priceFeed = jsonHandler.parseJSON(apiResponse);

            JSONArray filter = TradingBot.fileTradingPairs.getAvailableTradingPairs();

            //Loops through the filtered symbols from trading-pairs.txt
            for(int filterIndex = 0; filterIndex < filter.size(); filterIndex++){
                String filterSymbol = filter.get(filterIndex).toString();

                //Loops through all of the symbols from the API call
                for(int selectedIndex = 0; selectedIndex < priceFeed.size(); selectedIndex++){
                    JSONObject current = (JSONObject) priceFeed.get(selectedIndex);

                    String selectedSymbol = (String)current.get("symbol");

                    //If the API symbol is equal to the filtered symbol it gets the price
                    if(filterSymbol.equals(selectedSymbol)){

                        double selectedPrice = Double.parseDouble((String) current.get("price"));

                        //If its hasn't been initialized, it adds the new trading pair
                        if(initialized == false){
                            tradingPairs.add(new TradingPair(selectedSymbol, selectedPrice));
                        }else{
                            //Else it adds a new price to the trading pair
                            for(int tradingIndex = 0; tradingIndex < tradingPairs.size(); tradingIndex++){
                                String tradingSymbol = tradingPairs.get(tradingIndex).getSymbol();

                                if(filterSymbol.equals(tradingSymbol)){
                                    tradingPairs.get(tradingIndex).addPrice(selectedPrice);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }

            initialized = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
