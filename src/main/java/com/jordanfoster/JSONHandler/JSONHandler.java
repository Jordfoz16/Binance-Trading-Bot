package com.jordanfoster.JSONHandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class JSONHandler {

    public JSONArray parseJSON(String jsonString){

        JSONParser jsonParser = new JSONParser();

        ContainerFactory containerFactory = new ContainerFactory() {
            @Override
            public Map createObjectContainer() {
                return new LinkedHashMap<>();
            }

            @Override
            public List creatArrayContainer() {
                return new LinkedList<>();
            }
        };

        JSONArray jsonArray = new JSONArray();

        try {
            Object result = jsonParser.parse(jsonString, containerFactory);

            if(result instanceof LinkedHashMap){
                JSONObject jsonObject = new JSONObject();
                jsonObject.putAll((Map) result);
                jsonArray.add(jsonObject);

            }else if(result instanceof LinkedList){

                for(int i = 0; i < ((LinkedList) result).size(); i++){
                    if(((LinkedList) result).get(i) instanceof LinkedHashMap){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.putAll((Map) ((LinkedList) result).get(i));
                        jsonArray.add(jsonObject);
                    }else{
                        jsonArray.add(result);
                    }
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }
}
