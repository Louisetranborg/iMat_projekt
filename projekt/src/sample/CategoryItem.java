package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.io.IOException;

public class CategoryItem extends AnchorPane {

    private ProductCategory productCategory;
    private SearchController searchController;
    @FXML private Label categoryLabel;

    public CategoryItem(ProductCategory productCategory, SearchController searchController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("categoryItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        } catch (IOException exception){
            throw new RuntimeException(exception);
        }

        this.productCategory = productCategory;
        this.searchController = searchController;
        categoryLabel.setText(productCategory.name());

    }

}
