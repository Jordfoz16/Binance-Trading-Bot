package com.jordanfoster.UserInterface;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.TradingBot.PriceFeed.Price;
import com.jordanfoster.TradingBot.TradingBot;
import com.jordanfoster.TradingBot.TradingStrategy.EMA.ResultsEMA;
import com.jordanfoster.TradingBot.BoughtCurrency;
import com.jordanfoster.UserInterface.Logging.LineChartData;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.util.ArrayList;

public class MainPageController {

    //Main Page
    @FXML private TextField txtProfit;
    @FXML private ComboBox<String> cmbSymbol;
    @FXML private TableView tbBoughtTable;
    @FXML private LineChart<Integer, Double> lineChart;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;

    //Wallet Page
    @FXML private TextArea txtAvailable;
    @FXML private TextArea txtLocked;

    //EMA
    @FXML private TextField txtThreshold;
    @FXML private TextField txtTimeHoldBuy;
    @FXML private TextField txtTimeHoldSell;
    @FXML private TextField txtCooldown;

    //Account

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

    public void addLineChartSeries(XYChart.Series<Integer, Double> newSeries){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if(newSeries.getData().size() > 30){
                    xAxis.setUpperBound(newSeries.getData().size());
                    //xAxis.autosize();
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

        tbBoughtTable.getItems().clear();

        for(int i = 0; i < boughtCurrenciesList.size(); i++){
            String symbol = boughtCurrenciesList.get(i).getSymbol();
            double price = boughtCurrenciesList.get(i).getPrice();
            double amount = boughtCurrenciesList.get(i).getAmount();
            tbBoughtTable.getItems().add(new BoughtCurrency(symbol, price, amount));
        }
    }

    public void setProfit(double profit){
        String formattedProfit = String.format("%.4f", profit);
        txtProfit.setText(formattedProfit);
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
        xAxis.setTickUnit(1);
        yAxis.setForceZeroInRange(false);

    }

    @FXML protected void updateSettingEMA(ActionEvent event){
        double threshold = Double.parseDouble(txtThreshold.getText());
        int timeHoldBuy = Integer.parseInt(txtTimeHoldBuy.getText());
        int timeHoldSell = Integer.parseInt(txtTimeHoldSell.getText());
        int cooldown = Integer.parseInt(txtCooldown.getText());

        BinanceTradingBot.tradingBot.getEMA().setBuyThreshold(threshold);
        BinanceTradingBot.tradingBot.getEMA().setTradingCooldown(cooldown);

        for(int i = 0; i < BinanceTradingBot.tradingBot.getEMA().getEMA().size(); i++){
            BinanceTradingBot.tradingBot.getEMA().getEMA().get(i).setTimeHoldBuy(timeHoldBuy);
            BinanceTradingBot.tradingBot.getEMA().getEMA().get(i).setTimeHoldSell(timeHoldSell);
        }
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
