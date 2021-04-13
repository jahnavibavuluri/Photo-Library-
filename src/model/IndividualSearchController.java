package model;

import app.Photo;
import app.Tag;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * This class takes care of the elements and events
 * performed on the Individual Search fxml page.
 *
 * @author Jahnavi Bavuluri and Chiraag Rekhari
 */
public class IndividualSearchController {
    @FXML GridPane search_grid;
    @FXML ImageView ind_search_image;
    @FXML Label ind_search_label;

    /**
     * The main stage where the application will be running.
     */
    Stage mainStage;

    /**
     * This method populates the individual search gridpane
     * with the photo and the caption.
     *
     * @param mainStage     The stage where the application will be running.
     * @param photo         The photo that shows up in the users search results.
     */
    public void start(Stage mainStage, Photo photo){
        this.mainStage = mainStage;
        ind_search_label.setText((photo.getCaption() == null ? "Caption: " : "Caption: " + photo.getCaption()));
        ind_search_image.setImage(new Image(photo.getFile().toURI().toString()));
        search_grid.setVisible(true);
    }
}
