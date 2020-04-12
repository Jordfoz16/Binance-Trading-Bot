package com.jordanfoster.HTTPHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BinanceAPI {

    private String apiURL = "https://api.binance.com/api/v3/";

    public BinanceAPI(){

    }

    public String apiRequest(String type, Map<String, String> parameters){

        //Adding the API type to the end of the URL
        String apiURL = this.apiURL + type;

        HttpURLConnection con;
        try {

            //Check if there are parameters
            if(parameters != null){
                apiURL += ParameterStringBuilder.getParamsString(parameters);
            }

            //Creating the HTTP connection
            URL url = new URL(apiURL);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            con.setRequestProperty("Content-Type", "application/json");

            //Getting response codes
            int status = con.getResponseCode();

            if (status > 299) {
                System.out.println("Error Code: " + status);
                return null;
            }

            //Getting the response from the API
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            con.disconnect();

            return content.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getServerTime(){

        return apiRequest("time", null);
    }

    public String getTickerPrice(){

        return apiRequest("ticker/price", null);
    }

    public String getTickerPrice(String ticker){

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("symbol", ticker);

        return apiRequest("ticker/price", parameters);
    }
}
