package com.jordanfoster;

import com.jordanfoster.FileManagement.FileManagement;
import com.jordanfoster.JSONHandler.JSONHandler;
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
import org.json.simple.JSONObject;

public class BinanceTradingBot extends Application {

    public static MainPageController mainController;

    public static TradingBot tradingBot;
    public static FileManagement fileManagement;

    public static String apiKey;
    public static String secretKey;

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

        fileManagement = new FileManagement();

        readBinanceKeys();


        tradingBot = new TradingBot();
        tradingBot.start();
    }

    public void readBinanceKeys(){
        String path = "apikey.txt";
        JSONHandler jsonHandler = new JSONHandler();

        JSONObject apiFile;

        if(fileManagement.fileExists(path)){
            apiFile = jsonHandler.parseJSON(fileManagement.read(path)).get(0);

        }else{

            //Creates the file if it doesnt exists
            String content = "{\n" +
                    "\t\"apikey\": \"Enter your API key from binance\",\n" +
                    "\t\"secretkey\": \"Enter your Secret Key from binance\"\n" +
                    "}";

            fileManagement.write(path, content);

            apiFile = jsonHandler.parseJSON(fileManagement.read(path)).get(0);
        }

        apiKey = apiFile.get("apikey").toString();
        secretKey = apiFile.get("secretkey").toString();
        mainController.setAccountPage(apiKey, secretKey);
    }

    public static void main(String[] args){
        launch(args);
    }
}
