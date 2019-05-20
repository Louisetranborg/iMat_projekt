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
import static java.lang.System.out;

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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cartPane.fxml"));        //Laddar in rätt fxml-fil
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

    private void createProductCartItems(){
        for(Product product: parentController.iMatDataHandler.getProducts()){
            ProductCartItem productCartItem = new ProductCartItem(new ShoppingItem(product),parentController);
            productCartItemMap.put(product.getName(),productCartItem);

        }
    }

    protected void addProductToCart(Product product){
        if(cartFlowPane.getChildren().contains(productCartItemMap.get(product.getName()))){
            shoppingCart.addItem(productCartItemMap.get(product.getName()).getShoppingItem());
            productCartItemMap.get(product.getName()).getShoppingItem().setAmount(productCartItemMap.get(product.getName()).getShoppingItem().getAmount() + 1);
            out.print("två " + productCartItemMap.get(product.getName()).getShoppingItem().getAmount());
        }
        else{
            cartFlowPane.getChildren().add(productCartItemMap.get(product.getName()));
            shoppingCart.addProduct(product);
            out.print("ett" + productCartItemMap.get(product.getName()).getShoppingItem().getAmount());
        }
    }

}

