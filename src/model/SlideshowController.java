package model;
import app.Album;
import app.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.ArrayList;

/**
 * This class takes care of the elements and actions
 * performed on the Slideshow fxml page.
 *
 * @author Jahnavi Bavuluri and Chiraag Rekhari
 */
public class SlideshowController {
    /**
     * The user that is currently using the application.
     */
    private User user;
    /**
     * The album that is currently being displayed.
     */
    private Album album;
    /**
     * The main stage where the application is running.
     */
    public Stage mainStage;
    /**
     * The list that stores all the users on the application.
     */
    public ArrayList<User> UsersList;
    /**
     * The index of where the current user is located in the
     * list of all users.
     */
    public int userIndex;
    /**
     * The index of where the current album is located in the
     * users list of albums.
     */
    public int albumIndex;
    /**
     * The index of the current photo being displayed.
     */
    public int photo;
    @FXML ImageView Image;
    @FXML Label slideshow_caption;
    @FXML Label slideshow_date;
    @FXML Label slideshow_tags;

    /**
     * This method populates the slideshow view with the first photo in the album
     * and displays the photo's caption, date, and tags.
     *
     * @param mainStage     The main stage where the application is running.
     * @param userIndex     The index of where the current user is located in the
     *                      list of all users.
     * @param albumIndex    The index of where the current album is located in the
     *                      users list of albums.
     */
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
        slideshow_tags.setWrapText(true);
        slideshow_date.setWrapText(true);
        slideshow_caption.setWrapText(true);
        this.photo = 0;
        if (this.album.getPhotos().size() != 0) {
            Image.setImage(new Image(album.getPhotos().get(photo).getFile().toURI().toString()));
            slideshow_caption.setText((album.getPhotos().get(photo).getCaption() == null ? "Caption: " : album.getPhotos().get(photo).getCaption()));
            slideshow_date.setText((album.getPhotos().get(photo).getDate() == null ? "Date: " : album.getPhotos().get(photo).getDate()));
            slideshow_tags.setText((album.getPhotos().get(photo).getDisplayTags() == null ? "Tags: " : album.getPhotos().get(photo).getDisplayTags()));
        }
    }

    /**
     * This method handles the transition back to the photos view.
     *
     * @throws Exception
     */
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

    /**
     * This method shows the previous photo in the album as well
     * as displays its caption, date, and tags.
     */
    public void previousPhoto() {
        if (this.album.getPhotos().size() != 0) {
            //user can only go back if they are not at the beginning
            if (this.photo != 0) {
                this.photo--;
                Image.setImage(new Image(this.album.getPhotos().get(this.photo).getFile().toURI().toString()));
                slideshow_caption.setText((album.getPhotos().get(photo).getCaption() == null ? "Caption: " : album.getPhotos().get(photo).getCaption()));
                slideshow_date.setText((album.getPhotos().get(photo).getDate() == null ? "Date: " : album.getPhotos().get(photo).getDate()));
                slideshow_tags.setText((album.getPhotos().get(photo).getDisplayTags() == null ? "Tags: " : album.getPhotos().get(photo).getDisplayTags()));
            }
        }
    }

    /**
     * This method shows the next photo in the album as well
     * as displays its caption, date, and tags.
     */
    public void nextPhoto() {
        if (this.album.getPhotos().size() != 0) {
            //we can only go next if the photo index is not at the end
            if (this.photo != this.album.getPhotos().size() - 1) {
                this.photo++;
                Image.setImage(new Image(this.album.getPhotos().get(this.photo).getFile().toURI().toString()));
                slideshow_caption.setText((album.getPhotos().get(photo).getCaption() == null ? "Caption: " : album.getPhotos().get(photo).getCaption()));
                slideshow_date.setText((album.getPhotos().get(photo).getDate() == null ? "Date: " : album.getPhotos().get(photo).getDate()));
                slideshow_tags.setText((album.getPhotos().get(photo).getDisplayTags() == null ? "Tags: " : album.getPhotos().get(photo).getDisplayTags()));
            }
        }
    }

}

