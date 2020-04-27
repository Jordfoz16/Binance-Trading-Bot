package com.jordanfoster.Userinterface;

import com.jordanfoster.BinanceTradingBot;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;

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
    @FXML private LineChart<Integer, Double> chartPrice;
    @FXML private LineChart<Integer, Double> chartRSI;

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

    //Log Tab
    @FXML private TextArea txtLog;

    @FXML protected void initialize(){
        initConfigValues();
    }

    private void initConfigValues(){
        //EMA Tab
        txtNValue.setText(BinanceTradingBot.fileConfig.getElement("ema","n-value"));
        txtBuyWaitTime.setText(BinanceTradingBot.fileConfig.getElement("ema","buy-wait"));
        txtSellWaitTime.setText(BinanceTradingBot.fileConfig.getElement("ema","sell-wait"));
        txtCalibrationTime.setText(BinanceTradingBot.fileConfig.getElement("ema","calibration-time"));

        //RSI Tab
        txtUpperRSI.setText(BinanceTradingBot.fileConfig.getElement("rsi", "upper-bound"));
        txtLowerRSI.setText(BinanceTradingBot.fileConfig.getElement("rsi", "lower-bound"));

        //Account Tab
        txtAPI.setText(BinanceTradingBot.fileConfig.getElement("account","api-key"));
        txtSecret.setText(BinanceTradingBot.fileConfig.getElement("account","secret-key"));

    }

    /*
    Overview Tab
     */

    /*
    EMA Tab
     */

    @FXML protected void emaSave(){
        BinanceTradingBot.fileConfig.editElement("ema","n-value", Integer.parseInt(txtNValue.getText()));
        BinanceTradingBot.fileConfig.editElement("ema","buy-wait", Integer.parseInt(txtBuyWaitTime.getText()));
        BinanceTradingBot.fileConfig.editElement("ema","sell-wait", Integer.parseInt(txtSellWaitTime.getText()));
        BinanceTradingBot.fileConfig.editElement("ema","calibration-time", Integer.parseInt(txtCalibrationTime.getText()));
    }

    /*
    RSI Tab
     */

    @FXML protected void rsiSave(){
        BinanceTradingBot.fileConfig.editElement("rsi", "upper-bound", Integer.parseInt(txtUpperRSI.getText()));
        BinanceTradingBot.fileConfig.editElement("rsi", "lower-bound", Integer.parseInt(txtLowerRSI.getText()));
    }

    /*
    Order Book Tab
     */

    /*
    Account Tab
     */

    @FXML protected void accountSave(){
        BinanceTradingBot.fileConfig.editElement("account","api-key", txtAPI.getText());
        BinanceTradingBot.fileConfig.editElement("account","secret-key", txtSecret.getText());
    }

    /*
    Log Tab
     */
}
