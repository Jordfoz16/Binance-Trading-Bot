package com.jordanfoster.FileHandler;

import com.jordanfoster.JSONHandler.WatchListJSON;
import netscape.javascript.JSObject;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class WatchListFile {

    private String filename = "watchlist.txt";
    private File watchlistFile = new File(filename);

    private ArrayList<JSONObject> watchListContent = new ArrayList<JSONObject>();

    public  WatchListFile(){
        if(!watchlistFile.exists()){
            writeFile();
        }

        readFile();
    }

    public void readFile(){

        if(watchlistFile.exists()){
            String line = null;

            try {
                FileReader fileReader = new FileReader(filename);

                BufferedReader bufferedReader = new BufferedReader(fileReader);

                StringBuffer content = new StringBuffer();
                while((line = bufferedReader.readLine()) != null) {
                    content.append(line);
                }

                WatchListJSON watchListJSON = new WatchListJSON();
                watchListContent = watchListJSON.parseWatchList(content.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeFile() {
        try {
            FileWriter fileWriter = new FileWriter(filename);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("[\n" +
                    "  \t{\n" +
                    "\t\t\"symbol\": \"BTCUSDT\"\n" +
                    "\t},\n" +
                    "\t{\n" +
                    "\t\t\"symbol\": \"LTCUSDT\"\n" +
                    "\t},\n" +
                    "\t{\n" +
                    "\t\t\"symbol\": \"ETHUSDT\"\n" +
                    "\t}\n" +
                    "]");

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<JSONObject> getContent(){
        return watchListContent;
    }
}
