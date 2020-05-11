package com.jordanfoster.TradingBot.Indicators.RSI;

import com.jordanfoster.TradingBot.TradingBot;

import java.util.ArrayList;

public class RSIValue {

    private double averageGains = 0;
    private double averageLosses = 0;

    private double startingValue = 0;
//    private ArrayList<Double> gains = new ArrayList<Double>();
//    private ArrayList<Double> losses = new ArrayList<Double>();

    private ArrayList<Double> rsiValues = new ArrayList<Double>();

//    public void addGains(double value){
//        double percentage = calculatePercentage(value, startingValue);
//        gains.add(percentage);
//        checkGainsListSize();
//    }

//    public void addLosses(double value){
//        double percentage = calculatePercentage(startingValue, value);
//        losses.add(percentage);
//        checkLossesListSize();
//    }

    public void addRSIValue(double value){
        rsiValues.add(value);
        checkListSize();
    }

//    private void checkGainsListSize(){
//        int listSize = RSI.rsiPeriod;
//
//        if(gains.size() > listSize){
//            gains.remove(0);
//        }
//    }
//
//    private void checkLossesListSize(){
//        int listSize = RSI.rsiPeriod;
//
//        if(losses.size() > listSize){
//            losses.remove(0);
//        }
//    }

    private void checkListSize(){
        int listSize = Integer.parseInt(TradingBot.fileConfig.getElement("price-feed", "price-history-size"));

        if(rsiValues.size() > listSize){
            rsiValues.remove(0);
        }
    }

    private double calculatePercentage(double largeValue, double smallValue){
        double percentage = largeValue / smallValue;
        percentage = (percentage - 1.0) * 100.0;

        return percentage;
    }

//    public void clearLists(){
//        gains.clear();
//        losses.clear();
//    }

    public void setStartingValue(double startingValue){
        this.startingValue = startingValue;
    }

//    public ArrayList<Double> getGains(){
//        return gains;
//    }
//
//    public ArrayList<Double> getLosses(){
//        return losses;
//    }

    public ArrayList<Double> getRsiValues(){
        return rsiValues;
    }

    public double getStartingValue(){
        return startingValue;
    }

    public double get(int index){
        return rsiValues.get(index);
    }

    public double getCurrent(){
        if(rsiValues.size() < 1) return 0;
        return rsiValues.get(rsiValues.size() - 1);
    }

}
