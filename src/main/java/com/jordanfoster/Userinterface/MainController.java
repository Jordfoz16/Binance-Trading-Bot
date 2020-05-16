package com.jordanfoster.Userinterface;

import com.jordanfoster.BinanceTradingBot;
import com.jordanfoster.TradingBot.Indicators.EMA.EMAValue;
import com.jordanfoster.TradingBot.Indicators.RSI.RSIValue;
import com.jordanfoster.TradingBot.PriceFeed.TradingPair;
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

    //Account Tab - Config
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
    }

    private void initConfigLineChart(){
        chartPrice.setAnimated(false);
        chartRSI.setAnimated(false);

        xAxisPrice.setAutoRanging(false);
        xAxisPrice.setTickUnit(10);
        xAxisPrice.setLowerBound(0);
        xAxisPrice.setUpperBound(30);

        yAxisPrice.setForceZeroInRange(false);
        yAxisPrice.setTickUnit(0.01);

        xAxisRSI.setAutoRanging(false);
        xAxisRSI.setTickUnit(10);
        xAxisRSI.setLowerBound(0);
        xAxisRSI.setUpperBound(30);

        yAxisRSI.setAutoRanging(false);
        yAxisRSI.setTickUnit(20);
        yAxisRSI.setUpperBound(100);
        yAxisRSI.setLowerBound(0);
    }

    private ArrayList<TradingPair> priceHistory;
    private ArrayList<EMAValue> emaHistory;
    private ArrayList<RSIValue> rsiHistory;
    private int selectedPair = 0;

    public void updateOverview(ArrayList<TradingPair> priceFeed, ArrayList<EMAValue> emaFeed, ArrayList<RSIValue> rsiFeed, int selectedPair){
        this.priceHistory = priceFeed;
        this.emaHistory = emaFeed;
        this.rsiHistory = rsiFeed;
        this.selectedPair = selectedPair;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                updatePriceChart();
                updateRSIChart();
                updateState();
            }
        });

    }

    /*
    Overview Tab
     */

    public void updateState(){

        EMAValue currentEMA = emaHistory.get(selectedPair);
        RSIValue currentRSI = rsiHistory.get(selectedPair);

        lblIndicatorEMA.setText(currentEMA.getStateString());
        lblIndicatorRSI.setText(currentRSI.getStateString());
    }

    public void updatePriceChart(){

        chartPrice.getData().clear();

        XYChart.Series<Integer, Double> priceData = new XYChart.Series<>();
        XYChart.Series<Integer, Double> emaData = new XYChart.Series<>();

        priceData.setName("BTCUSDT");
        emaData.setName("EMA");

        for(int i = 0; i < priceHistory.get(selectedPair).getPriceList().size(); i++){
            priceData.getData().add(new XYChart.Data<>(i ,priceHistory.get(selectedPair).get(i)));
            emaData.getData().add(new XYChart.Data<>(i ,emaHistory.get(selectedPair).get(i)));
        }

        if(priceHistory.get(selectedPair).getPriceList().size() > 30){
            xAxisPrice.setUpperBound(priceHistory.get(selectedPair).getPriceList().size() - 1);
        }

        chartPrice.getData().add(priceData);
        chartPrice.getData().add(emaData);

        xAxisPrice.autosize();
        yAxisPrice.autosize();
    }

    public void updateRSIChart(){

        chartRSI.getData().clear();

        XYChart.Series<Integer, Double> rsiData = new XYChart.Series<>();

        rsiData.setName("RSI");

        for(int i = 0; i < rsiHistory.get(selectedPair).getRsiValues().size(); i++){
            rsiData.getData().add(new XYChart.Data<>(i ,rsiHistory.get(selectedPair).get(i)));
        }

        if(rsiHistory.get(selectedPair).getRsiValues().size() > 30){
            xAxisRSI.setUpperBound(rsiHistory.get(selectedPair).getRsiValues().size() - 1);
        }

        chartRSI.getData().add(rsiData);

        //Setting colour of the line
        Color color = Color.GREEN; // or any other color
        String rgb = String.format("%d, %d, %d", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
        Node line = rsiData.getNode().lookup(".chart-series-line");
        line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
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
    Order Book Tab
     */

    /*
    Account Tab
     */

    @FXML protected void accountSave(){
        TradingBot.fileConfig.editElement("account","api-key", txtAPI.getText());
        TradingBot.fileConfig.editElement("account","secret-key", txtSecret.getText());
        TradingBot.fileConfig.editElement("price-feed", "price-history-size", Integer.parseInt(txtPriceFeedSize.getText()));
    }

    /*
    Log Tab
     */
}
