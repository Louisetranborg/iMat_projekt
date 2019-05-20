package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import static java.lang.System.out;

import java.io.IOException;

public class ProductItem extends AnchorPane {

    private SearchController parentController;
    private Product product;
    @FXML private Label name;
    @FXML private ImageView image;
    @FXML private Label price;
    @FXML private ImageView addButton;
    @FXML private ImageView removeButton;

    @FXML
    protected void onClick(){ //När man klickar på ett productItem skall info om produkten komma upp
        parentController.openProductDetailView(product);
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

    @FXML
    protected void clickedOnAddButton(Event event){
        parentController.mouseTrap(event);
        parentController.shoppingCartPane.addProductToCart(product);
    }

}
