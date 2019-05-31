package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MenuItem extends AnchorPane {

    protected SearchController parentController; //Kolla om vi verkligen vill låsa det till MainWindow.
    FXMLLoader fxmlLoader;

    @FXML
    protected RadioButton categoryButton;   //Detta är kategoriknappen med dess kategorinamn

    public MenuItem(SearchController parentController){
        fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/menuItem.fxml")); //Laddar in rätt fxml-fil
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        } catch (IOException exception){
            throw new RuntimeException(exception);
        }

        this.parentController = parentController;
        categoryButton.setToggleGroup(parentController.menuToggleGroup); //Gör så att endast en knapp kan vara nedtryckt åt gången
        categoryButton.getStyleClass().remove("radio-button"); //Tar bort utseendet för radio-button
        categoryButton.getStyleClass().add("toggle-button");      //Ändrar utseendet så det ser ut som en kanpp
    }
}
