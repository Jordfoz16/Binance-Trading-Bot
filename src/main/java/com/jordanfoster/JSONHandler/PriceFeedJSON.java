package com.jordanfoster.JSONHandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PriceFeedJSON {

    private ArrayList<JSONObject> priceFeed = new ArrayList<JSONObject>();

    public void readPriceJSON(String content){

        JSONParser parser = new JSONParser();

        try {
            //Loads price feed into a JSON Array
            JSONArray jsonArray = (JSONArray) parser.parse(content);

            ArrayList<JSONObject> priceList = new ArrayList<JSONObject>();

            for(int i = 0; i < jsonArray.size(); i++){
                //Access each element of the array
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                priceList.add(jsonObject);
            }

            priceFeed = priceList;

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<JSONObject> getPriceFeed(){
        return priceFeed;
    }
}
