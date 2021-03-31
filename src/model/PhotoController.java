package model;

import app.Album;
import app.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.stage.FileChooser;

import java.io.EOFException;
import java.io.File;
import java.util.ArrayList;

public class PhotoController {

    int albumIndex;
    int userIndex;
    ArrayList<User> UsersList;
    User user;
    Album album;
    Stage mainStage;
    @FXML ScrollPane scroll;
    @FXML GridPane grid;
    @FXML ImageView image2;
    @FXML Button logout_btn;

    public int row = 0;
    public int col = 0;

    public void start(Stage mainStage, int albumIndex, int userIndex) {
        Image i = new javafx.scene.image.Image("/view/image.jpg");
        image2.setImage(i);

        this.mainStage= mainStage;
        try {
            UsersList = Serialize.readApp();
        } catch (Exception e) {
            System.out.println("This should not appear since users array list will always have Stock user!");
            if (e instanceof EOFException)
                UsersList = new ArrayList<User>();
        }
        this.userIndex = userIndex;
        //this.user = UsersList.get(userIndex);
        this.albumIndex = albumIndex;
        //this.album = user.getAlbums().get(albumIndex);
        //System.out.println(user.getUsername());
    }

    public void setImage(ActionEvent e) {
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", ".gif", ".bmp");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
        File selectedFile = fileChooser.showOpenDialog(mainStage);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/IndividualPhotoController.fxml"));
        try {
            AnchorPane img = (AnchorPane)loader.load();
            IndividualAlbumController test = loader.getController();
            test.album_grid.setVisible(true);
            Image i = new Image(selectedFile.toURI().toString());
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
