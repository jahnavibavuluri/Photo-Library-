package app;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The Photos class is where the application runs from. It
 * extends the Application class from JavaFX.
 *
 * @author Jahnavi Bavuluri and Chiraag Rekhari
  */
public class Photos extends Application {

    /**
     * This method starts the Photos project by overriding the
     * start method from the Application class.
     *
     * @param primaryStage  the main Stage were the application will be running
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/login.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        LoginController listController = loader.getController();
        listController.start(primaryStage);

        primaryStage.setTitle("Photos52 -- Jahnavi Bavuluri and Chiraag Rekhari");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * The Main method that launches the actual application.
     *
     * @param args
     */
    public static void main(String[] args) {launch(args);}
}
