package model;

import app.User;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.scene.layout.AnchorPane;


public class LoginController {

    Stage mainStage;
    ArrayList<User> UsersList;
    @FXML TextField login;
    @FXML Button login_btn;

    public void start(Stage mainStage) {
        this.mainStage = mainStage;
        try {
            UsersList = Serialize.readApp();
        } catch (Exception e) {
            System.out.println("This should not appear since users array list will always have Stock user!");
            if (e instanceof EOFException)
                UsersList = new ArrayList<User>();
        }
        System.out.println(UsersList.toString());
    }

    public void login(ActionEvent e) throws Exception{


        if((login.getText().toLowerCase()).equals("admin")) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/admin.fxml"));
            try {

                AnchorPane root = (AnchorPane) loader.load();
                AdminController adminview = loader.getController();
                //Stage stage = new Stage();
                adminview.start(mainStage);
                Scene scene = new Scene(root);
                mainStage.setScene(scene);
                mainStage.show();

            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            String user_logging_in = login.getText();
            for (int i = 0; i<UsersList.size(); i++) {
                if (UsersList.get(i).getUsername().equals(user_logging_in)) {
                    Stage appStage=(Stage)login_btn.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/view/album.fxml"));
                    AnchorPane root = (AnchorPane)loader.load();
                    AlbumController controller = loader.getController();
                    controller.start(appStage,UsersList.get(i));
                    appStage.setScene(new Scene(root));
                    appStage.show();
                }
            }
        }
    }

}
