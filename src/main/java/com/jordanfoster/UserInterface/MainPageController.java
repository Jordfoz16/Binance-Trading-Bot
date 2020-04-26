package com.jordanfoster.UserInterface;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.TradingBot.PriceFeed.Price;
import com.jordanfoster.TradingBot.TradingBot;
import com.jordanfoster.TradingBot.TradingStrategy.EMA.ResultsEMA;
import com.jordanfoster.TradingBot.BoughtCurrency;
import com.jordanfoster.UserInterface.Logging.LineChartData;
import com.jordanfoster.UserInterface.Logging.Log;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

public class MainPageController {

    //Main Page
    @FXML private TextField txtProfit;
    @FXML private TextField txtPollingRate;
    @FXML private ComboBox<String> cmbSymbol;
    @FXML private TableView tbBoughtTable;
    @FXML private LineChart<Integer, Double> lineChart;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private Label lblStatus;

    //Wallet Page
    @FXML private TextArea txtAvailable;
    @FXML private TextArea txtLocked;

    //EMA
    @FXML private TextField txtThreshold;
    @FXML private TextField txtTimeHoldBuy;
    @FXML private TextField txtTimeHoldSell;
    @FXML private TextField txtCooldown;
    @FXML private TextField txtNValue;

    //Account
    @FXML private TextField txtAPIKey;
    @FXML private TextField txtSecret;

    //Log
    @FXML private TextArea txtLog;

    private boolean init = false;

    private ArrayList<Price> priceFeed = new ArrayList<Price>();

    public LineChartData lineChartData;

    @FXML protected void initialize(){

        //Line Chart Setup
        lineChartData = new LineChartData();
        lineChart.setAnimated(false);
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(30);
        xAxis.setTickUnit(10);

        //Table View Setup
        TableColumn<String, BoughtCurrency> column1 = new TableColumn<>("Symbol");
        column1.setCellValueFactory(new PropertyValueFactory<>("symbol"));

        TableColumn<String, BoughtCurrency> column2 = new TableColumn<>("Price");
        column2.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<String, BoughtCurrency> column3 = new TableColumn<>("Amount");
        column3.setCellValueFactory(new PropertyValueFactory<>("amount"));

        tbBoughtTable.getColumns().add(column1);
        tbBoughtTable.getColumns().add(column2);
        tbBoughtTable.getColumns().add(column3);

        //tbBoughtTable.getItems().add(new BoughtCurrency("BTCUSDT", 100, 0.1));
    }

    synchronized public void updatePriceFeed(ArrayList<Price> priceFeed){
        this.priceFeed = priceFeed;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                initComboBox();

                if(lineChartData != null){
                    lineChartData.updateLineChartData(priceFeed);
                }

            }
        });
    }

    synchronized public void updateIndicator(ArrayList<ResultsEMA> indicatorValue){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(lineChartData != null){
                    lineChartData.updateLineChartIndicator(indicatorValue);
                }
            }
        });
    }

    public void addLineChartSeries(XYChart.Series<Integer, Double> newSeries){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if(newSeries.getData().size() > 30){
                    xAxis.setUpperBound(newSeries.getData().size());
                    xAxis.setTickUnit(10);
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

    public void updateTableView(ArrayList<BoughtCurrency> boughtCurrenciesList){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tbBoughtTable.getItems().clear();

                for(int i = 0; i < boughtCurrenciesList.size(); i++){
                    String symbol = boughtCurrenciesList.get(i).getSymbol();
                    double price = boughtCurrenciesList.get(i).getPrice();
                    double amount = boughtCurrenciesList.get(i).getAmount();
                    tbBoughtTable.getItems().add(new BoughtCurrency(symbol, price, amount));
                }
            }
        });

    }

    public void setProfit(double profit){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String formattedProfit = String.format("%.4f", profit);
                txtProfit.setText(formattedProfit);
            }
        });

    }

    public void setStatus(boolean running){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (running) {
                    lblStatus.setText("Running");
                } else {
                    lblStatus.setText("Not Running");
                }
            }
        });
    }

    public void setAccountPage(String apiKey, String secret){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                txtAPIKey.setText(apiKey);
                txtSecret.setText(secret);
            }
        });
    }

    public void setEMAPage(String threshold, String timeHoldBuy, String timeHoldSell, String cooldown, String nValue){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                txtThreshold.setText(threshold);
                txtTimeHoldBuy.setText(timeHoldBuy);
                txtTimeHoldSell.setText(timeHoldSell);
                txtCooldown.setText(cooldown);
                txtNValue.setText(nValue);
            }
        });
    }

    public void saveAccountPage(){
        String content = "{\n" +
                "\t\"apikey\": \"" + txtAPIKey.getText() + "\",\n" +
                "\t\"secretkey\": \"" + txtSecret.getText() + "\"\n" +
                "}";
        BinanceTradingBot.fileManagement.write("apikey.txt", content);
        BinanceTradingBot.apiKey = txtAPIKey.getText();
        BinanceTradingBot.secretKey = txtSecret.getText();

        new Log().logWarning("API Keys Changed");
    }

    public void saveConfigEMA(){
        double threshold = Double.parseDouble(txtThreshold.getText());
        int timeHoldBuy = Integer.parseInt(txtTimeHoldBuy.getText());
        int timeHoldSell = Integer.parseInt(txtTimeHoldSell.getText());
        int cooldown = Integer.parseInt(txtCooldown.getText());
        double nValue = Double.parseDouble(txtNValue.getText());

        String content = "{\n" +
                "\t\"N-value\": \""+ txtNValue.getText() +"\",\n" +
                "\t\"Buy-Threshold\": \"" + txtThreshold.getText() + "\",\n" +
                "\t\"Buy-HoldTime\": \"" + txtTimeHoldBuy.getText() + "\",\n" +
                "\t\"Sell-HoldTime\": \"" + txtTimeHoldSell.getText() + "\",\n" +
                "\t\"CooldownTime\": \"" + txtCooldown.getText() + "\"\n" +
                "}";

        BinanceTradingBot.fileManagement.write("ema.txt", content);
        BinanceTradingBot.tradingBot.getEMA().setN(nValue);
        BinanceTradingBot.tradingBot.getEMA().setBuyThreshold(threshold);

        for(int i = 0; i < BinanceTradingBot.tradingBot.getEMA().getEMA().size(); i++){
            BinanceTradingBot.tradingBot.getEMA().getEMA().get(i).setTimeHoldBuy(timeHoldBuy);
            BinanceTradingBot.tradingBot.getEMA().getEMA().get(i).setTimeHoldSell(timeHoldSell);
            BinanceTradingBot.tradingBot.getEMA().getEMA().get(i).setTradingCooldown(cooldown);
        }

        new Log().logWarning("EMA Config Changed");
    }

    public void initComboBox(){

        if(!init){
            for(int i = 0; i < priceFeed.size(); i++){
                cmbSymbol.getItems().add(i, priceFeed.get(i).getSymbol());
            }

            init = true;
        }
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
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(30);
        xAxis.setTickUnit(10);
        yAxis.setForceZeroInRange(false);

    }

    @FXML protected void handleMainApply(ActionEvent event){
        BinanceTradingBot.tradingBot.setPollingRate(Integer.parseInt(txtPollingRate.getText()));
    }

    @FXML protected void handleStartButton(ActionEvent event){
        BinanceTradingBot.tradingBot.startTrading();
    }

    @FXML protected void handleStopButton(ActionEvent event){
        BinanceTradingBot.tradingBot.stopTrading();
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
