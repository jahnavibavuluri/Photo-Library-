package model;

import app.Album;
import app.User;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class IndividualAlbumController {

    @FXML ImageView image;
    @FXML GridPane album_grid;
    @FXML Label album_name_text;
    @FXML Label number_of_pics;
    @FXML Label dates_of_pics;
    public Stage mainStage;
    public User user;
    //public ArrayList<User> UsersList;

    public void start(Stage mainStage, Album album, User user) {
        this.user = user;
        this.mainStage = mainStage;
        album_grid.setVisible(true);
        number_of_pics.setText((album.numPhotos() == 0 ? "N/A" : String.valueOf(album.numPhotos())));
        album_name_text.setText((album.getName()));
        dates_of_pics.setText(album.getRangeOfPhotos().toString());
        album_grid.setVisible(true);

        album_grid.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    System.out.println(album.getName() + " was clicked twice!");
                    //Stage appStage=(Stage)logout_btn.getScene().getWindow();
                    try {
                        goToPhotosScene(album);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void goToPhotosScene(Album album) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/photos.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        PhotoController controller = loader.getController();
        controller.start(mainStage,album,user);
        mainStage.setScene(new Scene(root));
        mainStage.show();
    }

    /*
    public void addAlbum(Stage mainStage) {
        album_grid.setVisible(true);
        //album_name_text.setText(name_of_album);
        //Font f = Font.font(FontPosture.ITALIC);
        //number_of_pics.setFont(Font.font(number_of_pics.getText(),FontPosture.ITALIC, 10.0));
        //number_of_pics.setFont(Font.font(String.valueOf(FontPosture.ITALIC)));
        //dates_of_pics.setFont(Font.font(String.valueOf(FontPosture.ITALIC)));
    }*/


}
