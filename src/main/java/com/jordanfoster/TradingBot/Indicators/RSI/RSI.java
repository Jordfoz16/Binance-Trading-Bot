package com.jordanfoster.TradingBot.Indicators.RSI;

import com.jordanfoster.TradingBot.PriceFeed.TradingPair;
import com.jordanfoster.TradingBot.TradingBot;

import java.util.ArrayList;

public class RSI {

    public static int rsiPeriod = 600;
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
        rsiPeriod = Integer.parseInt(TradingBot.fileConfig.getElement("rsi", "rsi-period"));
        calibrationTime = Integer.parseInt(TradingBot.fileConfig.getElement("rsi", "rsi-calibration"));
        rsiUpperBound = Integer.parseInt(TradingBot.fileConfig.getElement("rsi", "upper-bound"));
        rsiLowerBound = Integer.parseInt(TradingBot.fileConfig.getElement("rsi", "lower-bound"));
    }

    public void update(){
        refreshValues();

        for(int index = 0; index < TradingBot.priceFeed.getTradingPairs().size(); index++){
            calculateRSI(index);
            updateState(index);
        }
    }

    private void updateState(int index){
        RSIValue currentCoin = rsiValues.get(index);

        if(calibrationTime <= calibrationCounter){

            if(currentCoin.getCurrent() > rsiUpperBound){
                currentCoin.setState(RSIValue.State.BUY);
            }else if(currentCoin.getCurrent() < rsiLowerBound){
                currentCoin.setState(RSIValue.State.SELL);
            }else{
                currentCoin.setState(RSIValue.State.HOLD);
            }

        }else{
            currentCoin.setState(RSIValue.State.CALIBRATION);
            calibrationCounter++;
        }
    }

    private void calculateRSI(int index){
        TradingPair currentPair = TradingBot.priceFeed.getTradingPair(index);

        ArrayList<Double> gains = new ArrayList<Double>();
        ArrayList<Double> losses = new ArrayList<Double>();

        gains.clear();
        losses.clear();

        if(initialized){

            RSIValue currentRSI = rsiValues.get(index);

            if(currentPair.getPriceList().size() > rsiPeriod){
                //If there is enough data to use the full rsi period
                //Uses the second step RSI calculation

                double startingValue = currentPair.get(currentPair.getPriceList().size() - rsiPeriod - 1);

                for(int i = 0; i < rsiPeriod; i++){
                    double indexPrice = currentPair.get(currentPair.getPriceList().size() - i - 1);

                    if(indexPrice > startingValue){
                        double gain = ((indexPrice / startingValue) - 1.0) * 100.0;
                        gains.add(gain);
                    }else if(indexPrice < startingValue){
                        double loss = ((startingValue / indexPrice) - 1.0) * 100.0;
                        losses.add(loss);
                    }
                }

                double averageGain = calculateAverage(gains);
                double averageLosses = calculateAverage(losses);

                double rsiValue = calculateStepTwoRSI(currentRSI.getPrevAverageGains(), currentRSI.getPrevAverageLosses(), averageGain, averageLosses);

                currentRSI.addRSIValue(rsiValue);
                currentRSI.setPrevAverage(averageGain, averageLosses);

            }else{
                //If there isn't enough data to use the full rsi period
                //Uses the first step RSI calculation

                double startingValue = currentPair.get(0);

                for(int i = 0; i < currentPair.getPriceList().size() - 1; i++){
                    double indexPrice = currentPair.get(currentPair.getPriceList().size() - i - 1);

                    if(indexPrice > startingValue){
                        double gain = ((indexPrice / startingValue) - 1.0) * 100.0;
                        gains.add(gain);
                    }else if(indexPrice < startingValue){
                        double loss = ((startingValue / indexPrice) - 1.0) * 100.0;
                        losses.add(loss);
                    }
                }

                double averageGain = calculateAverage(gains);
                double averageLosses = calculateAverage(losses);

                double rsiValue = calculateStepOneRSI(averageGain, averageLosses);

                currentRSI.addRSIValue(rsiValue);
                currentRSI.setPrevAverage(averageGain, averageLosses);
            }

        }else{
            rsiValues.add(new RSIValue());
            rsiValues.get(rsiValues.size() - 1).addRSIValue(50);
        }

        initialized = true;
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

    public ArrayList<RSIValue> getRsiValues(){
        return rsiValues;
    }
}
