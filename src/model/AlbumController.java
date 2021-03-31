package model;

import app.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.EOFException;
import java.util.ArrayList;


public class AlbumController {

    int userIndex;
    ArrayList<User> UsersList;
    User user;
    Stage mainStage;
    @FXML ScrollPane scroll;
    @FXML GridPane grid;
    @FXML Button logout_btn;

    public int row = 0;
    public int col = 0;

    public void start(Stage mainStage, int userIndex) {
        this.mainStage = mainStage;

        try {
            UsersList = Serialize.readApp();
        } catch (Exception e) {
            System.out.println("This should not appear since users array list will always have Stock user!");
            if (e instanceof EOFException)
                UsersList = new ArrayList<User>();
        }
        this.userIndex = userIndex;
        this.user = UsersList.get(userIndex);
        System.out.println(user.getUsername());
    }

    public void logout(ActionEvent event) throws Exception{
        //saves the users arraylist
        Serialize.writeApp(UsersList);

        Stage appStage=(Stage)logout_btn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/login.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        LoginController controller = loader.getController();
        controller.start(appStage);
        appStage.setScene(new Scene(root));
        appStage.show();

    }

    public void setImage(ActionEvent e) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/IndividualAlbumController.fxml"));
        try {
            AnchorPane img = (AnchorPane)loader.load();
            IndividualAlbumController test = loader.getController();
            test.album_grid.setVisible(true);
            Image i = new javafx.scene.image.Image("/view/image.jpg");
            test.image.setImage(i);
            grid.add(test.album_grid, col, row);
            if (col == 2) {
                row++;
                col = 0;
            } else {
                col++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

 /*GridPane album_grid = new GridPane();
        album_grid.addRow(100);
        album_grid.addRow(33);
        album_grid.addRow(34);
        album_grid.addRow(33);
        album_grid.setPrefHeight(200);
        album_grid.setPrefWidth(190);
        ImageView imgView = new ImageView();
        imgView.setFitWidth(100);
        imgView.setFitHeight(100);
        Image i = new javafx.scene.image.Image("/view/image.jpg");
        imgView.setImage(i);
        album_grid.add(imgView, 0,0);
        Label name = new Label();
        name.setText("Name of album");
        album_grid.add(name,0,1);
        Label number = new Label();
        number.setText("number of pictures in album");
        album_grid.add(number,0,2);
        Label date = new Label();
        date.setText("Date of album");
        album_grid.add(date,0,3);
        grid.add(album_grid, col, row);
        if (col == 2) {
            row++;
            col = 0;
        } else {
            col++;
        }*/