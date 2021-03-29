package model;

import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;
import java.util.ArrayList;
import app.User;
import app.Photos;
import model.AdminController;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.Optional;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.scene.layout.AnchorPane;




public class LoginController {

    @FXML TextField login;
    @FXML Button login_btn;
    Stage mainStage;
    public void start(Stage mainStage) {
    this.mainStage = mainStage;
    }

    public void login(ActionEvent e) throws Exception{


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/admin.fxml"));
        try {
            mainStage.close();
            AnchorPane root = (AnchorPane)loader.load();
            AdminController adminview = loader.getController();
            Stage stage = new Stage();

            adminview.start(stage);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException exception) {
            exception.printStackTrace();
        }

            /*Stage appStage;
            Parent root;

            appStage = mainStage;
            root=FXMLLoader.load(getClass().getResource("/view/admin.fxml"));
            Scene scene=new Scene(root);
            appStage.setScene(scene);
            appStage.show();*/



    }

}
