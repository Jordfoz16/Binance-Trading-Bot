package com.jordanfoster.JSONHandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class WatchListJSON {

    public ArrayList<JSONObject> parseWatchList(String watchlist){

        JSONParser parser = new JSONParser();

        try {
            //Loads price feed into a JSON Array
            JSONArray jsonArray = (JSONArray) parser.parse(watchlist);

            ArrayList<JSONObject> watchListArray = new ArrayList<JSONObject>();

            for(int i = 0; i < jsonArray.size(); i++){
                //Access each element of the array
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                watchListArray.add(jsonObject);
            }

            return watchListArray;

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return null;
    }
}
