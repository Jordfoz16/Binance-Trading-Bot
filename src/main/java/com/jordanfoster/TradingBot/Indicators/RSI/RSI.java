package com.jordanfoster.TradingBot.Indicators.RSI;

import com.jordanfoster.TradingBot.PriceFeed.TradingPair;
import com.jordanfoster.TradingBot.TradingBot;

import java.util.ArrayList;

public class RSI {

    public static int rsiPeriod = 120;

    private ArrayList<RSIValue> rsiValues = new ArrayList<RSIValue>();
    private boolean initialized = false;

    public void update(){

        for(int index = 0; index < TradingBot.priceFeed.getTradingPairs().size(); index++){

            TradingPair currentPair = TradingBot.priceFeed.getTradingPair(index);

            ArrayList<Double> gains = new ArrayList<Double>();
            ArrayList<Double> losses = new ArrayList<Double>();

            gains.clear();
            losses.clear();

            if(initialized){

                RSIValue currentRSI = rsiValues.get(index);

                //Moves the starting value along when enough data is available
                if(currentPair.getPriceList().size() > rsiPeriod){
                    currentRSI.setStartingValue(currentPair.get(currentPair.getPriceList().size() - rsiPeriod));

                    //Cycles through price data
                    for(int i = 1; i < rsiPeriod; i++){
                        double currentPrice = currentPair.getPriceList().get(currentPair.getPriceList().size() - i);

                        //Adds to the gains and losses
                        if(currentPrice > currentRSI.getStartingValue()){
                            gains.add(currentPrice);
                        }else if(currentPrice < currentRSI.getStartingValue()){
                            losses.add(currentPrice);
                        }
                    }

                    //Calculates an average for gains and losses
                    double averageGains = 0;
                    double averageLosses = 0;

                    for (int i = 0 ; i < gains.size(); i++){
                        averageGains += gains.get(i);
                    }

                    for(int i = 0; i < losses.size(); i++){
                        averageLosses += losses.get(i);
                    }

                    averageGains = averageGains / gains.size();
                    averageLosses = averageLosses / losses.size();

                    //Calculates RSI
                    currentRSI.addRSIValue(calculateRSI(averageGains, averageLosses));

                }else{
                    
                }

            }else{
                rsiValues.add(new RSIValue());
                rsiValues.get(rsiValues.size() - 1).setStartingValue(currentPair.getCurrentPrice());
            }
        }
        initialized = true;
    }

    private double calculateRSI(double averageGains, double averageLosses){
        double rsi = 0.0;

        if(Double.isNaN(averageGains)) averageGains = 0.1;
        if(Double.isNaN(averageLosses)) averageLosses = 0.1;

        rsi = 100.0 - (100.0 / (1.0 + ((averageGains / rsiPeriod) / (averageLosses / rsiPeriod))));

        return rsi;
    }

    public ArrayList<RSIValue> getRsiValues(){
        return rsiValues;
    }
}
