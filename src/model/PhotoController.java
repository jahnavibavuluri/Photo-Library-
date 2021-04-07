package model;

import app.Album;
import app.Photo;
import app.User;
import app.Tag;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
import javafx.util.Pair;

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
    public void addTag(ActionEvent e) throws Exception{
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Tag");

        // Set the button types.
        ButtonType done = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(done, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField from = new TextField();
        from.setPromptText("Key");
        TextField to = new TextField();
        to.setPromptText("Value");

        gridPane.add(new Label("Key:"), 0, 0);
        gridPane.add(from, 1, 0);
        gridPane.add(new Label("Value:"), 2, 0);
        gridPane.add(to, 3, 0);

        dialog.getDialogPane().setContent(gridPane);


        Platform.runLater(() -> from.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == done) {
                return new Pair<>(from.getText().toLowerCase(), to.getText().toLowerCase());
            }
            return null;
        });
        String temp;
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(pair -> {

            String value = "" + pair.getValue();
            String key = "" + pair.getKey();
            System.out.println(key + " " + value);
            try {
                Tag tag;
                if(key.equals("location") || key.equals("weather")){
                     tag = new Tag(key, value, false);
                } else{
                     tag = new Tag(key, value, true);
                }
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
                    for (Photo p : album.getPhotos()) {
                        if (p.sameImage(i))
                            photoInAlbum = p;
                    }
                }
                photoInAlbum.addTag(tag);
                user.addPreset(tag);
                user.printPreset();
            } catch  (IllegalArgumentException error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                String content = error.getMessage();
                alert.setContentText(content);
                alert.showAndWait();
            }


            //System.out.println(pair.getKey() + " " + pair.getValue());
            /*Album a = user.getAlbumWithName(pair.getKey());
            try {
                user.editAlbum(pair.getKey() + "", pair.getValue() + "");
                System.out.println(a.getName());
                //Node node = getNode(pair.getKey());
                System.out.println("the old album was: " + pair.getKey() + "\n the new albums is: " + pair.getValue());
                try {
                    //resetAlbums();
                } catch (Exception r) {
                    r.printStackTrace();
                }
            } catch (IllegalArgumentException error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                String content = error.getMessage();
                alert.setContentText(content);
                alert.showAndWait();
            }*/
        });
    }

    public void deleteTag(ActionEvent e) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Delete Tag");

        // Set the button types.
        ButtonType done = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(done, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField from = new TextField();
        from.setPromptText("Key");
        TextField to = new TextField();
        to.setPromptText("Value");

        gridPane.add(new Label("Key:"), 0, 0);
        gridPane.add(from, 1, 0);
        gridPane.add(new Label("Value:"), 2, 0);
        gridPane.add(to, 3, 0);

        dialog.getDialogPane().setContent(gridPane);


        Platform.runLater(() -> from.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == done) {
                return new Pair<>(from.getText(), to.getText());
            }
            return null;
        });
        String temp;
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(pair -> {
            try {
                String value = "" + pair.getValue();
                String key = "" + pair.getKey();
                System.out.println(key + " " + value);
                Tag tag = new Tag(key, value, true);
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
                    for (Photo p : album.getPhotos()) {
                        if (p.sameImage(i))
                            photoInAlbum = p;
                    }
                }
                photoInAlbum.deleteTag(tag);
                user.deletePreset(tag);
                user.printPreset();
            } catch (IllegalArgumentException error){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                String content = error.getMessage();
                alert.setContentText(content);
                alert.showAndWait();
            }
        });
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

    public void viewSlideshow() throws Exception {
        Stage appStage=this.mainStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/slideshow.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        SlideshowController controller = loader.getController();
        controller.start(this.mainStage, this.user, this.album);
        appStage.setScene(new Scene(root));
        appStage.show();
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
            //find the photo in the album that corresponds to the photo being edited
            for (Photo p: album.getPhotos()) {
                if (p.sameImage(i))
                    photoInAlbum = p;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("You are deleting the selected photo!");
            alert.setContentText("Are you sure you want to delete this photo?");
            alert.showAndWait();
            try {
                this.album.deletePhoto(photoInAlbum);
                try {
                    resetPhotos();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } catch (IllegalArgumentException error) {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Input Error");
                String content = error.getMessage();
                alert1.setContentText(content);
                alert1.showAndWait();
            }
        }

    }





    public void movePhoto(ActionEvent e) {
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
            for (Photo p: album.getPhotos()) {
                if (p.sameImage(i))
                    photoInAlbum = p;
            }
            TextInputDialog movePhoto = new TextInputDialog();
            movePhoto.initOwner(this.mainStage);
            movePhoto.setTitle("Move Photo");
            movePhoto.setHeaderText("Enter the album name you would like to move this photo into.");
            Optional<String> result = movePhoto.showAndWait();
            if (result.isPresent()) {
                try {
                    Album moveTo = user.getAlbum(result.get());
                    user.movePhoto(photoInAlbum, this.album, moveTo);
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
        }
    }

    public void copyPhoto(ActionEvent e) {
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
            for (Photo p: album.getPhotos()) {
                if (p.sameImage(i))
                    photoInAlbum = p;
            }
            TextInputDialog copyPhoto = new TextInputDialog();
            copyPhoto.initOwner(this.mainStage);
            copyPhoto.setTitle("Copy Photo");
            copyPhoto.setHeaderText("Enter the album name you would like to copy this photo into.");
            Optional<String> result = copyPhoto.showAndWait();
            if (result.isPresent()) {
                try {
                    Album copyTo = user.getAlbum(result.get());
                    user.copyPhoto(photoInAlbum, this.album, copyTo);
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
        }

    }

}
