package model;

import app.Album;
import app.Photo;
import app.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.Iterator;
import java.util.Optional;

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
    @FXML ImageView display_image;
    @FXML Label photos_caption;
    @FXML Label photos_date;
    @FXML Label photos_tags;
    @FXML Button logout_btn;

    public int row = 0;
    public int col = 0;

    public void start(Stage mainStage, Album album, User user) {
        //Image i = new javafx.scene.image.Image("/view/image.jpg");
        //image2.setImage(i);
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
        clearPhotoDisplay();
        if (this.album.numPhotos() > 0) {
            for (Photo p: this.album.getPhotos()) {
                try {
                    populatePhotos(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void clearPhotoDisplay() {
        this.display_image.setImage(null);
        this.photos_caption.setText("Caption: ");
        this.photos_date.setText("Date: ");
        this.photos_tags.setText("Tags: ");
    }

    public void populatePhotos(Photo p) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/IndividualPhotosController.fxml"));
        try {
            AnchorPane img = (AnchorPane)loader.load();
            IndividualPhotosController photoView = loader.getController();
            photoView.start(mainStage, p, display_image, photos_caption, photos_date, photos_tags);
            grid.add(photoView.photo_grid, col, row);
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
            Image i = new Image(selectedFile.toURI().toString());
            Photo p = new Photo(i);
            photoView.start(this.mainStage,p, display_image, photos_caption, photos_date, photos_tags);

            //photoView.image.setImage(i);
            //photoView.album_grid.setVisible(true);

            try {
                this.album.addPhoto(p);
                grid.add(photoView.photo_grid, col, row);
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

    public void resetPhotos() throws Exception {
        Serialize.writeApp(UsersList);
        this.row = 0;
        this.col = 0;
        Iterator<Node> iter = this.grid.getChildren().iterator();
        while (iter.hasNext()) {
            Node node = iter.next();
            iter.remove();
        }
        this.start(this.mainStage, this.album, this.user);
    }

    public void editCaption(ActionEvent e) throws Exception {
        //get the image that the change it being made on
        Image i = display_image.getImage();
        Photo photoInAlbum = null;
        if (i == null) {
            //there is no photo selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No photo is selected!");
            String content = ("Please select an image to edit.");
            alert.setContentText(content);
            alert.showAndWait();
        } else {
            //find the photo in the album that corresponds to the photo being edited
            for (Photo p: album.getPhotos()) {
                if (p.sameImage(i))
                    photoInAlbum = p;
            }
            TextInputDialog newCaption = new TextInputDialog();
            newCaption.initOwner(this.mainStage);
            newCaption.setTitle("Edit Caption");
            newCaption.setHeaderText("New Caption");
            Optional<String> result = newCaption.showAndWait();
            if (result.isPresent()) {
                try {
                    photoInAlbum.setCaption(result.get());
                    try {
                        resetPhotos();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } catch (IllegalArgumentException error) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Input Error");
                    String content = error.getMessage();
                    alert.setContentText(content);
                    alert.showAndWait();
                }
            }
            //photos_caption.setText(photoInAlbum.getCaption());
        }
    }

    public void deletePhoto(ActionEvent e) {
        Image i = display_image.getImage();
        Photo photoInAlbum = null;
        if (i == null) {
            //there is no photo selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No photo is selected!");
            String content = ("Please select an image to edit.");
            alert.setContentText(content);
            alert.showAndWait();
        } else {
            //still have to finish implementing
        }

    }

    public void addTag(ActionEvent e) {

    }

    public void deleteTag(ActionEvent e) {

    }

    public void movePhoto(ActionEvent e) {

    }

    public void copyPhoto(ActionEvent e) {

    }

}
