package main.Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
       // DataStore.initJobsFromFile("out.txt");
       // DataStore.initCustomersFromDatabase();



//        DataStore.initTrailersFromFile("trailers.txt");
//        DataStore.initVehiclesFromFile("trucks.txt");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));

        Parent root = fxmlLoader.load();
        primaryStage.setTitle("QR-Editor");
        Scene scene=new Scene(root, 1600, 900);

        String css = Main.class.getResource("/style.css").toExternalForm();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);

        primaryStage.setScene(scene);
        MainController controller =  fxmlLoader.getController();
        controller.setScene(primaryStage,scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
