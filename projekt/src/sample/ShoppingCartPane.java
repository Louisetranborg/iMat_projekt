package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingCart;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCartPane extends AnchorPane {

    private SearchController parentController;
    private ShoppingCart shoppingCart;
    @FXML private FlowPane cartFlowPane;
    @FXML private ScrollPane cartScrollPane;

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

        cartScrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() { //Gör så att man inte kan skrolla horisontiellt
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0){
                    event.consume();
                }
            }
        });

        createProductCartItems();
    }

    protected void createProductCartItems(){
        for(Product product: parentController.iMatDataHandler.getProducts()){
            ProductCartItem productCartItem = new ProductCartItem(new ShoppingItem(product),parentController);
            productCartItemMap.put(product.getName(),productCartItem);
        }
    }

}

