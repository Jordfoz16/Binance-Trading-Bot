package com.jordanfoster.Userinterface;

import com.jordanfoster.TradingBot.Indicators.EMA.EMA;
import com.jordanfoster.TradingBot.Indicators.EMA.TradingPair.TradingPairEMA;
import com.jordanfoster.TradingBot.Indicators.RSI.RSI;
import com.jordanfoster.TradingBot.Indicators.RSI.TradingPair.TradingPairRSI;
import com.jordanfoster.TradingBot.PriceFeed.CandleStick.CandleStickFeed;
import com.jordanfoster.TradingBot.BackTesting.BackTester;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainController{

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
    @FXML private Button btnSaveEMA;

    //RSI Tab
    @FXML private TextField txtRSIPeriod;
    @FXML private TextField txtUpperRSI;
    @FXML private TextField txtLowerRSI;
    @FXML private LineChart<Integer, Double> chartRSITab;
    @FXML private NumberAxis xAxisRSITab;
    @FXML private NumberAxis yAxisRSITab;

    //Testing Tab
    private BackTester backTester;
    @FXML private TextField txtTestStartValue;
    @FXML private Label lblTestStartingDate;
    @FXML private Label lblTestEndingDate;
    @FXML private Label lblDataPoints;
    @FXML private Label lblEMAIndications;
    @FXML private Label lblRSIIndications;
    @FXML private Label lblTestNumberOfTrades;
    @FXML private Label lblTestProfitableTrades;
    @FXML private Label lblTestUnprofitableTrades;
    @FXML private Label lblTestTotalProfit;
    @FXML private Label lblTestLargestProfit;
    @FXML private Label lblTestLargestLoss;
    @FXML private Label lblTestFinalAccount;
    @FXML private TextField txtTestShortPeriod;
    @FXML private TextField txtTestMediumPeriod;
    @FXML private TextField txtTestLongPeriod;
    @FXML private TextField txtTestingRSIPeriod;
    @FXML private TextField txtTestingRSIUpper;
    @FXML private TextField txtTestingRSILower;
    @FXML private TextField txtTestPeriodMin;
    @FXML private TextField txtTestUpperMin;
    @FXML private TextField txtTestLowerMin;
    @FXML private TextField txtTestPeriodMax;
    @FXML private TextField txtTestUpperMax;
    @FXML private TextField txtTestLowerMax;
    @FXML private ComboBox<String> cbTestInterval;
    @FXML private CheckBox checkTestEMA;
    @FXML private CheckBox checkTestRSI;
    @FXML private ProgressBar progressRSIAuto;

    //Practice Tab


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

        cbInterval.getSelectionModel().select(TradingBot.fileConfig.getElement("price-feed", "interval-rate"));
    }

    public void tabChange(){
        //updatePriceChart();
        //updateRSIChart();
    }

    /*
    Overview Tab
     */

    public void updateData(CandleStickFeed candleFeed, EMA emaIndicator, RSI rsiIndicator){
        ChartData chartData = new ChartData(candleFeed, emaIndicator, rsiIndicator);
        Platform.runLater(new ChartUpdateRunnable(chartData));
    }

    private class ChartData{
        private ArrayList<TradingPair> tradingPairs = new ArrayList<TradingPair>();
        private EMA ema = new EMA();
        private RSI rsi = new RSI();
        ChartData(CandleStickFeed candleStickFeed, EMA emaIndicator, RSI rsiIndicator){
            this.tradingPairs = candleStickFeed.getTradingPairs();
            this.ema = emaIndicator;
            this.rsi = rsiIndicator;
        }
    }

    private class ChartUpdateRunnable implements Runnable {
        private ChartData chartData;
        ChartUpdateRunnable(ChartData chartData) {
            this.chartData = chartData;
        }
        @Override
        public void run(){
            updatePriceChart(chartData);
            updateRSIChart(chartData);
        }
    }

    public void updateChartZoom(){
        chartLowerBound = 1000 - Integer.parseInt(cbChartZoom.getValue());
        xAxisPrice.setLowerBound(chartLowerBound);
        xAxisEMATab.setLowerBound(chartLowerBound);
        xAxisRSI.setLowerBound(chartLowerBound);
        xAxisRSITab.setLowerBound(chartLowerBound);
    }

    protected void updatePriceChart(ChartData chartData){
        if(chartData.tradingPairs.size() == 0){
            return;
        }

        if(tabPanel.getSelectionModel().getSelectedIndex() == 0 || tabPanel.getSelectionModel().getSelectedIndex() == 1){

            chartPrice.getData().clear();
            chartEMATab.getData().clear();

            XYChart.Series<Integer, Double> priceData = new XYChart.Series<>();
            XYChart.Series<Integer, Double> emaData = new XYChart.Series<>();
            XYChart.Series<Integer, Double> emaData2 = new XYChart.Series<>();
            XYChart.Series<Integer, Double> emaData3 = new XYChart.Series<>();

            priceData.setName("BTCUSDT");

            emaData.setName("EMA " + chartData.ema.periodShort);
            emaData2.setName("EMA " + chartData.ema.periodMedium);
            emaData3.setName("EMA " + chartData.ema.periodLong);

            for(int i = chartLowerBound; i < chartUpperBound; i++){
                priceData.getData().add(new XYChart.Data<>(i , chartData.tradingPairs.get(selectedPair).getCandleStick(i).close));
                emaData.getData().add(new XYChart.Data<>(i , ((TradingPairEMA) chartData.ema.getIndicator(selectedPair)).getCandleSmall(i).EMA));
                emaData2.getData().add(new XYChart.Data<>(i , ((TradingPairEMA) chartData.ema.getIndicator(selectedPair)).getCandleMed(i).EMA));
                emaData3.getData().add(new XYChart.Data<>(i , ((TradingPairEMA) chartData.ema.getIndicator(selectedPair)).getCandleLarge(i).EMA));
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

    protected void updateRSIChart(ChartData chartData){
        if(chartData.tradingPairs.size() == 0){
            return;
        }
        if(tabPanel.getSelectionModel().getSelectedIndex() == 0 || tabPanel.getSelectionModel().getSelectedIndex() == 2){

            chartRSI.getData().clear();
            chartRSITab.getData().clear();

            XYChart.Series<Integer, Double> rsiData = new XYChart.Series<>();

            rsiData.setName("RSI");

            for(int i = chartLowerBound; i < chartUpperBound; i++){
                rsiData.getData().add(new XYChart.Data<>(i , ((TradingPairRSI) chartData.rsi.getIndicator(selectedPair)).getCandle(i).RSI));
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

    /*
    EMA Tab
     */

    @FXML protected void emaSave(){
        TradingBot.fileConfig.editElement("ema","short-period-value", Integer.parseInt(txtShortPeriod.getText()));
        TradingBot.fileConfig.editElement("ema","medium-period-value", Integer.parseInt(txtMediumPeriod.getText()));
        TradingBot.fileConfig.editElement("ema","long-period-value", Integer.parseInt(txtLongPeriod.getText()));
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

    public void initBackTester(){
        backTester = new BackTester();
    }

    public void initTesting(){
        cbTestInterval.getItems().add("1m");
        cbTestInterval.getItems().add("3m");
        cbTestInterval.getItems().add("5m");
        cbTestInterval.getItems().add("15m");
        cbTestInterval.getItems().add("30m");
        cbTestInterval.getItems().add("1h");
        cbTestInterval.getItems().add("2h");
        cbTestInterval.getItems().add("4h");

        loadValues();
    }

    public void loadValues(){
        txtTestingRSIPeriod.setText(TradingBot.fileConfig.getElement("rsi", "rsi-period"));
        txtTestingRSIUpper.setText(TradingBot.fileConfig.getElement("rsi", "upper-bound"));
        txtTestingRSILower.setText(TradingBot.fileConfig.getElement("rsi", "lower-bound"));

        txtTestShortPeriod.setText(TradingBot.fileConfig.getElement("ema", "short-period-value"));
        txtTestMediumPeriod.setText(TradingBot.fileConfig.getElement("ema", "medium-period-value"));
        txtTestLongPeriod.setText(TradingBot.fileConfig.getElement("ema", "long-period-value"));

        cbTestInterval.getSelectionModel().select(TradingBot.fileConfig.getElement("price-feed", "interval-rate"));
    }

    public void updateRSIValues(){
        int period = Integer.parseInt(txtTestingRSIPeriod.getText());
        int upper = Integer.parseInt(txtTestingRSIUpper.getText());
        int lower = Integer.parseInt(txtTestingRSILower.getText());

        backTester.setRSIValues(period, upper, lower);
    }

    public void updateEMAValues(){
        int shortPeriod = Integer.parseInt(txtTestShortPeriod.getText());
        int mediumPeriod = Integer.parseInt(txtTestMediumPeriod.getText());
        int longPeriod = Integer.parseInt(txtTestLongPeriod.getText());

        backTester.setEMAValue(shortPeriod, mediumPeriod, longPeriod);
    }

    public void updateBackTestValues(){
        backTester.setStartValue(Integer.parseInt(txtTestStartValue.getText()));
        backTester.setInterval(cbTestInterval.getValue());
    }

    public void runTests(){
        updateBackTestValues();
        updateRSIValues();
        updateEMAValues();
        backTester.updatePriceFeed();
        backTester.run();
        updateResults(backTester);

        Thread thread = new Thread("Back Testing") {
            public void run() {

            }
        };
    }

    public void autoRSIValue(){

        Thread thread = new Thread("RSI Auto") {
            public void run(){

                updateBackTestValues();
                updateEMAValues();
                backTester.updatePriceFeed();

                int rsiMinValue = Integer.parseInt(txtTestPeriodMin.getText());
                int rsiMaxValue = Integer.parseInt(txtTestPeriodMax.getText());

                int rsiUpperMax = Integer.parseInt(txtTestUpperMax.getText());
                int rsiUpperMin = Integer.parseInt(txtTestUpperMin.getText());

                int rsiLowerMax = Integer.parseInt(txtTestLowerMax.getText());
                int rsiLowerMin = Integer.parseInt(txtTestLowerMin.getText());

                double profit = 0;
                int bestPeriodValue = 1;
                int bestUpper = 1;
                int bestLower = 1;

                int counter = 0;

                for(int rsiIndex = rsiMinValue; rsiIndex < rsiMaxValue; rsiIndex++){
                    for(int rsiUpper = rsiUpperMin; rsiUpper < rsiUpperMax; rsiUpper++){
                        for(int rsiLower = rsiLowerMin; rsiLower < rsiLowerMax; rsiLower++){
                            backTester.setRSIValues(rsiIndex, rsiUpper, rsiLower);
                            backTester.run();

                            //Update the values
                            counter++;
                            double progress = (double)counter / (double)((rsiMaxValue - rsiMinValue) * (rsiUpperMax - rsiUpperMin) * (rsiLowerMax - rsiLowerMin));
                            updateAutoRSIProgress(progress);
                            updateAutoRSIValues(rsiIndex, rsiUpper, rsiLower);

                            if(backTester.profit > profit){
                                profit = backTester.profit;
                                bestPeriodValue = rsiIndex;
                                bestUpper = rsiUpper;
                                bestLower = rsiLower;
                            }
                        }
                    }
                }
                //Run the best results
                backTester.setRSIValues(bestPeriodValue, bestUpper, bestLower);
                backTester.run();
                updateResults(backTester);

                txtTestingRSIPeriod.setText(Integer.toString(bestPeriodValue));
                txtTestingRSIUpper.setText(Integer.toString(bestUpper));
                txtTestingRSILower.setText(Integer.toString(bestLower));
            }
        };

        thread.start();
    }

    public void updateAutoRSIValues(int period, int upper, int lower){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                txtTestingRSIPeriod.setText(Integer.toString(period));
                txtTestingRSIUpper.setText(Integer.toString(upper));
                txtTestingRSILower.setText(Integer.toString(lower));
            }
        });
    }

    public void updateAutoRSIProgress(double progress){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                progressRSIAuto.setProgress(progress);
            }
        });
    }

    public void updateResults(BackTester backTester){

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                lblTestStartingDate.setText(getDate(backTester.startTime));
                lblTestEndingDate.setText(getDate(backTester.endTime));
                lblDataPoints.setText(Integer.toString(backTester.dataPoints));
                lblTestNumberOfTrades.setText(Integer.toString(backTester.numberOfTrades));
                lblTestTotalProfit.setText(Double.toString(backTester.profit));
                lblTestProfitableTrades.setText(Integer.toString(backTester.numberOfProfitable));
                lblTestUnprofitableTrades.setText(Integer.toString(backTester.numberOfUnprofitable));
                lblTestLargestProfit.setText(Double.toString(backTester.largestProfit));
                lblTestLargestLoss.setText(Double.toString(backTester.largestLoss));
                lblRSIIndications.setText(Integer.toString(backTester.rsiIndication));
                lblEMAIndications.setText(Integer.toString(backTester.emaIndication));
                lblTestFinalAccount.setText(Double.toString(backTester.accountValue));
            }
        });
    }

    public void saveSetting(){

        //Interval Rate
        TradingBot.fileConfig.editElement("price-feed", "interval-rate", cbTestInterval.getValue());

        //RSI Edits
        TradingBot.fileConfig.editElement("rsi", "rsi-period", Integer.parseInt(txtTestingRSIPeriod.getText()));
        TradingBot.fileConfig.editElement("rsi", "upper-bound", Integer.parseInt(txtTestingRSIUpper.getText()));
        TradingBot.fileConfig.editElement("rsi", "lower-bound", Integer.parseInt(txtTestingRSILower.getText()));

        //EMA Edits
        TradingBot.fileConfig.editElement("ema","short-period-value", Integer.parseInt(txtTestShortPeriod.getText()));
        TradingBot.fileConfig.editElement("ema","medium-period-value", Integer.parseInt(txtTestMediumPeriod.getText()));
        TradingBot.fileConfig.editElement("ema","long-period-value", Integer.parseInt(txtTestLongPeriod.getText()));

        initConfigValues();
        cbInterval.getSelectionModel().select(TradingBot.fileConfig.getElement("price-feed", "interval-rate"));
    }

    public String getDate(long unixTimestamp){
        Date date = new Date(unixTimestamp);

        SimpleDateFormat format = new SimpleDateFormat("dd-MM, HH:mm");
        String formattedDate = format.format(date);

        return formattedDate;
    }

    /*
    Practice Tab
     */

    public void startPractice(){
        TradingBot.practiceAccount.isTrading = true;
    }

    public void stopPractice(){
        TradingBot.practiceAccount.isTrading = false;
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


