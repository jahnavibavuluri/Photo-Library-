package controller;

import model.Album;
import model.Photo;
import model.User;
import model.Tag;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import model.Serialize;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

/**
 * This class takes care of the elements and actions
 * performed on the Photos fxml page.
 *
 * @author Jahnavi Bavuluri and Chiraag Rekhari
 */
public class PhotoController {
    /**
     * The list that stores all the users on the application.
     */
    public ArrayList<User> UsersList;
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
     * The index of where the current album is located in the
     * users list of albums.
     */
    public int albumIndex;
    /**
     * The index of where the current user is located in the
     * list of all users.
     */
    public int userIndex;
    @FXML ScrollPane scroll;
    @FXML GridPane grid;
    @FXML ImageView display_image;
    @FXML Label photos_caption;
    @FXML Label photos_date;
    @FXML Label photos_tags;
    @FXML Label albumName;
    @FXML Button logout_btn;

    /**
     * The row counter for the grid gridpane.
     */
    public int row = 0;
    /**
     * The column counter for the grid gridpane.
     */
    public int col = 0;

    /**
     * The start method displays all the photos in the selected album
     * along with their captions.
     *
     * @param mainStage     The main stage where the application is running.
     * @param albumIndex    The index of where the current album is located in the
     *                      users list of albums.
     * @param userIndex     The index of where the current user is located in the
     *                      list of all users.
     */
    public void start(Stage mainStage, int albumIndex, int userIndex) {
        this.albumIndex = albumIndex;
        this.userIndex = userIndex;
        this.mainStage= mainStage;
        try {
            UsersList = Serialize.readApp();
        } catch (Exception e) {
            System.out.println("This should not appear since users array list will always have Stock user!");
            e.printStackTrace();
        }
        this.user = UsersList.get(userIndex);
        this.album = user.getAlbums().get(albumIndex);
        //this.album = user.getAlbums().get(albumIndex);
        System.out.println(user.getUsername());
        System.out.println(album.getName());
        albumName.setText("Welcome to " + album.getName() + ", " + user.getUsername() + "! Click the photo you would like to display or edit.");
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
        mainStage.setOnCloseRequest(event -> {
            try {
                Serialize.writeApp(UsersList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This method adds a tag to the photo displayed on the
     * right side display panel.
     *
     * @throws Exception
     */
    public void addTag() throws Exception{
        if (display_image.getImage() == null) {
            //there is no photo selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No photo is selected!");
            String content = ("Please select an image to edit.");
            alert.setContentText(content);
            alert.showAndWait();
        }else {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Add Tag");
            dialog.setHeaderText("You can add a new Tag key value pair or choose from your preset tags: " + user.printPreset());

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

                String temp1 = "" + pair.getValue();
                String temp2 = "" + pair.getKey();
                String key = temp2.trim();
                String value = temp1.trim();

                if(key.equals("") || value.equals("")){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Input Error");
                    String content = "No empty tags allowed";
                    alert.setContentText(content);
                    alert.showAndWait();
                    return;
                }
                System.out.println(key + " " + value);
                try {
                    Tag tag;
                    if (key.equals("location") || key.equals("weather")) {
                        tag = new Tag(key, value, false);
                    } else {
                        tag = new Tag(key, value, true);
                    }
                    String i = display_image.getId();
                    Photo photoInAlbum = null;
                    if (display_image.getImage() == null) {
                        /*there is no photo selected
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("No photo is selected!");
                        String content = ("Please select an image to edit.");
                        alert.setContentText(content);
                        alert.showAndWait();
                        */
                    } else {
                        //find the photo in the album that corresponds to the photo being edited
                        for (Photo p : album.getPhotos()) {
                            Photo tempPhoto = new Photo(new File(i));
                            if (p.sameImage(tempPhoto))
                                photoInAlbum = p;
                        }
                    }
                    photoInAlbum.addTag(tag);
                    user.addPreset(tag);
                    clearPhotoDisplay();
                    Serialize.writeApp(UsersList);
                    //user.printPreset();
                } catch (Exception error) {
                    if (error instanceof IllegalArgumentException) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Input Error");
                        String content = error.getMessage();
                        alert.setContentText(content);
                        alert.showAndWait();
                    }else{
                        error.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * This method deleted a tag from the displayed photo
     *  in the right side display panel.
     */
    public void deleteTag() {
        if (display_image.getImage() == null) {
            //there is no photo selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No photo is selected!");
            String content = ("Please select an image to edit.");
            alert.setContentText(content);
            alert.showAndWait();
        }else {
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
                    String temp4 = "" + pair.getValue();
                    String temp5 = "" + pair.getKey();
                    String key = temp5.trim();
                    String value = temp4.trim();
                    if(key.equals("") || value.equals("")){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Input Error");
                        String content = "Please enter a tag to delete";
                        alert.setContentText(content);
                        alert.showAndWait();
                        return;
                    }
                    System.out.println(key + " " + value);
                    Tag tag = new Tag(key, value, true);



                    String i = display_image.getId();
                    Photo photoInAlbum = null;

                    if (display_image.getImage() == null) {
                        //there is no photo selected
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("No photo is selected!");
                        String content = ("Please select an image to edit.");
                        alert.setContentText(content);
                        alert.showAndWait();
                    } else {
                        //find the photo in the album that corresponds to the photo being edited
                        for (Photo p : album.getPhotos()) {
                            if (p.sameImage(new Photo(new File(i))))
                                photoInAlbum = p;
                        }
                    }
                    photoInAlbum.deleteTag(tag);
                    user.deletePreset(tag);
                    clearPhotoDisplay();
                    Serialize.writeApp(UsersList);
                } catch (Exception error) {
                    if (error instanceof IllegalArgumentException) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Input Error");
                        String content = error.getMessage();
                        alert.setContentText(content);
                        alert.showAndWait();
                    } else{
                        error.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * This method clears out the display panel on the right side.
     */
    public void clearPhotoDisplay() {
        this.display_image.setImage(null);
        this.photos_caption.setText("Caption: ");
        this.photos_date.setText("Date: ");
        this.photos_tags.setText("Tags: ");
    }

    /**
     * This method adds a gripane from the Individual Photos fxml
     * that displays the photo in the current album.
     *
     * @param p     The photo that is shown inside the album.
     */
    public void populatePhotos(Photo p) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/individualphotos.fxml"));
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

    /**
     * This method takes the user back to the albums screen
     * where they can choose to view another album.
     *
     * @throws Exception
     */
    public void logout() throws Exception{
        //saves the users arraylist
        Serialize.writeApp(UsersList);

        Stage appStage=(Stage)logout_btn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/album.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        AlbumController controller = loader.getController();
        controller.start(appStage, userIndex);
        appStage.setScene(new Scene(root));
        appStage.show();

    }

    /**
     * This method adds a new photo to the album that the user
     * is currently viewing.
     *
     * @throws IOException
     */
    public void addPhoto() throws IOException {
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp", "*.jpeg");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
        File selectedFile = fileChooser.showOpenDialog(mainStage);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/individualphotos.fxml"));
        try {
            AnchorPane img = (AnchorPane)loader.load();
            IndividualPhotosController photoView = loader.getController();
            Photo newPhoto = null;
            for (Album a: this.user.getAlbums()) {
                for (Photo p: a.getPhotos()) {
                    if (p.sameImage(new Photo(selectedFile))) { //if the file paths are the same then the same photo is referenced in this new album
                        newPhoto = p;
                    }
                }
            }

            if (newPhoto == null) {
                newPhoto = new Photo(selectedFile);
            }

            photoView.start(this.mainStage,newPhoto, display_image, photos_caption, photos_date, photos_tags);

            try {
                this.album.addPhoto(newPhoto);
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
            if(ex instanceof NullPointerException){
                System.out.println("You pressed cancel");
            } else {
                System.out.println("catch error");
                ex.printStackTrace();
            }
        }
        Serialize.writeApp(UsersList);
    }

    /**
     * This method deletes all the photo views (gridpanes)
     * and calls the start method to repopulate the photo
     * views in the album.
     * <p>
     *     This method is called whenever an edit is made so this change
     *     can be reflected in all the photos and the display panel can
     *     also be updated.
     * </p>
     *
     * @throws Exception
     */
    public void resetPhotos() throws Exception {
        Serialize.writeApp(UsersList);
        this.row = 0;
        this.col = 0;
        Iterator<Node> iter = this.grid.getChildren().iterator();
        while (iter.hasNext()) {
            Node node = iter.next();
            iter.remove();
        }
        this.start(this.mainStage, this.albumIndex, this.userIndex);
    }

    /**
     * This method handles the transition from this current photos
     * view to the slideshow view so the user can view their photos in
     * a slideshow manner.
     *
     * @throws Exception
     */
    public void viewSlideshow() throws Exception {
        Stage appStage=this.mainStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/slideshow.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        SlideshowController controller = loader.getController();
        controller.start(this.mainStage, this.userIndex, this.albumIndex);
        appStage.setScene(new Scene(root));
        appStage.show();
    }

    /**
     * This method allows a user to edit a caption on the
     * currently displayed photo.
     *
     * @throws Exception
     */
    public void editCaption() throws Exception {
        //get the image that the change it being made on
        String i = display_image.getId();
        Photo photoInAlbum = null;
        if (display_image.getImage() == null) {
            //there is no photo selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No photo is selected!");
            String content = ("Please select an image to edit.");
            alert.setContentText(content);
            alert.showAndWait();
        } else {
            //find the photo in the album that corresponds to the photo being edited
            for (Photo p: album.getPhotos()) {
                if (p.sameImage(new Photo(new File(i))))
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
                    Serialize.writeApp(UsersList);
                    try {
                        resetPhotos();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } catch (Exception error) {
                    if(error instanceof IllegalArgumentException) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Input Error");
                        String content = error.getMessage();
                        alert.setContentText(content);
                        alert.showAndWait();
                    } else{
                        error.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * This method allows a user to delete the displayed photo --
     * effectively deleting it from the entire album itself.
     */
    public void deletePhoto() {
        String i = display_image.getId();
        Photo photoInAlbum = null;
        if (display_image.getImage() == null) {
            //there is no photo selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No photo is selected!");
            String content = ("Please select an image to edit.");
            alert.setContentText(content);
            alert.showAndWait();
        } else {
            //find the photo in the album that corresponds to the photo being edited
            for (Photo p: album.getPhotos()) {
                if (p.sameImage(new Photo(new File(i))))
                    photoInAlbum = p;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("You are deleting the selected photo!");
            alert.setContentText("Are you sure you want to delete this photo?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    this.album.deletePhoto(photoInAlbum);
                    clearPhotoDisplay();
                    Serialize.writeApp(UsersList);
                    try {
                        resetPhotos();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } catch (Exception error) {
                    if (error instanceof IllegalArgumentException) {
                        Alert alert1 = new Alert(Alert.AlertType.ERROR);
                        alert1.setTitle("Input Error");
                        String content = error.getMessage();
                        alert1.setContentText(content);
                        alert1.showAndWait();
                    } else {
                        error.printStackTrace();
                    }
                }
            } else {
                return;
            }
        }
    }

    /**
     * This method allows the user to move the displayed photo from
     * the current album to another existing album -- the photo is removed
     * from the current album.
     */
    public void movePhoto() {
        String i = display_image.getId();
        Photo photoInAlbum = null;
        if (display_image.getImage() == null) {
            //there is no photo selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No photo is selected!");
            String content = ("Please select an image to edit.");
            alert.setContentText(content);
            alert.showAndWait();
        } else {
            for (Photo p: album.getPhotos()) {
                if (p.sameImage(new Photo(new File(i))))
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
                    Serialize.writeApp(UsersList);
                    try {
                        resetPhotos();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } catch (Exception error) {
                    if(error instanceof IllegalArgumentException) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Input Error");
                        String content = error.getMessage();
                        alert.setContentText(content);
                        alert.showAndWait();
                    } else{
                        error.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * This method allows a user to copy the displayed photo
     * into another album but both the photo is not removed from
     * the current album.
     */
    public void copyPhoto() {
        String i = display_image.getId();
        Photo photoInAlbum = null;
        if (display_image.getImage() == null) {
            //there is no photo selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No photo is selected!");
            String content = ("Please select an image to edit.");
            alert.setContentText(content);
            alert.showAndWait();
        } else {
            for (Photo p: album.getPhotos()) {
                if (p.sameImage(new Photo(new File(i))))
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
                    Serialize.writeApp(UsersList);
                    try {
                        resetPhotos();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } catch (Exception error) {
                    if(error instanceof IllegalArgumentException) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Input Error");
                        String content = error.getMessage();
                        alert.setContentText(content);
                        alert.showAndWait();
                    } else{
                        error.printStackTrace();
                    }
                }
            }
        }

    }

}
