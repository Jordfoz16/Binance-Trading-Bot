package com.jordanfoster.FileManagement;

import com.jordanfoster.TradingBot.TradingBot;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class FileOrders {

    private String path = "order-book.txt";

    private FileManagement fileManagement;

    private JSONObject orderObject = new JSONObject();

    public FileOrders(){
        fileManagement = new FileManagement();

        if(!fileManagement.fileExists(path)) {
            createFile();
        }else{
            readFile();
        }
    }

    public void addOrder(String symbol, TradingBot.State state, double price, String dateTime){
        readFile();

        JSONArray ordersArray = (JSONArray) orderObject.get("orders");

        JSONObject orderElements = new JSONObject();

        orderElements.put("symbol", symbol);
        orderElements.put("order", state.toString());
        orderElements.put("price", Double.toString(price));
        orderElements.put("date-time", dateTime);

        ordersArray.add(orderElements);
        writeFile();
    }


    private void readFile(){
        JSONParser jsonParser = new JSONParser();

        String jsonData = fileManagement.read(path);

        try {
            orderObject = (JSONObject) jsonParser.parse(jsonData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void writeFile(){
        fileManagement.write(path, orderObject.toJSONString());
    }

    private void createFile(){

        JSONObject infoElements = new JSONObject();

        infoElements.put("date-created", "today");
        infoElements.put("time-created", "1pm");

        JSONArray ordersArray = new JSONArray();

        orderObject.put("info", infoElements);
        orderObject.put("orders", ordersArray);

        writeFile();
    }


}
