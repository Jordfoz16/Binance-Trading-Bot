package com.jordanfoster;


import com.jordanfoster.FileManagement.FileConfig;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class BinanceTradingBot extends Application {

    public static String apiKey = "";
    public static String secretKey = "";

    public static FileConfig fileConfig;

    public static void main(String[] args){
        fileConfig = new FileConfig();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/MainInterface.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Binance Trading Bot v2.0");
        primaryStage.setScene(new Scene(root, 800, 700));
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });
        primaryStage.show();
    }
}
