package model;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;
import java.util.ArrayList;


public class AdminController {

    @FXML TextField add;
    @FXML TextField delete;
    //ArrayList<User> UsersList = new ArrayList<User>();

    public void start(Stage mainstage) {

    }
/*
    public void add(ActionEvent e){
        UsersList.add(new User(add.getText().trim()));
        print();
    }

    public void delete(ActionEvent e){
        for(int i = 0; i < UsersList.size(); i++) {
            if((UsersList.get(i).username).equals(delete.getText().trim())) {
                UsersList.remove(i);
                i--;
            }
        }
        print();
    }

    public void logout(ActionEvent e){

    }

    public ArrayList<User> getUsers(){
        return UsersList;
    }

    public void print(){
        for(int i = 0; i < UsersList.size(); i++) {
            System.out.print(User.get(i).username);
        }
    }
    */
}
