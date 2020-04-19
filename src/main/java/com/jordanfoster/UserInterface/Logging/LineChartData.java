package com.jordanfoster.UserInterface.Logging;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.TradingBot.PriceFeed.Price;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;

public class LineChartData {

    private int graphSize = 240;
    private int selectedSymbol = 0;

    private ArrayList<Double> priceData = new ArrayList<Double>();
    private ArrayList<Double> indicatorData = new ArrayList<Double>();

    public void updateLineChartData(ArrayList<Price> latestPrice) {

        priceData.add(latestPrice.get(selectedSymbol).getCurrentPrice());

        if(priceData.size() > graphSize) {
            priceData.remove(0);
        }

        XYChart.Series<Integer, Double> priceSeries = new XYChart.Series<Integer, Double>();

        priceSeries.setName(latestPrice.get(selectedSymbol).getSymbol());

        for(int i = 0; i < priceData.size(); i++){
            priceSeries.getData().add(new XYChart.Data<Integer, Double>(i, priceData.get(i)));
        }

        BinanceTradingBot.mainController.addLineChartSeries(priceSeries);
    }

    public void updateLineChartIndicator(double indicatorValue){

        indicatorData.add(indicatorValue);

        if(indicatorData.size() > graphSize){
            indicatorData.remove(0);
        }

        XYChart.Series<Integer, Double> emaSeries = new XYChart.Series<Integer, Double>();

        emaSeries.setName("EMA Value");

        for(int i = 0; i < priceData.size(); i++){
            emaSeries.getData().add(new XYChart.Data<Integer, Double>(i, indicatorData.get(i)));
        }

        BinanceTradingBot.mainController.addLineChartSeries(emaSeries);
    }

    public void setSymbol(int selectedSymbol){
        this.selectedSymbol = selectedSymbol;
        resetData();
    }

    public void resetData(){

        priceData.clear();
        indicatorData.clear();
    }

    public int getSelectedIndex(){
        return selectedSymbol;
    }
}
