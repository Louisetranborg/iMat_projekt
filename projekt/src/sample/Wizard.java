package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;

import java.io.IOException;

public class Wizard extends AnchorPane {

    private SearchController parentController;

    public Wizard(SearchController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/checkout.fxml"));        //Laddar in rätt fxml-fil
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

