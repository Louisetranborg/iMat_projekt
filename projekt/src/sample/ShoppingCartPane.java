package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingCart;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ShoppingCartPane extends AnchorPane {

    private SearchController parentController;
    private ShoppingCart shoppingCart;
    @FXML private FlowPane cartFlowPane;
    @FXML private StackPane stackPane;
    @FXML private AnchorPane cartPane;

    private Map<String, ProductCartItem> productCartItemMap = new HashMap<String, ProductCartItem>();

    public ShoppingCartPane(ShoppingCart shoppingCart, SearchController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cartPane.fxml")); //Laddar in rätt fxml-fil
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.shoppingCart = shoppingCart;
        this.parentController = parentController;

        createProductCartItems();

    }

    protected void createProductCartItems(){
        for(Product product: parentController.iMatDataHandler.getProducts()){
            ProductCartItem productCartItem = new ProductCartItem(new ShoppingItem(product),parentController);
            productCartItemMap.put(product.getName(),productCartItem);
            cartFlowPane.getChildren().add(productCartItem);
        }
    }

}

