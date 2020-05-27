package com.jordanfoster.Userinterface;

import com.jordanfoster.TradingBot.Indicators.EMA.EMA;
import com.jordanfoster.TradingBot.Indicators.EMA.TradingPair.TradingPairEMA;
import com.jordanfoster.TradingBot.Indicators.RSI.TradingPair.TradingPairRSI;
import com.jordanfoster.TradingBot.Indicators.TradingPairIndicator;
import com.jordanfoster.TradingBot.Orderbook.OrderBook;
import com.jordanfoster.TradingBot.PriceFeed.TradingPair;
import com.jordanfoster.TradingBot.BackTesting.BackTester;
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

    @FXML private TabPane tabPanel;

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
    @FXML private ComboBox<String> cbChartZoom;

    //Overview Tab - Charts
    private int chartUpperBound = 1000;
    private int chartLowerBound = 800;
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
    @FXML private TextField txtShortPeriod;
    @FXML private TextField txtMediumPeriod;
    @FXML private TextField txtLongPeriod;
    @FXML private TextField txtBuyWaitTime;
    @FXML private TextField txtSellWaitTime;
    @FXML private Button btnSaveEMA;

    //RSI Tab
    @FXML private TextField txtRSIPeriod;
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
    @FXML private ComboBox<String> cbTestInterval;

    //Order Book Tab
    @FXML private TextField txtTrades;
    @FXML private TableView tableOrderBook;

    //Setting Tab
    @FXML private TextField txtAPI;
    @FXML private TextField txtSecret;
    @FXML private Button btnSaveAPI;
    @FXML private Label lblConnection;
    @FXML private Label lblWalletConnection;
    @FXML private Label lblPriceConnection;
    @FXML private Button btnNetworkTest;
    @FXML private TextField txtIntervalRate;
    @FXML private ComboBox<String> cbInterval;

    //Log Tab
    @FXML private TextArea txtLog;

    //Selected Coin
    private int selectedPair = 0;

    //Chart Data
    private ArrayList<TradingPair> priceHistory = new ArrayList<>();
    private ArrayList<TradingPairIndicator> emaHistory = new ArrayList<>();
    private ArrayList<TradingPairIndicator> rsiHistory = new ArrayList<>();

    @FXML protected void initialize(){
        initConfigValues();
        initConfigLineChart();
        initTradingPairSelector();
        initInterval();
        initTesting();
    }

    private void initConfigValues(){
        //EMA Tab
        txtShortPeriod.setText(TradingBot.fileConfig.getElement("ema","short-period-value"));
        txtMediumPeriod.setText(TradingBot.fileConfig.getElement("ema","medium-period-value"));
        txtLongPeriod.setText(TradingBot.fileConfig.getElement("ema","long-period-value"));
        txtBuyWaitTime.setText(TradingBot.fileConfig.getElement("ema","buy-wait"));
        txtSellWaitTime.setText(TradingBot.fileConfig.getElement("ema","sell-wait"));

        //RSI Tab
        txtRSIPeriod.setText(TradingBot.fileConfig.getElement("rsi", "rsi-period"));
        txtUpperRSI.setText(TradingBot.fileConfig.getElement("rsi", "upper-bound"));
        txtLowerRSI.setText(TradingBot.fileConfig.getElement("rsi", "lower-bound"));

        //Account Tab
        txtAPI.setText(TradingBot.fileConfig.getElement("account","api-key"));
        txtSecret.setText(TradingBot.fileConfig.getElement("account","secret-key"));
        txtIntervalRate.setText(TradingBot.fileConfig.getElement("price-feed", "polling-rate"));
    }

    private void initConfigLineChart(){
        chartPrice.setAnimated(false);
        chartRSI.setAnimated(false);
        chartEMATab.setAnimated(false);
        chartRSITab.setAnimated(false);

        //EMA Chart

        xAxisPrice.setAutoRanging(false);
        xAxisPrice.setTickUnit(10);
        xAxisPrice.setLowerBound(chartLowerBound);
        xAxisPrice.setUpperBound(chartUpperBound);

        yAxisPrice.setForceZeroInRange(false);
        yAxisPrice.setTickUnit(20);

        xAxisEMATab.setAutoRanging(false);
        xAxisEMATab.setTickUnit(10);
        xAxisEMATab.setLowerBound(chartLowerBound);
        xAxisEMATab.setUpperBound(chartUpperBound);

        yAxisEMATab.setForceZeroInRange(false);
        yAxisEMATab.setTickUnit(20);

        //RSI Chart

        xAxisRSI.setAutoRanging(false);
        xAxisRSI.setTickUnit(10);
        xAxisRSI.setLowerBound(chartLowerBound);
        xAxisRSI.setUpperBound(chartUpperBound);

        yAxisRSI.setAutoRanging(false);
        yAxisRSI.setTickUnit(20);
        yAxisRSI.setUpperBound(100);
        yAxisRSI.setLowerBound(0);

        xAxisRSITab.setAutoRanging(false);
        xAxisRSITab.setTickUnit(10);
        xAxisRSITab.setLowerBound(chartLowerBound);
        xAxisRSITab.setUpperBound(chartUpperBound);

        yAxisRSITab.setAutoRanging(false);
        yAxisRSITab.setTickUnit(20);
        yAxisRSITab.setUpperBound(100);
        yAxisRSITab.setLowerBound(0);
    }

    private void initTradingPairSelector(){
        for(int i = 0; i < TradingBot.fileTradingPairs.getAvailableTradingPairs().size(); i++){
            cbTradingPairs.getItems().add(TradingBot.fileTradingPairs.getSymbol(i));
        }

        cbTradingPairs.getSelectionModel().selectFirst();

        for(int i = 1; i <= 20; i++){
            cbChartZoom.getItems().add(Integer.toString(50 * i));
        }

        cbChartZoom.getSelectionModel().select("200");

    }

    private void initInterval(){
        cbInterval.getItems().add("1m");
        cbInterval.getItems().add("3m");
        cbInterval.getItems().add("5m");
        cbInterval.getItems().add("15m");
        cbInterval.getItems().add("30m");
        cbInterval.getItems().add("1h");
        cbInterval.getItems().add("2h");
        cbInterval.getItems().add("4h");
        cbInterval.getItems().add("6h");
        cbInterval.getItems().add("8h");
        cbInterval.getItems().add("12h");
        cbInterval.getItems().add("1d");
        cbInterval.getItems().add("3d");
        cbInterval.getItems().add("1w");
        cbInterval.getItems().add("1M");

        cbInterval.getSelectionModel().select(TradingBot.fileConfig.getElement("price-feed", "interval-rate"));
    }

    public void tabChange(){
        updatePriceChart();
        updateRSIChart();
    }

    public synchronized void updateOverview(ArrayList<TradingPair> priceFeed, ArrayList<TradingPairIndicator> emaFeed, ArrayList<TradingPairIndicator> rsiFeed){

        priceHistory.clear();
        priceHistory.addAll(priceFeed);

        emaHistory.clear();
        emaHistory.addAll(emaFeed);

        rsiHistory.clear();
        rsiHistory.addAll(rsiFeed);

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

    public void updateSelectedPair(){
        selectedPair = cbTradingPairs.getSelectionModel().getSelectedIndex();
    }

    public void updateChartZoom(){
        chartLowerBound = 1000 - Integer.parseInt(cbChartZoom.getValue());
        xAxisPrice.setLowerBound(chartLowerBound);
        xAxisEMATab.setLowerBound(chartLowerBound);
        xAxisRSI.setLowerBound(chartLowerBound);
        xAxisRSITab.setLowerBound(chartLowerBound);
    }

    protected void updatePriceChart(){
        if(tabPanel.getSelectionModel().getSelectedIndex() == 0 || tabPanel.getSelectionModel().getSelectedIndex() == 1){

            chartPrice.getData().clear();
            chartEMATab.getData().clear();

            XYChart.Series<Integer, Double> priceData = new XYChart.Series<>();
            XYChart.Series<Integer, Double> emaData = new XYChart.Series<>();
            XYChart.Series<Integer, Double> emaData2 = new XYChart.Series<>();
            XYChart.Series<Integer, Double> emaData3 = new XYChart.Series<>();

            priceData.setName("BTCUSDT");

            emaData.setName("EMA " + EMA.periodShort);
            emaData2.setName("EMA " + EMA.periodMedium);
            emaData3.setName("EMA " + EMA.periodLong);

            for(int i = chartLowerBound; i < chartUpperBound; i++){
                priceData.getData().add(new XYChart.Data<>(i ,priceHistory.get(selectedPair).getCandleStick(i).close));
                emaData.getData().add(new XYChart.Data<>(i , ((TradingPairEMA) emaHistory.get(selectedPair)).getCandleSmall(i).EMA));
                emaData2.getData().add(new XYChart.Data<>(i , ((TradingPairEMA) emaHistory.get(selectedPair)).getCandleMed(i).EMA));
                emaData3.getData().add(new XYChart.Data<>(i , ((TradingPairEMA) emaHistory.get(selectedPair)).getCandleLarge(i).EMA));

            }

            if(tabPanel.getSelectionModel().getSelectedIndex() == 0){
                chartPrice.getData().add(priceData);
                chartPrice.getData().add(emaData);
                chartPrice.getData().add(emaData2);
                chartPrice.getData().add(emaData3);
            }else if (tabPanel.getSelectionModel().getSelectedIndex() == 1) {
                chartEMATab.getData().add(priceData);
                chartEMATab.getData().add(emaData);
                chartEMATab.getData().add(emaData2);
                chartEMATab.getData().add(emaData3);
            }
        }
        yAxisPrice.autosize();
    }

    protected void updateRSIChart(){
        if(tabPanel.getSelectionModel().getSelectedIndex() == 0 || tabPanel.getSelectionModel().getSelectedIndex() == 2){

            chartRSI.getData().clear();
            chartRSITab.getData().clear();

            XYChart.Series<Integer, Double> rsiData = new XYChart.Series<>();

            rsiData.setName("RSI");

            for(int i = chartLowerBound; i < chartUpperBound; i++){
                rsiData.getData().add(new XYChart.Data<>(i , ((TradingPairRSI) rsiHistory.get(selectedPair)).getCandle(i).RSI));
            }

                if(tabPanel.getSelectionModel().getSelectedIndex() == 0){
                    chartRSI.getData().add(rsiData);
                }else if(tabPanel.getSelectionModel().getSelectedIndex() == 2){
                    chartRSITab.getData().add(rsiData);
                }
                //Setting colour of the line
                Color color = Color.GREEN; // or any other color
                String rgb = String.format("%d, %d, %d", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
                Node line = rsiData.getNode().lookup(".chart-series-line");
                line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
            }
    }

    /*
    EMA Tab
     */

    @FXML protected void emaSave(){
        TradingBot.fileConfig.editElement("ema","short-period-value", Integer.parseInt(txtShortPeriod.getText()));
        TradingBot.fileConfig.editElement("ema","medium-period-value", Integer.parseInt(txtMediumPeriod.getText()));
        TradingBot.fileConfig.editElement("ema","long-period-value", Integer.parseInt(txtLongPeriod.getText()));
        TradingBot.fileConfig.editElement("ema","buy-wait", Integer.parseInt(txtBuyWaitTime.getText()));
        TradingBot.fileConfig.editElement("ema","sell-wait", Integer.parseInt(txtSellWaitTime.getText()));
    }

    /*
    RSI Tab
     */

    @FXML protected void rsiSave(){
        TradingBot.fileConfig.editElement("rsi", "rsi-period", Integer.parseInt(txtRSIPeriod.getText()));
        TradingBot.fileConfig.editElement("rsi", "upper-bound", Integer.parseInt(txtUpperRSI.getText()));
        TradingBot.fileConfig.editElement("rsi", "lower-bound", Integer.parseInt(txtLowerRSI.getText()));
    }

    /*
    Testing Tab
     */

    BackTester backTester = new BackTester();

    public void initTesting(){
        txtTestingRSIPeriod.setText(TradingBot.fileConfig.getElement("rsi", "rsi-period"));
        txtTestingRSIUpper.setText(TradingBot.fileConfig.getElement("rsi", "upper-bound"));
        txtTestingRSILower.setText(TradingBot.fileConfig.getElement("rsi", "lower-bound"));

        cbTestInterval.getItems().add("1m");
        cbTestInterval.getItems().add("3m");
        cbTestInterval.getItems().add("5m");
        cbTestInterval.getItems().add("15m");
        cbTestInterval.getItems().add("30m");
        cbTestInterval.getItems().add("1h");
        cbTestInterval.getItems().add("2h");
        cbTestInterval.getItems().add("4h");
        cbTestInterval.getItems().add("6h");
        cbTestInterval.getItems().add("8h");
        cbTestInterval.getItems().add("12h");
        cbTestInterval.getItems().add("1d");
        cbTestInterval.getItems().add("3d");
        cbTestInterval.getItems().add("1w");
        cbTestInterval.getItems().add("1M");

        cbTestInterval.getSelectionModel().select(TradingBot.fileConfig.getElement("price-feed", "interval-rate"));

        updateRSIValues();
    }

    public void updateRSIValues(){
        int period = Integer.parseInt(txtTestingRSIPeriod.getText());
        int upper = Integer.parseInt(txtTestingRSIUpper.getText());
        int lower = Integer.parseInt(txtTestingRSILower.getText());

        backTester.setRSIValues(period, upper, lower);
        backTester.setInterval(cbTestInterval.getValue());
    }

    public void runTests(){
        updateRSIValues();
        backTester.run();
        updateBackTest();
    }

    public void  updateBackTest(){
        lblTestNumberOfTrades.setText(Integer.toString(backTester.numberOfTrades));
        lblTestTotalProfit.setText(Double.toString(backTester.profit));
        lblTestProfitableTrades.setText(Integer.toString(backTester.numberOfProfitable));
        lblTestUnprofitableTrades.setText(Integer.toString(backTester.numberOfUnprofitable));
        lblTestLargestProfit.setText(Double.toString(backTester.largestProfit));
        lblTestLargestLoss.setText(Double.toString(backTester.largestLoss));
        lblRSIIndications.setText(Integer.toString(backTester.rsiIndication));
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
        TradingBot.fileConfig.editElement("price-feed", "polling-rate", Integer.parseInt(txtIntervalRate.getText()));
        TradingBot.fileConfig.editElement("price-feed", "interval-rate", cbInterval.getValue());
    }

    /*
    Log Tab
     */
}
