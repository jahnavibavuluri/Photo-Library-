package controller;

import model.Photo;
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
 * performed on the Individual Photos fxml page.
 *
 * @author Jahnavi Bavuluri and Chiraag Rekhari
 */
public class IndividualPhotosController {

    @FXML ImageView ind_photos_image;
    @FXML GridPane photo_grid;
    @FXML Label ind_caption_label;

    /**
     * The main stage where the application will be running.
     */
    Stage mainStage;

    /**
     * The start method populates the individual photo gridpane
     * with the photo thumbnail and caption.
     *
     * @param mainStage     The stage where the application will be running.
     * @param photo         The photo object that is in the album.
     * @param image         The imageview that is being passed in from the photos fxml.
     * @param caption       The caption label that is being passed in from the photos fxml.
     * @param date          The date label that is being passed in from the photos fxml.
     * @param tags          The tags label that is being passed in from the photos fxml.
     */
    public void start(Stage mainStage, Photo photo, ImageView image, Label caption, Label date, Label tags) {
        this.mainStage = mainStage;
        ind_caption_label.setText((photo.getCaption() == null ? "" : photo.getCaption()));
        ind_caption_label.setWrapText(true);
        ind_photos_image.setImage(new Image(photo.getFile().toURI().toString()));
        photo_grid.setVisible(true);

        photo_grid.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            /**
             * The handle method detects if the photo was clicked
             * and displays the photo on the right side display panel.
             *
             * @param mouseEvent    the mouse event that allows the application to know
             *                      that the user has clicked on this photo
             */
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 1) {
                    System.out.println(photo.getCaption() + " was clicked!");
                    displayPhoto(photo, image, caption, date, tags);
                }
            }
        });
    }

    /**
     * This method displays the clicked photo on the right
     * hand side display panel.
     *
     * @param photo     The photo that is being displayed.
     * @param image     The imageview that is being passed in from the photos fxml.
     * @param caption   The caption label that is being passed in from the photos fxml.
     * @param date      The date label that is being passed in from the photos fxml.
     * @param tags      The tags label that is being passed in from the photos fxml.
     */
    public void displayPhoto(Photo photo, ImageView image, Label caption, Label date, Label tags) {
        caption.setWrapText(true);
        tags.setWrapText(true);
        image.setId(photo.getFile().getAbsolutePath());
        image.setImage(new Image(photo.getFile().toURI().toString()));
        caption.setText((photo.getCaption() == null ? "Caption: " : photo.getCaption()));
        date.setText((photo.getDate() == null ? "Date: " : photo.getDate()));
        tags.setText(photo.getTags() == null ? "Tags: " : photo.getDisplayTags());
    }

}

