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

/**
 * This class takes care of the elements and events
 * performed on the Individual Album fxml page.
 *
 * @author Jahnavi Bavuluri and Chiraag Rekhari
 */
public class IndividualAlbumController {

    @FXML ImageView image;
    @FXML GridPane album_grid;
    @FXML Label album_name_text;
    @FXML Label number_of_pics;
    @FXML Label dates_of_pics;
    /**
     * The main stage where the application will be running.
     */
    public Stage mainStage;
    /**
     * The index of where the current album is located in the
     * users list of albums.
     */
    public int albumIndex;
    /**
     * The index of where the current user is located in the
     * list of all users.
     */
    public int userIndex;
    /**
     * The list that stores all of the users on the application.
     */
    public ArrayList<User> UsersList;
    /**
     * The actual user that is currently using the application.
     */
    private User user;
    /**
     * The actual albums that is currently being referenced.
     */
    private Album album;

    /**
     * The start method populates the individual album gridpane
     * with the album thumbnail, the name of the album, the number of
     * photos in the album, and the date range.
     *
     * @param mainStage     The stage where the application will be running.
     * @param albumIndex    The index of where the current album is located in the
     *                      users list of albums.
     * @param userIndex     The index of where the current user is located in the
     *                      list of all users.
     */
    public void start(Stage mainStage, int albumIndex, int userIndex) {
        this.albumIndex = albumIndex;
        this.userIndex = userIndex;
        this.mainStage = mainStage;

        try {
            UsersList = Serialize.readApp();
        } catch (Exception e) {
            System.out.println("This should not appear since users array list will always have Stock user!");
            e.printStackTrace();
        }

        this.user = UsersList.get(userIndex);
        System.out.println(user.getUsername());
        System.out.println(user.getAlbums().size());
        this.album = user.getAlbums().get(albumIndex);

        this.album_grid.setId(UsersList.get(userIndex).getAlbums().get(albumIndex).getName());

        if (album.getPhotos().size() != 0) {

            image.setImage(new Image(album.getPhotos().get(album.getPhotos().size()-1).getFile().toURI().toString()));
        }

        album_grid.setVisible(true);
        number_of_pics.setText("Number of photos: " + (album.numPhotos() == 0 ? "0" : String.valueOf(album.numPhotos())));
        album_name_text.setText((album.getName()));
        album_name_text.setWrapText(true);

        dates_of_pics.setWrapText(true);
        if (album.getRangeOfPhotos() == null) {
            dates_of_pics.setText("");
        } else if (album.getRangeOfPhotos().size() == 1) {
            dates_of_pics.setText(album.getRangeOfPhotos().get(0).toString());
        } else {
            dates_of_pics.setText(album.getRangeOfPhotos().get(0).toString() + " -- " + album.getRangeOfPhotos().get(1).toString());
        }

        album_grid.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            /**
             * The handle method detects if the album was double clicked
             * and takes the user into that individual album where they
             * can then view their photos.
             *
             * @param mouseEvent    the mouse event that allows the application to know
             *                      that the user has double clicked on this album
             */
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

    /**
     * This method handles the transition from the album view
     * to the photos view where users can view their photos
     * within a specific album.
     *
     * @param album         the album object that the user is viewing
     * @throws IOException
     */
    public void goToPhotosScene(Album album) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/photos.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        PhotoController controller = loader.getController();
        controller.start(mainStage,this.albumIndex,this.userIndex);
        mainStage.setScene(new Scene(root));
        mainStage.show();
    }
}
