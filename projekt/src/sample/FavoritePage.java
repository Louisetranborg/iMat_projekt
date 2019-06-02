package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;

import java.io.IOException;

public class FavoritePage extends AnchorPane {

    @FXML
    TilePane historyTilePane;

    public FavoritePage() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/favoritePage.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }
}
