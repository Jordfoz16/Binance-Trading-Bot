package com.jordanfoster.Userinterface;

import com.jordanfoster.TradingBot.PriceFeed.TradingPair;
import com.jordanfoster.TradingBot.Testing.Tester;
import com.jordanfoster.TradingBot.TradingBot;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MainController {

    //Overview Tab
    @FXML private Button btnStart;
    @FXML private Button btnStop;

    @FXML private Label lblRunning;
    @FXML private Label lblRunTime;

    //Overview Tab - Performance
    @FXML private Label lblStartingValue;
    @FXML private Label lblCurrentValue;
    @FXML private Label lblProfit;
    @FXML private Label lblIndicatorEMA;
    @FXML private Label lblIndicatorRSI;
    @FXML private ComboBox<String> cbTradingPairs;

    //Overview Tab - Charts
    private boolean initializedPriceChart = false;
    private boolean initializedRSIChart = false;
    @FXML private LineChart<Integer, Double> chartPrice;
    @FXML private NumberAxis xAxisPrice;
    @FXML private NumberAxis yAxisPrice;
    @FXML private LineChart<Integer, Double> chartRSI;
    @FXML private NumberAxis xAxisRSI;
    @FXML private NumberAxis yAxisRSI;

    //Overview Tab - Open Positions
    @FXML private TableView tablePositions;

    //EMA Tab
    @FXML private LineChart<Integer, Double> chartEMATab;
    @FXML private NumberAxis xAxisEMATab;
    @FXML private NumberAxis yAxisEMATab;

    //EMA Tab - Value
    @FXML private TextField txtNValue;

    //EMA Tab - Time Wait Value
    @FXML private TextField txtBuyWaitTime;
    @FXML private TextField txtSellWaitTime;

    //EMA Tab - Calibration
    @FXML private TextField txtCalibrationTime;

    //EMA Tab - Save
    @FXML private Button btnSaveEMA;

    //RSI Tab
    @FXML private TextField txtRSIPeriod;
    @FXML private TextField txtRSICalibration;
    @FXML private TextField txtUpperRSI;
    @FXML private TextField txtLowerRSI;
    @FXML private LineChart<Integer, Double> chartRSITab;
    @FXML private NumberAxis xAxisRSITab;
    @FXML private NumberAxis yAxisRSITab;

    //Testing Tab
    @FXML private Label lblStartingDate;
    @FXML private Label lblEndingDate;
    @FXML private Label lblDataPoints;
    @FXML private Label lblEMAIndications;
    @FXML private Label lblIntervalRate;
    @FXML private Label lblRSIIndications;
    @FXML private Label lblTestNumberOfTrades;
    @FXML private Label lblTestProfitableTrades;
    @FXML private Label lblTestUnprofitableTrades;
    @FXML private Label lblTestTotalProfit;
    @FXML private Label lblTestLargestProfit;
    @FXML private Label lblTestLargestLoss;
    @FXML private TextField txtEMAnValue;
    @FXML private TextField txtEMABuyWait;
    @FXML private TextField txtEMASellWait;
    @FXML private TextField txtTestingRSIPeriod;
    @FXML private TextField txtTestingRSIUpper;
    @FXML private TextField txtTestingRSILower;



    //Order Book Tab
    @FXML private TextField txtTrades;
    @FXML private TableView tableOrderBook;

    //Account Tab
    @FXML private TextField txtAPI;
    @FXML private TextField txtSecret;
    @FXML private Button btnSaveAPI;

    //Account Tab - Network Test
    @FXML private Label lblConnection;
    @FXML private Label lblWalletConnection;
    @FXML private Label lblPriceConnection;
    @FXML private Button btnNetworkTest;
    @FXML private TextField txtIntervalRate;

    //Settings Tab - Config
    @FXML private TextField txtPriceFeedSize;

    //Log Tab
    @FXML private TextArea txtLog;

    @FXML protected void initialize(){
        initConfigValues();
        initConfigLineChart();
    }

    private void initConfigValues(){
        //EMA Tab
        txtNValue.setText(TradingBot.fileConfig.getElement("ema","n-value"));
        txtBuyWaitTime.setText(TradingBot.fileConfig.getElement("ema","buy-wait"));
        txtSellWaitTime.setText(TradingBot.fileConfig.getElement("ema","sell-wait"));
        txtCalibrationTime.setText(TradingBot.fileConfig.getElement("ema","calibration-time"));

        //RSI Tab
        txtRSIPeriod.setText(TradingBot.fileConfig.getElement("rsi", "rsi-period"));
        txtRSICalibration.setText(TradingBot.fileConfig.getElement("rsi", "rsi-calibration"));
        txtUpperRSI.setText(TradingBot.fileConfig.getElement("rsi", "upper-bound"));
        txtLowerRSI.setText(TradingBot.fileConfig.getElement("rsi", "lower-bound"));

        //Account Tab
        txtAPI.setText(TradingBot.fileConfig.getElement("account","api-key"));
        txtSecret.setText(TradingBot.fileConfig.getElement("account","secret-key"));
        txtPriceFeedSize.setText(TradingBot.fileConfig.getElement("price-feed", "price-history-size"));
        txtIntervalRate.setText(TradingBot.fileConfig.getElement("price-feed", "interval-rate"));
    }

    private void initConfigLineChart(){
        chartPrice.setAnimated(false);
        chartRSI.setAnimated(false);
        chartEMATab.setAnimated(false);
        chartRSITab.setAnimated(false);

        //EMA Chart

        xAxisPrice.setAutoRanging(false);
        xAxisPrice.setTickUnit(10);
        xAxisPrice.setLowerBound(0);
        xAxisPrice.setUpperBound(30);

        yAxisPrice.setForceZeroInRange(false);
        yAxisPrice.setTickUnit(0.01);

        xAxisEMATab.setAutoRanging(false);
        xAxisEMATab.setTickUnit(10);
        xAxisEMATab.setLowerBound(0);
        xAxisEMATab.setUpperBound(30);

        yAxisEMATab.setForceZeroInRange(false);
        yAxisEMATab.setTickUnit(0.01);

        //RSI Chart

        xAxisRSI.setAutoRanging(false);
        xAxisRSI.setTickUnit(10);
        xAxisRSI.setLowerBound(0);
        xAxisRSI.setUpperBound(30);

        yAxisRSI.setAutoRanging(false);
        yAxisRSI.setTickUnit(20);
        yAxisRSI.setUpperBound(100);
        yAxisRSI.setLowerBound(0);

        xAxisRSITab.setAutoRanging(false);
        xAxisRSITab.setTickUnit(10);
        xAxisRSITab.setLowerBound(0);
        xAxisRSITab.setUpperBound(30);

        yAxisRSITab.setAutoRanging(false);
        yAxisRSITab.setTickUnit(20);
        yAxisRSITab.setUpperBound(100);
        yAxisRSITab.setLowerBound(0);
    }

    private ArrayList<TradingPair> priceHistory;
//    private ArrayList<Data> emaHistory;
//    private ArrayList<Data> rsiHistory;
    private int selectedPair = 0;

//    public void updateOverview(ArrayList<TradingPair> priceFeed, ArrayList<Data> emaFeed, ArrayList<Data> rsiFeed, int selectedPair){
//        this.priceHistory = priceFeed;
//        this.emaHistory = emaFeed;
//        this.rsiHistory = rsiFeed;
//        this.selectedPair = selectedPair;
//
//        if(priceHistory.size() == 0) return;
//
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//
//                updatePriceChart();
//                updateRSIChart();
//                updateState();
//            }
//        });
//
//    }

    /*
    Overview Tab
     */

    public void startTrading(){
        TradingBot.setTrading(true);
    }

    public void stopTrading(){
        TradingBot.setTrading(false);
    }

    public void updateState(){

//        Data currentEMA = emaHistory.get(selectedPair);
//        Data currentRSI = rsiHistory.get(selectedPair);
//
//        lblIndicatorEMA.setText(currentEMA.getStateString());
//        lblIndicatorRSI.setText(currentRSI.getStateString());
    }

    protected void updatePriceChart(){

//        chartPrice.getData().clear();
//        chartEMATab.getData().clear();
//
//        XYChart.Series<Integer, Double> priceData = new XYChart.Series<>();
//        XYChart.Series<Integer, Double> emaData = new XYChart.Series<>();
//
//        XYChart.Series<Integer, Double> priceData2 = new XYChart.Series<>();
//        XYChart.Series<Integer, Double> emaData2 = new XYChart.Series<>();
//
//        priceData.setName("BTCUSDT");
//        priceData2.setName(priceData.getName());
//
//        emaData.setName("EMA");
//        emaData2.setName(emaData.getName());
//
//        for(int i = 0; i < priceHistory.get(selectedPair).getPriceList().size(); i++){
//            priceData.getData().add(new XYChart.Data<>(i ,priceHistory.get(selectedPair).get(i)));
//            emaData.getData().add(new XYChart.Data<>(i ,emaHistory.get(selectedPair).get(i)));
//        }
//
//        if(priceHistory.get(selectedPair).getPriceList().size() > 30){
//            xAxisPrice.setUpperBound(priceHistory.get(selectedPair).getPriceList().size() - 1);
//            xAxisEMATab.setUpperBound(priceHistory.get(selectedPair).getPriceList().size() - 1);
//        }
//
//        priceData2.setData(priceData.getData());
//        emaData2.setData(emaData.getData());
//
//        chartPrice.getData().add(priceData);
//        chartPrice.getData().add(emaData);
//
//        chartEMATab.getData().add(priceData2);
//        chartEMATab.getData().add(emaData2);
//
//        xAxisPrice.autosize();
//        yAxisPrice.autosize();
    }

    protected void updateRSIChart(){

//        chartRSI.getData().clear();
//        chartRSITab.getData().clear();
//
//        XYChart.Series<Integer, Double> rsiData = new XYChart.Series<>();
//
//        XYChart.Series<Integer, Double> rsiData2 = new XYChart.Series<>();
//
//        rsiData.setName("RSI");
//        rsiData2.setName("RSI");
//
//        for(int i = 0; i < rsiHistory.get(selectedPair).getValues().size(); i++){
//            rsiData.getData().add(new XYChart.Data<>(i ,rsiHistory.get(selectedPair).get(i)));
//        }
//
//        if(rsiHistory.get(selectedPair).getValues().size() > 30){
//            xAxisRSI.setUpperBound(rsiHistory.get(selectedPair).getValues().size() - 1);
//            xAxisRSITab.setUpperBound(rsiHistory.get(selectedPair).getValues().size() - 1);
//        }
//
//        rsiData2.setData(rsiData.getData());
//
//        chartRSI.getData().add(rsiData);
//        chartRSITab.getData().add(rsiData2);
//
//
//        //Setting colour of the line
//        Color color = Color.GREEN; // or any other color
//        String rgb = String.format("%d, %d, %d", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
//        Node line = rsiData.getNode().lookup(".chart-series-line");
//        Node line2 = rsiData2.getNode().lookup(".chart-series-line");
//        line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
//        line2.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
    }

    /*
    EMA Tab
     */

    @FXML protected void emaSave(){
        TradingBot.fileConfig.editElement("ema","n-value", Integer.parseInt(txtNValue.getText()));
        TradingBot.fileConfig.editElement("ema","buy-wait", Integer.parseInt(txtBuyWaitTime.getText()));
        TradingBot.fileConfig.editElement("ema","sell-wait", Integer.parseInt(txtSellWaitTime.getText()));
        TradingBot.fileConfig.editElement("ema","calibration-time", Integer.parseInt(txtCalibrationTime.getText()));
    }

    /*
    RSI Tab
     */

    @FXML protected void rsiSave(){
        TradingBot.fileConfig.editElement("rsi", "rsi-period", Integer.parseInt(txtRSIPeriod.getText()));
        TradingBot.fileConfig.editElement("rsi", "rsi-calibration", Integer.parseInt(txtRSICalibration.getText()));
        TradingBot.fileConfig.editElement("rsi", "upper-bound", Integer.parseInt(txtUpperRSI.getText()));
        TradingBot.fileConfig.editElement("rsi", "lower-bound", Integer.parseInt(txtLowerRSI.getText()));
    }

    /*
    Testing Tab
     */

    public void runTests(){
        Tester tester = new Tester();
    }

    /*
    Order Book Tab
     */

    /*
    Settings Tab
     */

    @FXML protected void accountSave(){
        TradingBot.fileConfig.editElement("account","api-key", txtAPI.getText());
        TradingBot.fileConfig.editElement("account","secret-key", txtSecret.getText());
        TradingBot.fileConfig.editElement("price-feed", "price-history-size", Integer.parseInt(txtPriceFeedSize.getText()));
        TradingBot.fileConfig.editElement("price-feed", "interval-rate", Integer.parseInt(txtIntervalRate.getText()));
    }

    /*
    Log Tab
     */
}
