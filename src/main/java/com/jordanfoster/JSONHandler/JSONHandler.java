package com.jordanfoster.JSONHandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class JSONHandler {

    public ArrayList<JSONObject> parseJSON(String jsonString){

        JSONParser parser = new JSONParser();

        ArrayList<JSONObject> jsonObjectsArray = new ArrayList<JSONObject>();

        //Check if the JSON object is an Array
        if(jsonString.charAt(0) == '{'){

            try {
                JSONObject jsonObject = (JSONObject) parser.parse(jsonString);

                jsonObjectsArray.add(jsonObject);

                return jsonObjectsArray;

            } catch (ParseException e) {
                System.out.println("ERROR: JSON Object Parsing Failed");
            }

        }else{
            try {
                JSONArray jsonArray = (JSONArray) parser.parse(jsonString);

                for(int i = 0; i < jsonArray.size(); i++){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    jsonObjectsArray.add(jsonObject);
                }

                return jsonObjectsArray;

            } catch (ParseException e) {
                System.out.println("ERROR: JSON Array Parsing Failed");
            }
        }

        return null;
    }
}
