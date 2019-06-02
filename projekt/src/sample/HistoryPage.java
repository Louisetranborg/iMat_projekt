package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class HistoryPage extends AnchorPane {

    @FXML VBox historyOrderVbox;

    public HistoryPage() {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/historyPage.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);

            try {
                fxmlLoader.load();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }

        }
}
