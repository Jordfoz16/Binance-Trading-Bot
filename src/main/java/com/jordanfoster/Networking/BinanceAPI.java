package com.jordanfoster.Networking;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.TradingBot.TradingBot;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class BinanceAPI {

    private boolean networkDebug = false;

    private String apiResponse = "";

    public BinanceAPI(){

    }

    public String getAllCoinInformation() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        /*
        Get information of coins (available for deposit and withdraw) for user.
         */

        String api = "sapi/v1/capital/config/getall";

        ArrayList<Parameter> parameters = new ArrayList<Parameter>();

        parameters.add(new Parameter("timestamp", Long.toString(System.currentTimeMillis())));
        parameters.add(new Parameter("signature", getSignature(parameters)));

        return callAPI(parameters, api);
    }

    public String getTickerPrice() throws IOException {
        /*
        Latest price for a symbol or symbols.
         */

        String api = "api/v3/ticker/price";

        return callAPI(null, api);
    }

    public String getTickerPrice(String symbol) throws IOException{
        /*
        Latest price for a symbol or symbols.
         */

        String api = "api/v3/ticker/price";

        ArrayList<Parameter> parameters = new ArrayList<Parameter>();

        parameters.add(new Parameter("symbol", symbol));

        return callAPI(parameters, api);
    }

    public String getCandlestick(String symbol, String interval) throws IOException {

        String api = "/api/v3/klines";

        ArrayList<Parameter> parameters = new ArrayList<Parameter>();

        parameters.add(new Parameter("symbol", symbol));
        parameters.add(new Parameter("interval", interval));
        parameters.add(new Parameter("limit", Integer.toString(1000)));

        return callAPI(parameters, api);
    }

    private String callAPI(ArrayList<Parameter> parameters, String URL) throws IOException {
        CloseableHttpClient client = HttpClients.custom().build();

        RequestBuilder requestBuilder = RequestBuilder.get();
        requestBuilder.setUri("https://api.binance.com/" + URL);
        requestBuilder.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        requestBuilder.addHeader("X-MBX-APIKEY", TradingBot.apiKey);

        if(parameters != null) {
            for(int i = 0; i < parameters.size(); i++){
                requestBuilder.addParameter(parameters.get(i).getKey(), parameters.get(i).getValue());
            }
        }

        HttpUriRequest request = requestBuilder.build();

        CloseableHttpResponse httpResponse = client.execute(request);

        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }

        reader.close();
        client.close();

        if(networkDebug){
            System.out.println(response.toString());
        }

        return apiResponse = response.toString();
    }

    private String getSignature(ArrayList<Parameter> parameters) throws InvalidKeyException, NoSuchAlgorithmException {

        /*
        Creates a signature from the time stamp and the secret API key.
         */

        StringBuilder parameterStringBuilder = new StringBuilder();

        for(int i = 0; i < parameters.size(); i++){
            parameterStringBuilder.append(parameters.get(i).getKey());
            parameterStringBuilder.append("=");
            parameterStringBuilder.append(parameters.get(i).getValue());
            parameterStringBuilder.append("&");
        }

        String parameterString = parameterStringBuilder.toString();

        //Removes the & at the end of the parameter because it isn't needed
        if(parameterString.length() > 0){

        }

        parameterString = parameterString.substring(0, parameterString.length() - 1);

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

        SecretKeySpec secret_key = new SecretKeySpec(TradingBot.secretKey.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        char[] hash = Hex.encodeHex(sha256_HMAC.doFinal(parameterString.getBytes()));
        String hashString = String.copyValueOf(hash);

        if(networkDebug){
            System.out.println("Message: " + parameterString);
            System.out.println("Secret: " + TradingBot.secretKey);
            System.out.println("Signature: " + hashString);
        }

        return hashString;
    }
}
