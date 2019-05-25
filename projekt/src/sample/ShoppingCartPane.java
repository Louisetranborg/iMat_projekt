package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import se.chalmers.cse.dat216.project.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCartPane extends AnchorPane {

    private SearchController parentController;
    private ShoppingCart shoppingCart;
    @FXML private FlowPane cartFlowPane;
    @FXML private ScrollPane cartScrollPane;
    @FXML private StackPane stackPane;
    @FXML private AnchorPane cartPane;
    @FXML private Label totalLabel;
    @FXML private Button toCheckoutButton;
    @FXML private AnchorPane cartFlowPaneWrap;

    private Map<String, CartItem> productCartItemMap = new HashMap<String, CartItem>();
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");


    @FXML
    private void clickOnToCheckoutButton(){
        parentController.wizardToFront();
    }

    protected FlowPane getCartFlowPane(){
        return cartFlowPane;
    }

    protected AnchorPane getCartFlowPaneWrap(){
        return cartFlowPaneWrap;
    }

    public ShoppingCartPane(ShoppingCart shoppingCart, SearchController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/cartPane.fxml"));        //Laddar in rätt fxml-fil
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.shoppingCart = shoppingCart;
        this.parentController = parentController;

        shoppingCart.addShoppingCartListener(new ShoppingCartListener() {
            @Override
            public void shoppingCartChanged(CartEvent cartEvent) {
               updateCart();
            }
        });

        //Gör så att man inte kan skrolla horisontiellt i kategorierna


        cartScrollPane.addEventFilter(ScrollEvent.SCROLL,new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0) {
                    event.consume();
                }
            }
        });


        totalLabel.setText("Totalt " + decimalFormat.format(shoppingCart.getTotal()));

    }

    //Skapar alla våra productCartItems och lägger dem i en Map(productCartItemMap)
    protected void createProductCartItems(){
        for(Product product: parentController.iMatDataHandler.getProducts()){
            CartItem cartItem = new CartItem(parentController.shoppingItemMap.get(product.getName()),parentController);
            productCartItemMap.put(product.getName(),cartItem);
        }
    }

    protected void addProductToCart(ShoppingItem shoppingItem){ //Lägger endast ut ett cartItem när det behövs
        if(!shoppingCart.getItems().contains(shoppingItem)){
            shoppingCart.addItem(shoppingItem);
        }
    }

    protected void removeProductFromCart(ShoppingItem shoppingItem){
        shoppingCart.removeItem(shoppingItem);
    }

    protected Map<String,CartItem> getProductCartItemMap(){
        return productCartItemMap;
    }

    protected void updateCart(){
        cartFlowPane.getChildren().clear();
        for(ShoppingItem shoppingItem : shoppingCart.getItems()){
            cartFlowPane.getChildren().add(productCartItemMap.get(shoppingItem.getProduct().getName()));
        }
        totalLabel.setText("Totalt " + decimalFormat.format(shoppingCart.getTotal()) + " kr");
    }


}

