package com.jordanfoster.JSONHandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class PriceHandler {

    public PriceHandler(){

    }

    public ArrayList<JSONObject> readPriceJSON(String priceFeed){

        JSONParser parser = new JSONParser();

        try {
            //Loads price feed into a JSON Array
            JSONArray jsonArray = (JSONArray) parser.parse(priceFeed);

            ArrayList<JSONObject> priceList = new ArrayList<JSONObject>();

            for(int i = 0; i < jsonArray.size(); i++){
                //Access each element of the array
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                priceList.add(jsonObject);
            }

            return priceList;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
