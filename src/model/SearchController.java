package model;

import app.Album;
import app.Tag;
import app.Photo;
import app.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.time.ZoneId;
import java.util.*;
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


/**
 * This is responsible for controlling the search scene for when a user wants to search from their existing albums and photos
 *
 * @author Chiraag rekhari
 * @author Jahnavi Bavuluri
 */
public class SearchController {

    @FXML
    GridPane grid;
    /**
     * row that the photo image will be displayed in the gridpane
     */
    public int row = 0;
    /**
     * col that the photo image will be displayed in the gridpane
     */
    public int col = 0;
    /**
     *The stage passed in
     */
    public Stage mainStage;
    /**
     * current user working on
     */
    private User user;
    /**
     * the index of the user in the user ArrayList
     */
    public int userIndex;
    /**
     * Arraylist of all users in the application
     */
    public ArrayList<User> UsersList;
    /**
     * temp variable to tell if "AND" or "OR" button are pressed in searchBy2Tag method
     */
    public int z;
    /**
     * ArrayList of all albums a user currently has
     */
    public ArrayList<Album> allUserAlbums;
    /**
     * newAlbum to be created if the user wants to create an Album from their search results
     */
    public Album newAlbum;
    /**
     * the start date of date range search
     */
    public LocalDate from;
    /**
     * the end date of the date range search
     */
    public LocalDate to;

    /**
     * Responsible for starting the SearchController
     *
     * @param mainstage The stage passed in
     * @param userIndex the index of the user in the user ArrayList
     */
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

    /**
     * Responsible for handling the controller of the back button which brings the user back to the album scene
     *
     * @throws Exception
     */
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

    /**
     * Creates a new album from the search results and add it the current users total albums
     *
     * @throws Exception
     */
    public void setImage()throws Exception {
        if (newAlbum.getPhotos().size() != 0) {
            TextInputDialog addAlbum = new TextInputDialog();
            addAlbum.initOwner(this.mainStage);
            addAlbum.setTitle("Create album from search");
            addAlbum.setHeaderText("Name of new album");
            Optional<String> result = addAlbum.showAndWait();
            if (result.isPresent()) {
                System.out.println("im here");
                newAlbum.setName(result.get().trim());
                try {
                        user.addAlbum(newAlbum);
                        newAlbum = new Album("");
                        clearPhotos();
                } catch (IllegalArgumentException error) {
                    System.out.println("the error is happening here!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Input Error");
                    String content = error.getMessage();
                    alert.setContentText(content);
                    alert.showAndWait();
                }

            }
            Serialize.writeApp(UsersList);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            String content = "Please search for photos before creating a new album!";
            alert.setContentText(content);
            alert.showAndWait();
        }

    }
    //we need this becuae we cant set anything in the lambda function and i need to be able to tell if they click AND or OR
    /**
     * Sets z to either 1 or 2 so that it is possible to tell if "AND" or "OR" is pressed in the lambda function
     *
     * @param k sets z to either 1 or 2 depending if "AND" or "OR" is pressed
     */
    public void setter(int k){
        z = k;
    }

    /**
     * Controls the Search By 2 Tags button which allows the user to search by two tags and decide if they want to do it use "AND" or "OR"
     */
    public void searchBy2Tag() {
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

            String temp1 = (pair.getKey().getKey()) + "";
            String key1 = temp1.trim();

            String temp7 = pair.getKey().getValue() + "";
            String value1 = temp7.trim();

            String temp3 = pair.getValue().getKey() + "";
            String key2 = temp3.trim();

            String temp4 = pair.getValue().getValue() + "";
            String value2 = temp4.trim();

            if(key1.equals("") || value1.equals("") || key2.equals("")|| value2.equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                String content = "1 or more tags were not inputted. Please try again";
                alert.setContentText(content);
                alert.showAndWait();
                return;
            }
            System.out.println(key1 + " " + value1+ " " + key2 + " " + value2);
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

    /**
     * Controls the Search By 1 Tag button which allows the user to search their entire photo list by 1 key and 1 value
     */
    public void searchBy1Tag() {
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
            if(key.equals("") || value.equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                String content = "1 or more tags were not inputted. Please try again";
                alert.setContentText(content);
                alert.showAndWait();
                return;
            }
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

    /**
     * controls the clear button which clears the whole gridpane so the user can start searching from scratch
     * @throws Exception
     */
    public void clearPhotos() throws Exception {
        Serialize.writeApp(UsersList);
        this.row = 0;
        this.col = 0;
        Iterator<Node> iter = this.grid.getChildren().iterator();
        while (iter.hasNext()) {
            Node node = iter.next();
            iter.remove();
        }
    }

    /**
     * setter method to set variable "from" to the start date
     * @param date The date that from will be set to
     */
    public void setterFrom(LocalDate date){
        from = date;
    }

    /**
     * setter method to set variable "to to the end date
     * @param date The date that to will be set to
     */
    public void setterTo(LocalDate date){
        to = date;
    }

    /**
     * Controls the Search By Date Range Button which allows the user to search from a date range by selecting from 2 DatePickers
     * @throws Exception
     */
    public void searchByDate() throws Exception {
        TextInputDialog dateSearch = new TextInputDialog();
        dateSearch.initOwner(this.mainStage);
        TilePane r = new TilePane();
        dateSearch.getDialogPane().setContent(r);

        Label fromLabel = new Label("From:");
        Label toLabel = new Label("To:");
        // create a date picker
        DatePicker fromDate = new DatePicker();
        // action event
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                // get the date picker value
                setterFrom(fromDate.getValue());
            }
        };

        // show week numbers
        fromDate.setShowWeekNumbers(true);

        // when datePicker is pressed
        fromDate.setOnAction(event);
        //--------------------------------------------------------
        DatePicker toDate = new DatePicker();

        // action event
        EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                // get the date picker value
                setterTo(toDate.getValue());

                // get the selected date
                //k.setText("Date :" + i);
            }
        };

        // show week numbers
        toDate.setShowWeekNumbers(true);

        // when datePicker is pressed
        toDate.setOnAction(event2);
        //-----------------------------------------------------

        // add button and label
        r.getChildren().add(fromLabel);
        r.getChildren().add(fromDate);
        r.getChildren().add(toLabel);
        r.getChildren().add(toDate);


        // create a scene

        Optional<String> result = dateSearch.showAndWait();
        if (result.isPresent()) {
            System.out.println(from + " " + to);
            if (from == null || to == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                String content = "2 Tags were not inputted. Please try again";
                alert.setContentText(content);
                alert.showAndWait();
                return;
            }
            if (from.isAfter(to)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                String content = "Please Fix the Order of the Start and End Date!";
                alert.setContentText(content);
                alert.showAndWait();
                return;
            }
            ArrayList<Album> albums = user.getAlbums();
            for (int i = 0; i < albums.size(); i++) {
                Album a = albums.get(i);
                ArrayList<Photo> photos = a.getPhotos();
                for (int j = 0; j < photos.size(); j++) {
                    Photo p = photos.get(j);
                    Date tempDate = p.getActualDate();
                    LocalDate date = tempDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if (from.isBefore(to)) {
                        if (from.isBefore(date) && to.isAfter(date) || from.isEqual(date) || to.isEqual(date)) {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/view/IndividualSearchController.fxml"));
                            try {
                                if (newAlbum.addPhotoBoolean(newAlbum.getPhotos(), p)) {
                                    newAlbum.getPhotos().add(p);
                                } else {
                                    continue;
                                }
                                AnchorPane img = (AnchorPane) loader.load();
                                IndividualSearchController searchView = loader.getController();
                                searchView.start(mainStage, p);
                                grid.add(searchView.search_grid, col, row);
                                if (col == 2) {
                                    row++;
                                    col = 0;
                                } else {
                                    col++;
                                }

                            } catch (Exception q) {
                                q.printStackTrace();
                            }
                        }
                    }
                    if (from.isEqual(to)) {
                        if (from.isEqual(date) && to.isEqual(date)) {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/view/IndividualSearchController.fxml"));
                            try {
                                if (newAlbum.addPhotoBoolean(newAlbum.getPhotos(), p)) {
                                    newAlbum.getPhotos().add(p);
                                } else {
                                    continue;
                                }
                                AnchorPane img = (AnchorPane) loader.load();
                                IndividualSearchController searchView = loader.getController();
                                searchView.start(mainStage, p);
                                grid.add(searchView.search_grid, col, row);
                                if (col == 2) {
                                    row++;
                                    col = 0;
                                } else {
                                    col++;
                                }

                            } catch (Exception q) {
                                q.printStackTrace();
                            }
                        }

                    }
                }
            }
        }
    }


}
