package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class ProductCartItem extends AnchorPane {

    private SearchController parentController;
    private ShoppingItem shoppingItem;
    @FXML private ImageView cartImage;
    @FXML private Label cartName;

    public ProductCartItem(ShoppingItem shoppingItem, SearchController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("productCartItem.fxml")); //Laddar in rätt fxml-fil
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.shoppingItem = shoppingItem;
        this.parentController = parentController;
        cartImage.setImage(parentController.iMatDataHandler.getFXImage(shoppingItem.getProduct()));
        cartName.setText(shoppingItem.getProduct().getName());

    }


}
