package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;

import java.io.IOException;

public class HistoryItems extends AnchorPane {

    @FXML TilePane historyProductTilePane;

    SearchController parentController;

    public HistoryItems(SearchController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/historyItems.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.parentController = parentController;

    }

    @FXML
    protected void onClickAddHistoryToCart(){
        parentController.addAllHistoryItemsToCart();
    }

    @FXML
    protected void onClickBackToHistory(){
        parentController.productScrollPane.setVvalue(parentController.actualVValue);
        //TODO fixa så att man endast kallar en activate/showHistoryPage.
        parentController.productScrollPane.setContent(parentController.historyPage);
    }

}
