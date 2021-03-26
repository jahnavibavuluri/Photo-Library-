package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;

public class Photos extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/search.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        SearchController listController = loader.getController();
        listController.start(primaryStage);

        primaryStage.setTitle("Photos52 -- Jahnavi Bavuluri and Chiraag Rekhari");
        primaryStage.setScene(new Scene(root, 700, 700));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {launch(args);}
}
