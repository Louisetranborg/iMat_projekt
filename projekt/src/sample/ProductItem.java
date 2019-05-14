package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;

import java.awt.*;
import java.io.IOException;

public class ProductItem extends AnchorPane {

    private SearchController parentController;
    private Product product;
    @FXML private Label name;
    @FXML private ImageView image;
    @FXML private Label price;

    @FXML
    protected void onClick(){
        parentController.openProductDetailView();
    }

    public ProductItem(Product product, SearchController parentController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("productItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        } catch (IOException exception){
            throw new RuntimeException(exception);
        }

        this.parentController = parentController;
        this.product = product;
        name.setText(product.getName());
        image.setImage(parentController.iMatDataHandler.getFXImage(product));
        price.setText(product.getPrice() + " " + product.getUnit());

    }

}
