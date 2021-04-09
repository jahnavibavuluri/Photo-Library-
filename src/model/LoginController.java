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

    public void start(Stage mainStage) throws Exception{
        this.mainStage = mainStage;
        try {
            UsersList = Serialize.readApp();
        } catch (Exception e) {
            System.out.println("This is the first time a user is using our app so we must initialize users list and add stock.");
            UsersList = new ArrayList<User>();
            /*if (e instanceof EOFException) {
                System.out.println("oef!!!!");

            }*/
        }

        if (this.UsersList.isEmpty()) {
            this.UsersList.add(new User("stock"));
            Serialize.writeApp(UsersList);
        }

        System.out.println(UsersList.toString());

        mainStage.setOnCloseRequest(event -> {
            try {
                Serialize.writeApp(UsersList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
                System.out.println(UsersList.get(i).getUsername());
                if (user_logging_in.toLowerCase().equals(UsersList.get(i).getUsername())) {
                //if (UsersList.get(i).getUsername().toLowerCase().equals(user_logging_in)) {
                    System.out.println("there is a match! The user is: " + user_logging_in);
                    Stage appStage=this.mainStage;
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/view/album.fxml"));
                    AnchorPane root = (AnchorPane)loader.load();
                    AlbumController controller = loader.getController();
                    controller.start(appStage,i);
                    appStage.setScene(new Scene(root));
                    appStage.show();
                }
            }
        }
    }

}
