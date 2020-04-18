package com.jordanfoster.UserInterface.Logging;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.TradingBot.PriceFeed.Price;
import javafx.scene.chart.XYChart;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LineChartData {

    private int graphSize = 30;
    private int selectedSymbol = 0;
    private ArrayList<Double> priceData = new ArrayList<Double>();

    private ArrayList<Price> lastestPrice = new ArrayList<Price>();

    public LineChartData(ArrayList<Price> priceFeed){
        for(int i = 0; i < graphSize; i++){
            priceData.add(i, priceFeed.get(selectedSymbol).getLastPrice());
        }
    }

    public void addData(ArrayList<Price> priceFeed){
        lastestPrice = priceFeed;
        if(priceData.size() > 30){
            priceData.remove(0);
        }

        priceData.add(graphSize, priceFeed.get(selectedSymbol).getLastPrice());

        BinanceTradingBot.mainController.updateLineChart(priceData);
    }

    public void setSymbol(int selectedSymbol){
        this.selectedSymbol = selectedSymbol;
        resetData();
    }

    public void resetData(){
        priceData.clear();
        for(int i = 0; i < graphSize; i++){
            priceData.add(i, lastestPrice.get(selectedSymbol).getLastPrice());
        }
    }
}
