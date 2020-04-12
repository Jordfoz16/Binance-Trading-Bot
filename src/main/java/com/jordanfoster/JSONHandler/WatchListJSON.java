package com.jordanfoster.JSONHandler;

import com.jordanfoster.FileHandler.WatchListFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class WatchListJSON {

    private ArrayList<JSONObject> watchlist = new ArrayList<JSONObject>();
    private WatchListFile watchlistFile = new WatchListFile();

    public WatchListJSON(){
        parseWatchList(watchlistFile.getContent());
    }

    public void parseWatchList(String content){

        JSONParser parser = new JSONParser();

        try {
            //Loads price feed into a JSON Array
            JSONArray jsonArray = (JSONArray) parser.parse(content);

            ArrayList<JSONObject> watchListArray = new ArrayList<JSONObject>();

            for(int i = 0; i < jsonArray.size(); i++){
                //Access each element of the array
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                watchListArray.add(jsonObject);
            }

            watchlist = watchListArray;

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<JSONObject> getWatchlist(){
        return watchlist;
    }
}
