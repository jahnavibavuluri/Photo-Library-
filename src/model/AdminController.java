package model;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;
import java.util.ArrayList;
import app.User;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.Optional;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;



public class AdminController{

    @FXML TextField add;
    @FXML TextField delete;
    @FXML Button logout_btn;
    @FXML ListView<String> user_listview;

    ArrayList<User> UsersList = new ArrayList<User>();
    ObservableList<String> obsList = FXCollections.observableArrayList();


    public void start(Stage mainstage) {
        UsersList.add(new User("Stock"));
        updateListView();
    }

    public void logout(ActionEvent event) throws Exception{
        Stage appStage;
        Parent root;

        appStage=(Stage)logout_btn.getScene().getWindow();
        root=FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        Scene scene=new Scene(root);
        appStage.setScene(scene);
        appStage.show();
    }

    public void updateListView(){
        obsList.clear();
        for (int i = 0; i < UsersList.size(); i++) {
            obsList.add(UsersList.get(i).username);
        }
        user_listview.setItems(obsList);
    }

    public void add() {
        if(add.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input Error");
            String content = "Please Make An Input";
            alert.setContentText(content);
            alert.showAndWait();
            add.clear();
            return;
        }

            for (int i = 0; i < UsersList.size(); i++) {
                if (((UsersList.get(i).username.toLowerCase()).equals(add.getText().trim().toLowerCase()))) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Input Error");
                    String content = "No Two Users Can NOT Have The Same Name";
                    alert.setContentText(content);
                    alert.showAndWait();
                    add.clear();
                    return;
                }
            }

        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Confirmation");
        String content1 = "Are you sure you want to add?";
        alert1.setContentText(content1);
        Optional<ButtonType> result = alert1.showAndWait();
        if ((result).get() == ButtonType.OK) {
            UsersList.add(new User(add.getText().trim()));
            add.clear();
            updateListView();
        }else{
            add.clear();
            return;
        }

    }

    public void delete(){

        if(delete.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input Error");
            String content = "Please Make An Input";
            alert.setContentText(content);
            alert.showAndWait();
            delete.clear();
            return;
        }
        if(delete.getText().toLowerCase().equals("stock")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input Error");
            String content = "You Can Not Delete This User";
            alert.setContentText(content);
            alert.showAndWait();
            delete.clear();
            return;
        }



            for (int i = 0; i < UsersList.size(); i++) {
                if ((UsersList.get(i).username.toLowerCase()).equals(delete.getText().trim().toLowerCase())) {
                    Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                    alert1.setTitle("Confirmation");
                    String content1 = "Are you sure you want to delete?";
                    alert1.setContentText(content1);
                    Optional<ButtonType> result = alert1.showAndWait();
                    if ((result).get() == ButtonType.OK) {
                        UsersList.remove(i);
                        i--;
                        delete.clear();
                        updateListView();
                        return;
                    } else{
                        delete.clear();
                        return;
                    }
                }
            }


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Input Error");
        String content = delete.getText() + " has never been added to the list of users";
        alert.setContentText(content);
        delete.clear();
        alert.showAndWait();


    }


    public ArrayList<User> getUsers(){
        return UsersList;
    }


}
