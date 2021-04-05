package model;

import app.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SearchController {

    @FXML
    GridPane grid;

    @FXML
    ImageView image2;

    public int row = 0;
    public int col = 0;
    Stage mainStage;
    User user;

    public void start(Stage mainstage, User user) {
        this.mainStage = mainstage;
        this.user = user;
    }

    public void backToAlbums() throws Exception {
        Stage appStage=this.mainStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/album.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        AlbumController controller = loader.getController();
        controller.start(appStage,this.user);
        appStage.setScene(new Scene(root));
        appStage.show();
    }

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
