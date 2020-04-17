package com.jordanfoster.UserInterface.Logging;

import com.jordanfoster.BinanceTradingBot;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;

public class LineChartData {

    private int graphSize = 30;
    private ArrayList<Double> priceData = new ArrayList<Double>();

    public void addData(double newData){
        if(priceData.size() > 30){
            priceData.remove(priceData.size());
        }

        priceData.add(0, newData);

        BinanceTradingBot.mainController.updateLineChart(priceData);
    }
}
