package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class PersonalAccount extends AnchorPane {

    private SearchController parentController;

    public PersonalAccount(SearchController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("personalAccount.fxml"));
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
