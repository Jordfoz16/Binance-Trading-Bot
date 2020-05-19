package com.jordanfoster.TradingBot.Indicators.RSI;

import com.jordanfoster.TradingBot.PriceFeed.PriceFeed;
import com.jordanfoster.TradingBot.PriceFeed.TradingPair;
import com.jordanfoster.TradingBot.TradingBot;

import java.util.ArrayList;

public class RSI {

    public int rsiPeriod = 600;
    private int calibrationTime = 30;
    private int calibrationCounter = 0;
    private int rsiUpperBound = 60;
    private int rsiLowerBound = 40;

    private ArrayList<RSIValue> rsiValues = new ArrayList<RSIValue>();
    private boolean initialized = false;

    public RSI(){
        refreshValues();
    }

    public void refreshValues(){
        int currentPeriod = rsiPeriod;

        rsiPeriod = Integer.parseInt(TradingBot.fileConfig.getElement("rsi", "rsi-period"));
        calibrationTime = Integer.parseInt(TradingBot.fileConfig.getElement("rsi", "rsi-calibration"));
        rsiUpperBound = Integer.parseInt(TradingBot.fileConfig.getElement("rsi", "upper-bound"));
        rsiLowerBound = Integer.parseInt(TradingBot.fileConfig.getElement("rsi", "lower-bound"));

        if(rsiPeriod != currentPeriod){
            periodChanged();
        }
    }

    public void update(){
        refreshValues();

        for(int index = 0; index < TradingBot.priceFeed.getTradingPairs().size(); index++){
            calculateRSI(index, null);
            updateState(index);
        }

        initialized = true;
    }

    private void updateState(int index){
        RSIValue currentCoin = rsiValues.get(index);

        if(calibrationTime <= calibrationCounter){

            if(currentCoin.getCurrent() > rsiUpperBound){
                currentCoin.setState(TradingBot.State.BUY);
            }else if(currentCoin.getCurrent() < rsiLowerBound){
                currentCoin.setState(TradingBot.State.SELL);
            }else{
                currentCoin.setState(TradingBot.State.HOLD);
            }

        }else{
            currentCoin.setState(TradingBot.State.CALIBRATION);
            calibrationCounter++;
        }
    }

    private void calculateRSI(int index, TradingPair tradingPair){

        TradingPair currentPair = TradingBot.priceFeed.getTradingPair(index);

        if(tradingPair != null){
            currentPair = tradingPair;
        }

        if(initialized){

            RSIValue currentRSI = rsiValues.get(index);

            if(currentPair.getPriceList().size() > rsiPeriod){
                //If there is enough data to use the full rsi period
                //Uses the second step RSI calculation

                double startingValue = currentPair.get(currentPair.getPriceList().size() - rsiPeriod - 1);

                double averageGain = calculateAverage(findGain(startingValue, currentPair));
                double averageLosses = calculateAverage(findLosses(startingValue, currentPair));

                double rsiValue = calculateStepTwoRSI(currentRSI.getPrevAverageGains(), currentRSI.getPrevAverageLosses(), averageGain, averageLosses);

                currentRSI.addRSIValue(rsiValue);
                currentRSI.setPrevAverage(averageGain, averageLosses);

            }else{
                //If there isn't enough data to use the full rsi period
                //Uses the first step RSI calculation

                double startingValue = currentPair.get(0);

                double averageGain = calculateAverage(findGain(startingValue, currentPair));
                double averageLosses = calculateAverage(findLosses(startingValue, currentPair));

                double rsiValue = calculateStepOneRSI(averageGain, averageLosses);

                currentRSI.addRSIValue(rsiValue);
                currentRSI.setPrevAverage(averageGain, averageLosses);
            }

        }else{
            rsiValues.add(new RSIValue());
            rsiValues.get(rsiValues.size() - 1).addRSIValue(50);
        }
    }

    private void periodChanged(){

        //Cycles through the the price feed and calculates new RSI values depending on the new period

        PriceFeed tempPriceFeed = new PriceFeed();

        for(int index = 0; index < TradingBot.priceFeed.getTradingPairs().size(); index++){
            RSIValue currentRSI = rsiValues.get(index);

            currentRSI.getRsiValues().clear();
            currentRSI.addRSIValue(50);


            tempPriceFeed.tradingPairs.add(new TradingPair(TradingBot.priceFeed.getTradingPair(index).getSymbol(), TradingBot.priceFeed.getTradingPair(index).get(0)));

            for(int i = 0; i < TradingBot.priceFeed.getTradingPair(index).getPriceList().size(); i++){
                tempPriceFeed.getTradingPair(index).addPrice(TradingBot.priceFeed.getTradingPair(index).get(i));
                calculateRSI(index, tempPriceFeed.getTradingPair(index));
            }
        }
    }

    private double calculateStepOneRSI(double averageGains, double averageLosses){
        double rsi = 0.0;

        if(Double.isNaN(averageGains)) averageGains = 0;
        if(Double.isNaN(averageLosses)) averageLosses = 0;

        rsi = 100.0 - (100.0 / (1.0 + ((averageGains / rsiPeriod) / (averageLosses / rsiPeriod))));

        return rsi;
    }

    private double calculateStepTwoRSI(double prevAverageGains, double prevAverageLosses, double averageGains, double averageLosses){

        double rsi = 0.0;

        if(Double.isNaN(averageGains)) averageGains = 0;
        if(Double.isNaN(averageLosses)) averageLosses = 0;
        if(Double.isNaN(prevAverageGains)) prevAverageGains = 0;
        if(Double.isNaN(prevAverageLosses)) prevAverageLosses = 0;

        rsi = 100.0 - (100.0 / (1.0 + ((prevAverageGains * 13 + averageGains) / (prevAverageLosses * 13 + averageLosses))));

        return rsi;
    }
    private double calculateAverage(ArrayList<Double> list){
        double average = 0;

        for(int i = 0; i < list.size(); i++){
            average += list.get(i);
        }

        average = average / (double) list.size();

        return average;
    }

    private ArrayList<Double> findGain(double startingValue, TradingPair currentPair){
        ArrayList<Double> gains = new ArrayList<Double>();

        int period = 0;

        if(currentPair.getPriceList().size() < rsiPeriod){
            period = currentPair.getPriceList().size();
        }else{
            period = rsiPeriod;
        }

        for(int i = 0; i < period; i++){
            double indexPrice = currentPair.get(currentPair.getPriceList().size() - i - 1);

            if(indexPrice > startingValue){
                double gain = ((indexPrice / startingValue) - 1.0) * 100.0;
                gains.add(gain);
            }
        }

        return gains;
    }

    private ArrayList<Double> findLosses(double startingValue, TradingPair currentPair){
        ArrayList<Double> losses = new ArrayList<Double>();

        int period = 0;

        if(currentPair.getPriceList().size() < rsiPeriod){
            period = currentPair.getPriceList().size();
        }else{
            period = rsiPeriod;
        }

        for(int i = 0; i < period; i++){
            double indexPrice = currentPair.get(currentPair.getPriceList().size() - i - 1);

            if(indexPrice < startingValue){
                double loss = ((startingValue / indexPrice) - 1.0) * 100.0;
                losses.add(loss);
            }
        }

        return losses;
    }

    public void clear(){
        rsiValues.clear();
        calibrationCounter = 0;
        initialized = false;
    }

    public ArrayList<RSIValue> getRsiValues(){
        return rsiValues;
    }
}
