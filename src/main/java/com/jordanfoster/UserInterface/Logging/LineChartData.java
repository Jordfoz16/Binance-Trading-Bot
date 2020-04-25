package com.jordanfoster.UserInterface.Logging;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.TradingBot.PriceFeed.Price;
import com.jordanfoster.TradingBot.TradingStrategy.EMA.ResultsEMA;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;

public class LineChartData {

    private int graphSize = 600;
    private int selectedSymbol = 0;

    public void updateLineChartData(ArrayList<Price> prices) {

        XYChart.Series<Integer, Double> priceSeries = new XYChart.Series<Integer, Double>();

        priceSeries.setName(prices.get(selectedSymbol).getSymbol());

        for(int i = 0; i < prices.get(selectedSymbol).getPriceHistory().size(); i++){
            priceSeries.getData().add(new XYChart.Data<Integer, Double>(i, prices.get(selectedSymbol).getPriceHistory().get(i)));
        }

        BinanceTradingBot.mainController.addLineChartSeries(priceSeries);
    }

    public void updateLineChartIndicator(ArrayList<ResultsEMA> indicatorValues){

        XYChart.Series<Integer, Double> emaSeries = new XYChart.Series<Integer, Double>();

        emaSeries.setName("EMA Value");

        for(int i = 0; i < indicatorValues.get(selectedSymbol).getEMAHistory().size(); i++){
            emaSeries.getData().add(new XYChart.Data<Integer, Double>(i, indicatorValues.get(selectedSymbol).getEMAHistory().get(i)));
        }

        BinanceTradingBot.mainController.addLineChartSeries(emaSeries);
    }

    public void setSymbol(int selectedSymbol){
        this.selectedSymbol = selectedSymbol;
    }

    public int getSelectedIndex(){
        return selectedSymbol;
    }
}
