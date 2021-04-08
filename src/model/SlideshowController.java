package model;
import app.Album;
import app.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.util.ArrayList;


public class SlideshowController {
    @FXML ImageView Image;
    private User user;
    private Album album;
    public Stage mainStage;
    public ArrayList<User> UsersList;
    public int userIndex;
    public int albumIndex;
    int photo;

    public void start(Stage mainStage, int userIndex, int albumIndex){
        this.userIndex = userIndex;
        this.albumIndex = albumIndex;
        this.mainStage = mainStage;
        try {
            UsersList = Serialize.readApp();
        } catch (Exception e) {
            System.out.println("This should not appear since users array list will always have Stock user!");
            e.printStackTrace();
        }
        this.user = UsersList.get(userIndex);
        this.album = user.getAlbums().get(albumIndex);
        photo = 0;
        if (this.album.getPhotos().size() != 0) {
            Image.setImage(new Image(album.getPhotos().get(photo).getFile().getAbsolutePath()));
        }
    }

    public void backToPhotos() throws Exception {
        Stage appStage=this.mainStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/photos.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        PhotoController controller = loader.getController();
        controller.start(this.mainStage, this.albumIndex, this.userIndex);
        appStage.setScene(new Scene(root));
        appStage.show();
    }

    public void previousPhoto() {
        if (this.album.getPhotos().size() != 0) {
            //if we are at the beginning and want to go back, we have to get the last photo and go backwards
            if (photo == 0) {
                photo = this.album.getPhotos().size()-1;
            }
            Image.setImage(new Image(this.album.getPhotos().get(photo).getFile().getAbsolutePath()));
            photo--;
        }
    }

    public void nextPhoto() {
        if (this.album.getPhotos().size() != 0) {
            //if we are at the end and want to go forward, we have to loop back and start at beginning
            if (photo == this.album.getPhotos().size() - 1) {
                photo = 0;
            }
            Image.setImage(new Image(this.album.getPhotos().get(photo).getFile().getAbsolutePath()));
            photo++;
        }
    }

}

