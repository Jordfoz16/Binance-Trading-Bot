package com.jordanfoster;

import com.jordanfoster.TradingBot.TradingBot;
import com.jordanfoster.TradingBot.Wallet.Wallet;
import com.jordanfoster.UserInterface.MainPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sun.applet.Main;

public class BinanceTradingBot extends Application {

    public static boolean startTradingBot = true;
    public static boolean isRunning = false;

    public static MainPageController mainController;

    private TradingBot tradingBot = new TradingBot();

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/MainPage.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Binance Trading Bot");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.setResizable(false);
        primaryStage.show();

        mainController = loader.<MainPageController>getController();

        startTradingBot();
    }

    public void startTradingBot(){

        if(isRunning == false){
            if(startTradingBot == true){
                //Start Trading Bot
                tradingBot.start();
                isRunning = true;
            }
        }
    }

    public void stopTradingBot(){

        if(isRunning == true){
            if(startTradingBot == false){
                //Stop Trading Bot
            }
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}
