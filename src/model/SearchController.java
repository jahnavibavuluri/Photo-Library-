package model;

import app.Album;
import app.Tag;
import app.Photo;
import app.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.time.LocalDate;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/IndividualAlbumController.fxml"));
        try {
            AnchorPane img = (AnchorPane) loader.load();
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
 */

public class SearchController {

    @FXML
    GridPane grid;

    @FXML
    ImageView image2;

    public int row = 0;
    public int col = 0;
    public Stage mainStage;
    private User user;
    public int userIndex;
    public ArrayList<User> UsersList;
    int z;
    ArrayList<Album> allUserAlbums;
    Album newAlbum;

    public void start(Stage mainstage, int userIndex) {
        this.mainStage = mainstage;
        this.userIndex = userIndex;

        newAlbum = new Album("");
        try {
            UsersList = Serialize.readApp();
        } catch (Exception e) {
            System.out.println("This should not appear since users array list will always have Stock user!");
            e.printStackTrace();
        }
        this.user = UsersList.get(userIndex);
        allUserAlbums = user.getAlbums();
        mainStage.setOnCloseRequest(event -> {
            try {
                Serialize.writeApp(UsersList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }



    public void backToAlbums() throws Exception {
        Stage appStage = this.mainStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/album.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        AlbumController controller = loader.getController();
        controller.start(appStage, this.userIndex);
        appStage.setScene(new Scene(root));
        appStage.show();
    }

    public void setImage(ActionEvent e)throws Exception {
        TextInputDialog addAlbum = new TextInputDialog();
        addAlbum.initOwner(this.mainStage);
        addAlbum.setTitle("Create album from search");
        addAlbum.setHeaderText("Name of new album");
        Optional<String> result = addAlbum.showAndWait();
        if (result.isPresent()) {
            System.out.println("im here");
            newAlbum.setName(result.get());
            user.getAlbums().add(newAlbum);
        }
        Serialize.writeApp(UsersList);
    }
    //we need this becuae we cant set anything in the lambda function and i need to be able to tell if they click AND or OR
    public void setter(int k){
        z = k;
    }

    public void searchBy2Tag(ActionEvent e) {
        Dialog<Pair<String, String>> first = new Dialog<>();
        Dialog<Pair<String, String>> second = new Dialog<>();

        Dialog<Pair<Pair, Pair>> dialog = new Dialog<>();

        dialog.setTitle("Enter tag to search by");

        ButtonType done = new ButtonType("AND", ButtonBar.ButtonData.OK_DONE);
        ButtonType done2 = new ButtonType("OR", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(done, done2, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField from = new TextField();
        from.setPromptText("Key");
        TextField to = new TextField();
        to.setPromptText("Value");
        TextField from1 = new TextField();
        from1.setPromptText("Key2");
        TextField to1 = new TextField();
        to1.setPromptText("Value2");

        gridPane.add(new Label("Key:"), 0, 0);
        gridPane.add(from, 1, 0);
        gridPane.add(new Label("Value:"), 2, 0);
        gridPane.add(to, 3, 0);
        gridPane.add(new Label("Key2:"), 4, 0);
        gridPane.add(from1, 5, 0);
        gridPane.add(new Label("Value2:"), 6, 0);
        gridPane.add(to1, 7, 0);

        dialog.getDialogPane().setContent(gridPane);
        Platform.runLater(() -> from.requestFocus());


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == done) {
                setter(1);
                return new Pair<Pair,Pair>(new Pair<>(from.getText(), to.getText()), new Pair<>(from1.getText().toLowerCase(), to1.getText().toLowerCase()));
            }
            if (dialogButton == done2) {
                setter(2);
                return new Pair<Pair,Pair>(new Pair<>(from.getText(), to.getText()), new Pair<>(from1.getText().toLowerCase(), to1.getText().toLowerCase()));
            }
            return null;
        });

        Optional<Pair<Pair, Pair>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            //1 = AND
            //2 = OR

            String key1 = pair.getKey().getKey() + "";
            String value1 = pair.getKey().getValue() + "";
            String key2 = pair.getValue().getKey() + "";
            String value2 = pair.getValue().getValue() + "";
            //System.out.println(key1 + " " + value1+ " " + key2 + " " + value2);
            ArrayList<Album> albums = user.getAlbums();
            for( int i =0; i<albums.size(); i++){
                Album a = albums.get(i);
                ArrayList<Photo> photos = a.getPhotos();
                for(int j =0; j<photos.size(); j++){
                    Photo p = photos.get(j);
                    ArrayList<Tag> tags = p.getTags();
                    boolean temp = false;
                    boolean temp2 = false;
                    for(int k =0; k<tags.size(); k++){
                        System.out.println("z = " + z);
                        if(z == 1) {
                            System.out.println("I am in AND");
                            if (key1.equals(tags.get(k).getKey()) && value1.equals(tags.get(k).getValue())) {temp = true;}
                            if(key2.equals(tags.get(k).getKey()) && value2.equals(tags.get(k).getValue())){temp2 = true;}
                            if(temp && temp2){
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("/view/IndividualSearchController.fxml"));
                                try {
                                    if(newAlbum.addPhotoBoolean(newAlbum.getPhotos(), p)){
                                        newAlbum.getPhotos().add(p);
                                    }else{
                                        continue;
                                    }
                                    AnchorPane img = (AnchorPane)loader.load();
                                    IndividualSearchController searchView = loader.getController();
                                    searchView.start(mainStage, p);
                                    grid.add(searchView.search_grid, col, row);
                                    if (col == 2) {
                                        row++;
                                        col = 0;
                                    } else {
                                        col++;
                                    }

                                } catch (Exception q){
                                    q.printStackTrace();
                                    break;
                                }
                                continue;
                            }
                        }
                        if(z==2){
                            System.out.println("I am in OR");
                            if ((key1.equals(tags.get(k).getKey()) && value1.equals(tags.get(k).getValue())) || (key2.equals(tags.get(k).getKey()) && value2.equals(tags.get(k).getValue()))) {
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("/view/IndividualSearchController.fxml"));
                                try {
                                    if(newAlbum.addPhotoBoolean(newAlbum.getPhotos(), p)){
                                        newAlbum.getPhotos().add(p);
                                    }else{
                                        continue;
                                    }
                                    AnchorPane img = (AnchorPane)loader.load();
                                    IndividualSearchController searchView = loader.getController();
                                    searchView.start(mainStage, p);
                                    grid.add(searchView.search_grid, col, row);
                                    if (col == 2) {
                                        row++;
                                        col = 0;
                                    } else {
                                        col++;
                                    }
                                } catch (Exception q){
                                    q.printStackTrace();
                                    continue;
                                }
                                continue;
                            }
                        }
                    }
                }
            }
            if(newAlbum.getPhotos().size() == 0){
                System.out.println("Could not find tag");
            }
             // turn this into dialog box
        });
    }


    public void searchBy1Tag(ActionEvent e) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Enter tag to search by");

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

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            String key = pair.getKey() + "";
            String value = pair.getValue() + "";
            ArrayList<Album> albums = user.getAlbums();
            for( int i =0; i<albums.size(); i++){
                Album a = albums.get(i);
                ArrayList<Photo> photos = a.getPhotos();
                for(int j =0; j<photos.size(); j++){
                    Photo p = photos.get(j);
                    ArrayList<Tag> tags = p.getTags();
                    for(int k =0; k<tags.size(); k++){

                        if(key.equals(tags.get(k).getKey()) && value.equals(tags.get(k).getValue())){

                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/view/IndividualSearchController.fxml"));
                            try {
                                if(newAlbum.addPhotoBoolean(newAlbum.getPhotos(), p)){
                                    newAlbum.getPhotos().add(p);
                                }else{
                                    continue;
                                }
                                AnchorPane img = (AnchorPane)loader.load();
                                IndividualSearchController searchView = loader.getController();
                                searchView.start(mainStage, p);
                                grid.add(searchView.search_grid, col, row);
                                if (col == 2) {
                                    row++;
                                    col = 0;
                                } else {
                                    col++;
                                }

                            } catch (Exception q){
                                q.printStackTrace();
                            }
                        }
                    }
                }
            }

        });
    }



    public void searchByDate(ActionEvent e){

    }
}
