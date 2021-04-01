package model;

import app.Album;
import app.Photo;
import app.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import java.io.IOException;
import java.util.ArrayList;

// !!!NOTE: PHOTO CONTROLLER IS NOT WORKING RIGHT NOW BECAUSE NOTHING IS BEING PASSING INTO THE SCENE FROM ALBUMS.
// THIS WILL WORK ONCE WE FIGURE OUT MOUSE CLICKED AND CAN ACTUALLY SEND IN THE USER AND ALBUM THAT WE ARE USING!!!

public class PhotoController {

    //int albumIndex;
    //int userIndex;
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

    public void start(Stage mainStage, Album album, User user) {
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
        //DO NOT DELETE THESE COMMENTS
        //this.user = UsersList.get(userIndex);
        this.album = album;
        this.user = user;
        //this.album = user.getAlbums().get(albumIndex);
        System.out.println(user.getUsername());
        System.out.println(album.getName());
    }

    public void logout(ActionEvent event) throws Exception{
        //saves the users arraylist
        Serialize.writeApp(UsersList);

        Stage appStage=(Stage)logout_btn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/album.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        AlbumController controller = loader.getController();
        controller.start(appStage, user);
        appStage.setScene(new Scene(root));
        appStage.show();

    }

    public void setImage(ActionEvent e) throws IOException {
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", ".gif", ".bmp");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
        File selectedFile = fileChooser.showOpenDialog(mainStage);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/IndividualPhotosController.fxml"));
        try {
            AnchorPane img = (AnchorPane)loader.load();
            IndividualPhotosController photoView = loader.getController();
            photoView.album_grid.setVisible(true);
            Image i = new Image(selectedFile.toURI().toString());
            photoView.image.setImage(i);
            Photo p = new Photo(i);
            try {
                this.album.addPhoto(p);
                grid.add(photoView.album_grid, col, row);
                if (col == 2) {
                    row++;
                    col = 0;
                } else {
                    col++;
                }
            } catch (IllegalArgumentException error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                String content = error.getMessage();
                alert.setContentText(content);
                alert.showAndWait();
            }

        } catch (Exception ex) {
            System.out.println("catch error");
            ex.printStackTrace();
        }
        Serialize.writeApp(UsersList);
    }

}
