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


public class SlideshowController {
    @FXML ImageView Image;
    User user;
    Album album;
    Stage mainStage;
    int photo;

    public void start(Stage mainStage, User user, Album album){
        this.user = user;
        this.album = album;
        this.mainStage = mainStage;
        photo = 0;
        if (this.album.getPhotos().size() != 0) {
            Image.setImage(album.getPhotos().get(photo).getImage());
        }
    }

    public void backToPhotos() throws Exception {
        Stage appStage=this.mainStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/photos.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        PhotoController controller = loader.getController();
        controller.start(this.mainStage, this.album, this.user);
        appStage.setScene(new Scene(root));
        appStage.show();
    }

    public void previousPhoto() {
        if (this.album.getPhotos().size() != 0) {
            //if we are at the beginning and want to go back, we have to get the last photo and go backwards
            if (photo == 0) {
                photo = this.album.getPhotos().size()-1;
            }
            Image.setImage(this.album.getPhotos().get(photo).getImage());
            photo--;
        }
    }

    public void nextPhoto() {
        if (this.album.getPhotos().size() != 0) {
            //if we are at the end and want to go forward, we have to loop back and start at beginning
            if (photo == this.album.getPhotos().size() - 1) {
                photo = 0;
            }
            Image.setImage(this.album.getPhotos().get(photo).getImage());
            photo++;
        }
    }

}

