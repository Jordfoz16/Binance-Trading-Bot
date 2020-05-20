package com.jordanfoster.FileManagement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileTradingPairs {

    private String path = "trading-pairs.txt";

    private FileManagement fileManagement;

    private JSONArray tradingPairArray = new JSONArray();

    public FileTradingPairs(){
        fileManagement = new FileManagement();

        if(!fileManagement.fileExists(path)) {
            createFile();
        }else{
            readFile();
        }
    }

    public JSONArray getAvailableTradingPairs(){
        return tradingPairArray;
    }

    private void readFile(){
        JSONParser jsonParser = new JSONParser();

        String jsonData = fileManagement.read(path);

        try {
            tradingPairArray = (JSONArray) jsonParser.parse(jsonData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void writeFile(){
        fileManagement.write(path, tradingPairArray.toJSONString());
    }

    private void createFile(){

        tradingPairArray.add("BTCUSDT");
        tradingPairArray.add("ETHUSDT");

        fileManagement.write(path, tradingPairArray.toJSONString());
    }
}
