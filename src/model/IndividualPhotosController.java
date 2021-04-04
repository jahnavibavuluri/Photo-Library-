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

public class IndividualPhotosController {

    @FXML ImageView ind_photos_image;
    @FXML GridPane photo_grid;
    @FXML Label ind_caption_label;

    Stage mainStage;

    public void start(Stage mainStage, Photo photo, ImageView image, Label caption, Label date, Label tags) {
        this.mainStage = mainStage;
        ind_caption_label.setText((photo.getCaption() == null ? "No caption" : photo.getCaption()));
        ind_photos_image.setImage(photo.getImage());
        photo_grid.setVisible(true);

        photo_grid.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 1) {
                    System.out.println(photo.getCaption() + " was clicked!");
                    displayPhoto(photo, image, caption, date, tags);
                }
            }
        });
    }

    public void displayPhoto(Photo photo, ImageView image, Label caption, Label date, Label tags) {
        image.setImage(photo.getImage());
        caption.setText((photo.getCaption() == null ? "No caption" : photo.getCaption()));
        StringBuilder allTags = new StringBuilder();
        date.setText((photo.getDate() == null ? "No date" : photo.getDate()));
        for (Tag tag: photo.getTags()) {
            allTags.append(tag.toString());
        }
        tags.setText(allTags.toString());
    }

}

