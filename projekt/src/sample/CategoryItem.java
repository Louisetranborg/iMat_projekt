package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.io.IOException;

public class CategoryItem extends AnchorPane {

    private ProductCategory productCategory;
    private SearchController parentController;                      //Detta är vår searchcontroller för att komma åt imata
    @FXML private RadioButton categoryButton;                       //Detta är kategoriknappen med dess kategorinamn

    @FXML
    protected void onClick(Event event){        //När man klickar på en kategori skall varorna i mitten uppdateras
        parentController.updateProductPaneFromCategory(productCategory);
    }

    public CategoryItem(ProductCategory productCategory, SearchController parentController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("categoryItem.fxml")); //Laddar in rätt fxml-fil
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        } catch (IOException exception){
            throw new RuntimeException(exception);
        }

        this.productCategory = productCategory;
        this.parentController = parentController;
        categoryButton.setText(productCategory.name());
        categoryButton.setToggleGroup(parentController.toggleGroup); //Gör så att endast en knapp kan vara nedtryckt åt gången

        categoryButton.getStyleClass().remove("radio-button"); //Tar bort utseendet för radio-button
        categoryButton.getStyleClass().add("toggle-button");      //Ändrar utseendet så det ser ut som en kanpp
    }



}
