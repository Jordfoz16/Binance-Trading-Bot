package com.jordanfoster;

import com.jordanfoster.TradingBot.TradingBot;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BinanceTradingBot extends Application {

    public static boolean priceFeedRunning = true;

    TradingBot tradingBot = new TradingBot();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/UserInterface/MainPage.fxml"));
        primaryStage.setTitle("Binance Trading Bot");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();
    }

    public void start(){
        tradingBot.start();
    }

    public static void main(String[] args){
        launch(args);
        BinanceTradingBot btb = new BinanceTradingBot();
        btb.start();
    }
}
