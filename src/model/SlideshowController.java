package model;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.image.Image;


public class SlideshowController {
    @FXML
    ImageView Image;

    public void start(Stage mainStage){
        Image i = new javafx.scene.image.Image("/view/image.jpg");
        Image.setImage(i);
    }
}

