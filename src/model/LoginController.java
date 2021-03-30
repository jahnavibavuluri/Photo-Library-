package model;

import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.scene.layout.AnchorPane;




public class LoginController {

    @FXML TextField login;
    @FXML Button login_btn;

    public void start(Stage mainStage) {
    }

    public void login(ActionEvent e) throws Exception{


        if((login.getText().toLowerCase()).equals("admin")) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/admin.fxml"));
            try {

                AnchorPane root = (AnchorPane) loader.load();
                AdminController adminview = loader.getController();
                Stage stage = new Stage();

                adminview.start(stage);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

}
