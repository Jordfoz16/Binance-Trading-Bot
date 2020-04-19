package com.jordanfoster.UserInterface;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.TradingBot.PriceFeed.Price;
import com.jordanfoster.TradingBot.TradingStrategy.EMA.ResultsEMA;
import com.jordanfoster.UserInterface.Logging.LineChartData;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Observable;

public class MainPageController {

    @FXML private TextArea txtLog;
    @FXML private TextArea txtAvailable;
    @FXML private TextArea txtLocked;
    @FXML private TextField txtProfit;
    @FXML private ComboBox<String> cmbSymbol;

    @FXML private LineChart<Integer, Double> lineChart;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;

    private boolean init = false;

    private ArrayList<Price> priceFeed = new ArrayList<Price>();

    public LineChartData lineChartData;

    @FXML protected void initialize(){

        lineChartData = new LineChartData();

        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(30);
        xAxis.setTickUnit(1);

        lineChart.setAnimated(false);
    }

    public void initComboBox(){

        if(!init){
            for(int i = 0; i < priceFeed.size(); i++){
                cmbSymbol.getItems().add(i, priceFeed.get(i).getSymbol());
            }

            init = true;
        }
    }

    public void updatePriceFeed(ArrayList<Price> priceFeed){
        this.priceFeed = priceFeed;

        initComboBox();

        if(lineChartData != null){
            lineChartData.updateLineChartData(priceFeed);
        }
    }

    public void updateIndicator(ArrayList<ResultsEMA> indicatorValue){

        if(lineChartData != null){
            lineChartData.updateLineChartIndicator(indicatorValue.get(lineChartData.getSelectedIndex()).getCurrentEMA());
        }
    }

    public void setProfit(double profit){
        String formattedProfit = String.format("%.4f", profit);
        txtProfit.setText(formattedProfit);
    }

    @FXML protected void handleComboBox(ActionEvent event){
        String selected = (String) cmbSymbol.getValue();

        int index = 0;
        for(int i = 0; i < priceFeed.size(); i++){
            if(selected.equals(priceFeed.get(i).getSymbol())){
                index = i;
            }
        }

        //Reset the line charts yAxis
        lineChartData.setSymbol(index);
        lineChart.getData().clear();
        yAxis.autosize();
        yAxis.setForceZeroInRange(false);

    }

    public void addLineChartSeries(XYChart.Series<Integer, Double> newSeries){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if(newSeries.getData().size() > 30){
                    xAxis.setUpperBound(newSeries.getData().size());
                }

                if(lineChart.getData().size() < 2){
                    lineChart.getData().add(newSeries);
                }

                for(int i = 0; i < lineChart.getData().size(); i++){
                    if(lineChart.getData().get(i).getName().equals(newSeries.getName())){
                        lineChart.getData().get(i).setData(newSeries.getData());
                    }
                }

                yAxis.autosize();
                yAxis.setForceZeroInRange(false);
            }
        });
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
}
