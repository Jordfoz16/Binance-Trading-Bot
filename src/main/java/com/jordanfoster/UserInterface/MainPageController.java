package com.jordanfoster.UserInterface;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.TradingBot.PriceFeed.Price;
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

public class MainPageController {

    @FXML private TextArea txtLog;
    @FXML private TextArea txtAvailable;
    @FXML private TextArea txtLocked;
    @FXML private TextField txtProfit;
    @FXML private ComboBox cmbSymbol;

    @FXML private LineChart<Integer, Double> lineChart;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;

    private boolean init = false;

    private XYChart.Series<Integer, Double> series;
    private ArrayList<Price> priceFeed = new ArrayList<Price>();

    public LineChartData lineChartData;

    @FXML protected void initialize(){
        series = new XYChart.Series<Integer, Double>();
        series.setName("Bitcoin Price");

        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(30);
        xAxis.setTickUnit(1);

        lineChart.setAnimated(false);
        lineChart.getData().add(series);
    }

    public void initPriceFeed(){

        if(!init){
            for(int i = 0; i < priceFeed.size(); i++){
                cmbSymbol.getItems().add(i, priceFeed.get(i).getSymbol());
            }

            lineChartData = new LineChartData(priceFeed);
            init = true;
        }
    }

    public void updatePriceFeed(ArrayList<Price> priceFeed){
        this.priceFeed = priceFeed;

        initPriceFeed();

        if(lineChartData != null){
            lineChartData.addData(priceFeed);
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

        lineChartData.setSymbol(index);
        yAxis.autosize();
        yAxis.setForceZeroInRange(false);

    }

    public void updateLineChart(ArrayList<Double> priceData){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                
                if (lineChart.getData().size() > 0){
                    lineChart.getData().get(0).getData().clear();
                }

                for(int i = 0; i < priceData.size(); i++){

                    lineChart.getData().get(0).getData().add(new XYChart.Data<Integer, Double>(i, priceData.get(i)));
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
