package com.jordanfoster;

import com.jordanfoster.TradingBot.TradingBot;
import com.jordanfoster.TradingBot.Wallet.Wallet;
import com.jordanfoster.UserInterface.MainPageController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class BinanceTradingBot extends Application {

    public static MainPageController mainController;

    public static TradingBot tradingBot;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/MainPage.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Binance Trading Bot");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });
        primaryStage.show();

        mainController = loader.<MainPageController>getController();

        tradingBot = new TradingBot();
        tradingBot.start();
    }

    public static void main(String[] args){
        launch(args);
    }
}
