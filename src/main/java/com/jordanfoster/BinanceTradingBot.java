package com.jordanfoster;


import com.jordanfoster.TradingBot.TradingBot;
import com.jordanfoster.Userinterface.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class BinanceTradingBot extends Application {

    public static MainController mainController;

    @Override
    public void start(Stage primaryStage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/MainInterface.fxml"));
//        Parent root = loader.load();
//        primaryStage.setTitle("Binance Trading Bot v2.0");
//        primaryStage.setScene(new Scene(root, 800, 700));
//        primaryStage.setResizable(false);
//        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent e) {
//                Platform.exit();
//                System.exit(0);
//            }
//        });
//
//        mainController = loader.getController();
//
//        primaryStage.show();
    }

    public static void main(String[] args){

        TradingBot bot = new TradingBot();
        bot.start();

        //Launch the GUI
        //launch(args);
    }
}
