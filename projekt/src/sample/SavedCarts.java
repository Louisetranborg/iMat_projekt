package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SavedCarts extends AnchorPane {

    private SearchController parentController;

    public SavedCarts(SearchController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("savedCarts.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);


        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }


        this.parentController = parentController;

    }

}
