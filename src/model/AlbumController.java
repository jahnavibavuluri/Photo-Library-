package model;

import app.Album;
import app.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.util.Pair;

import java.io.EOFException;
import java.io.IOException;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;


public class AlbumController {

    public ArrayList<User> UsersList;
    public ArrayList<Album> albums;
    private User user;
    public int userIndex;
    public Stage mainStage;
    public int row = 0;
    public int col = 0;

    @FXML ScrollPane scroll;
    @FXML GridPane grid;
    @FXML Button logout_btn;
    @FXML Label username;


    public void start(Stage mainStage, int userIndex) {
        this.mainStage = mainStage;
        this.userIndex = userIndex;
        try {
            UsersList = Serialize.readApp();
        } catch (Exception e) {
            System.out.println("This should not appear since users array list will always have Stock user!");
            e.printStackTrace();
        }
        System.out.println(UsersList.toString());
        user = UsersList.get(userIndex);
        //System.out.println(UsersList);
        albums = user.getAlbums();
        username.setText("Welcome to the albums in your photo library, " + user.getUsername() + "! Double click on the album you wish to enter.");
        System.out.println(user.getUsername());
        System.out.println(user.numberOfAlbums());
        System.out.println(user.getAlbums());
        if (user.numberOfAlbums() > 0) {
            for (int i = 0; i<albums.size(); i++) {
            //for (Album a : user.getAlbums()) {
                try {
                    populateAlbums(i);
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


    public void populateAlbums(int displayAlbumIndex) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/IndividualAlbumController.fxml"));
        try {
            AnchorPane img = (AnchorPane) loader.load();
            IndividualAlbumController albumView = loader.getController();
            albumView.start(mainStage, displayAlbumIndex, userIndex);
            grid.add(albumView.album_grid, col, row);
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

    public void logout(ActionEvent event) throws Exception {
        //saves the users arraylist
        Serialize.writeApp(UsersList);
        System.out.println(user.getAlbums());

        Stage appStage = (Stage) logout_btn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/login.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        LoginController controller = loader.getController();
        controller.start(appStage);
        appStage.setScene(new Scene(root));
        appStage.show();
    }

    public void addAlbum(ActionEvent e) throws Exception {
        TextInputDialog addAlbum = new TextInputDialog();
        addAlbum.initOwner(this.mainStage);
        addAlbum.setTitle("New Album");
        addAlbum.setHeaderText("Name of new album");
        Optional<String> result = addAlbum.showAndWait();
        if (result.isPresent()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/IndividualAlbumController.fxml"));
            try {
                AnchorPane img = (AnchorPane) loader.load();
                IndividualAlbumController albumView = loader.getController();
                Album newAlbum = new Album(result.get());
                try {
                    user.addAlbum(newAlbum);
                    Serialize.writeApp(UsersList);
                    System.out.println(albums.size());
                    System.out.println(albums.toString());
                    albumView.start(mainStage, albums.size()-1, userIndex);
                    grid.add(albumView.album_grid, col, row);
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
                ex.printStackTrace();
            }
        }
        Serialize.writeApp(UsersList);
    }

    public void editAlbum() throws Exception {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Album Name");

        // Set the button types.
        ButtonType done = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(done, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField from = new TextField();
        from.setPromptText("Old Name");
        TextField to = new TextField();
        to.setPromptText("New Name");

        gridPane.add(new Label("Old Name:"), 0, 0);
        gridPane.add(from, 1, 0);
        gridPane.add(new Label("New Name:"), 2, 0);
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

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(pair -> {
            //System.out.println(pair.getKey() + " " + pair.getValue());
            Album a = user.getAlbumWithName(pair.getKey());
            try {
                user.editAlbum(pair.getKey() + "", pair.getValue() + "");
                System.out.println(a.getName());
                //Node node = getNode(pair.getKey());
                System.out.println("the old album was: " + pair.getKey() + "\n the new albums is: " + pair.getValue());
                try {
                    resetAlbums();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IllegalArgumentException error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                String content = error.getMessage();
                alert.setContentText(content);
                alert.showAndWait();
            }

        });
    }

    public void deleteAlbum(ActionEvent e) throws Exception {
        TextInputDialog deletingAlbum = new TextInputDialog();
        deletingAlbum.initOwner(this.mainStage);
        deletingAlbum.setTitle("Delete Album");
        deletingAlbum.setHeaderText("Please enter the name of the album you would like to delete.");
        Optional<String> result = deletingAlbum.showAndWait();
        if (result.isPresent()) {
            try {
                System.out.println("the album being deleted is: " + result.get());
                user.deleteAlbum(result.get());
                Serialize.writeApp(UsersList);
                resetAlbums();
            } catch (IllegalArgumentException error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                String content = error.getMessage();
                alert.setContentText(content);
                alert.showAndWait();
            }
        }
    }

    public void resetAlbums() throws Exception{
        Serialize.writeApp(UsersList);
        this.row = 0;
        this.col = 0;
        Iterator<Node> iter = this.grid.getChildren().iterator();
        while (iter.hasNext()) {
            Node node = iter.next();
            iter.remove();
        }
        this.start(this.mainStage, userIndex);
    }

    public void searchPhotos(ActionEvent e) throws Exception {
        Stage appStage=this.mainStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/search.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        SearchController controller = loader.getController();
        controller.start(this.mainStage, userIndex);
        appStage.setScene(new Scene(root));
        appStage.show();

    }

}


/*
    public void setImage(ActionEvent e) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/IndividualAlbumController.fxml"));
        try {
            AnchorPane img = (AnchorPane)loader.load();
            IndividualAlbumController test = loader.getController();
            test.album_grid.setVisible(true);
            Image i = new javafx.scene.image.Image("/view/image.jpg");
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

 GridPane album_grid = new GridPane();
        album_grid.addRow(100);
        album_grid.addRow(33);
        album_grid.addRow(34);
        album_grid.addRow(33);
        album_grid.setPrefHeight(200);
        album_grid.setPrefWidth(190);
        ImageView imgView = new ImageView();
        imgView.setFitWidth(100);
        imgView.setFitHeight(100);
        Image i = new javafx.scene.image.Image("/view/image.jpg");
        imgView.setImage(i);
        album_grid.add(imgView, 0,0);
        Label name = new Label();
        name.setText("Name of album");
        album_grid.add(name,0,1);
        Label number = new Label();
        number.setText("number of pictures in album");
        album_grid.add(number,0,2);
        Label date = new Label();
        date.setText("Date of album");
        album_grid.add(date,0,3);
        grid.add(album_grid, col, row);
        if (col == 2) {
            row++;
            col = 0;
        } else {
            col++;
        }*/