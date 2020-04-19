package com.jordanfoster.UserInterface.Logging;

import com.jordanfoster.BinanceTradingBot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    public Log(){

    }

    public Log(String msg){
        String outputMsg = getTimeDate() + " - " + msg;
        BinanceTradingBot.mainController.addLog(outputMsg);
    }

    public void logError(String msg){
        String outputMsg = getTimeDate() + " - ERROR - " + msg;
        BinanceTradingBot.mainController.addLog(outputMsg);
    }

    public void logWarning(String msg){
        String outputMsg = getTimeDate() + " - WARNING - " + msg;
        BinanceTradingBot.mainController.addLog(outputMsg);
    }

    public String getTimeDate(){
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        return datetime.format(now);
    }

    public void setProfit(double profit){
        BinanceTradingBot.mainController.setProfit(profit);
    }
}
