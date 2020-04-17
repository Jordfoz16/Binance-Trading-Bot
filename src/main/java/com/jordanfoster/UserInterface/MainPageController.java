package com.jordanfoster.UserInterface;

import com.jordanfoster.BinanceTradingBot;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.annotation.Resources;
import java.net.URL;
import java.util.ArrayList;

public class MainPageController {

    @FXML private TextArea txtLog;
    @FXML private TextArea txtAvailable;
    @FXML private TextArea txtLocked;
    @FXML private TextField txtProfit;

    @FXML private LineChart<Integer, Double> lineChart;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;

    private XYChart.Series<Integer, Double> series;

    @FXML protected void initialize(){
        series = new XYChart.Series<Integer, Double>();
        series.setName("Bitcoin Price");

        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(30);
        xAxis.setTickUnit(1);

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(7000);
        yAxis.setUpperBound(7100);
        yAxis.setTickUnit(10);

        lineChart.getData().add(series);
    }

    @FXML protected void handleStartButton(ActionEvent event){
        BinanceTradingBot.startTradingBot = true;
    }

    @FXML protected void handleStopButton(ActionEvent event){
        BinanceTradingBot.startTradingBot = false;
    }

    public void addLog(String msg){
        txtLog.appendText("\n" + msg);
    }

    public void updateAvailableBalance(String msg){
        txtAvailable.setText(msg);
    }

    public void updateLockedBalance(){

    }

    public void setProfit(double profit){
        String formattedProfit = String.format("%.4f", profit);
        txtProfit.setText(formattedProfit + "$");
    }

    public void updateLineChart(ArrayList<Double> priceData){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                for(int i = 0; i < priceData.size(); i++){
                    lineChart.getData().get(0).getData().add(new XYChart.Data<Integer, Double>(i, priceData.get(i)));
                }
            }
        });
    }
}
