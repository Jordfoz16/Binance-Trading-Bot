package com.jordanfoster.FileManagement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileConfig {

    private String path = "config.txt";

    private FileManagement fileManagement;

    private JSONObject configObject = new JSONObject();

    public FileConfig(){
        fileManagement = new FileManagement();

        if(!fileManagement.fileExists(path)) {
            createFile();
        }else{
            readFile();
        }
    }

    public String getElement(String objectKey, String elementKey){
        readFile();

        JSONObject element = (JSONObject) configObject.get(objectKey);

        return element.get(elementKey).toString();
    }

    public void editElement(String objectKey, String elementKey, String value){
        readFile();

        JSONObject editObject = (JSONObject) configObject.get(objectKey);

        editObject.put(elementKey, value);

        writeFile();
    }

    public void editElement(String objectKey, String elementKey, int value){
        readFile();

        JSONObject editObject = (JSONObject) configObject.get(objectKey);

        editObject.put(elementKey, value);

        writeFile();
    }

    public void editElement(String objectKey, String elementKey, double value){
        readFile();

        JSONObject editObject = (JSONObject) configObject.get(objectKey);

        editObject.put(elementKey, value);

        writeFile();
    }

    private void readFile(){
        JSONParser jsonParser = new JSONParser();

        String jsonData = fileManagement.read(path);

        try {
            configObject = (JSONObject) jsonParser.parse(jsonData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void writeFile(){
        fileManagement.write(path, configObject.toJSONString());
    }

    private void createFile(){

        JSONObject emaElements = new JSONObject();
        JSONObject rsiElements = new JSONObject();
        JSONObject accountElements = new JSONObject();
        JSONObject priceFeedElements = new JSONObject();

        //EMA Values
        emaElements.put("n-value", 360);
        emaElements.put("buy-wait", 10);
        emaElements.put("sell-wait", 0);
        emaElements.put("calibration-time", 30);
        configObject.put("ema", emaElements);

        //RSI Values
        rsiElements.put("rsi-period", 360);
        rsiElements.put("rsi-calibration", 60);
        rsiElements.put("lower-bound", 40);
        rsiElements.put("upper-bound", 60);
        configObject.put("rsi", rsiElements);

        //Account Values
        accountElements.put("api-key", "");
        accountElements.put("secret-key", "");
        configObject.put("account", accountElements);

        //Price Feed Value
        priceFeedElements.put("price-history-size", 600);
        configObject.put("price-feed", priceFeedElements);

        fileManagement.write(path, configObject.toJSONString());
    }

}
