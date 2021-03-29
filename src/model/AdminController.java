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


public class AdminController {

    @FXML TextField add;
    @FXML TextField delete;
    @FXML ListView<String> user_listview;

    ArrayList<User> UsersList = new ArrayList<User>();
    ObservableList<String> obsList = FXCollections.observableArrayList();;

    public void start(Stage mainstage) {

    }
    public void updateListView(){
        obsList.clear();
        for (int i = 0; i < UsersList.size(); i++) {
            obsList.add(UsersList.get(i).username);
        }
        user_listview.setItems(obsList);
    }

    public void add() {
            for (int i = 0; i < UsersList.size(); i++) {
                if (((UsersList.get(i).username).equals(add.getText().trim()))) {
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
        if(UsersList.size()==0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input Error");
            String content = "There is nothing in the list";
            alert.setContentText(content);
            alert.showAndWait();
            delete.clear();
            return;
        }



            for (int i = 0; i < UsersList.size(); i++) {
                if ((UsersList.get(i).username).equals(delete.getText().trim())) {
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

    public void logout(ActionEvent e){

    }

    public ArrayList<User> getUsers(){
        return UsersList;
    }


}
