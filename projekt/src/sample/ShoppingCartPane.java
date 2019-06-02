package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import se.chalmers.cse.dat216.project.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
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
    @FXML private Button emptyCart;
    @FXML private AnchorPane deleteCartAnchorPane;

    private Map<String, CartItem> productCartItemMap = new HashMap<String, CartItem>();
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    //TODO Ersätt eventuellt med activateWizard.
    @FXML
    private void clickOnToCheckoutButton(){
        parentController.wizardToFront();
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
               if(shoppingCart.getItems().isEmpty()){
                   toCheckoutButton.setDisable(true);
               } else{
                   toCheckoutButton.setDisable(false);
               }
               // System.out.println("Update Amount!");
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

        //Todo ta bort raden under om vi behåller shoppingcart mellan omstart av program.
        toCheckoutButton.setDisable(true);
        totalLabel.setText("Totalt " + decimalFormat.format(shoppingCart.getTotal()));

    }

    protected Button getToCheckoutButton(){
        return toCheckoutButton;
    }


    //TODO överväg att flytta ut till initialize
    //Skapar alla våra productCartItems och lägger dem i en Map(productCartItemMap)
    protected void createProductCartItems(){
        for(Product product: parentController.iMatDataHandler.getProducts()){
            CartItem cartItem = new CartItem(product,parentController);
            productCartItemMap.put(product.getName(),cartItem);
            //parentController.getShoppingItemOfProductInCart();
        }
    }

    protected Map<String,CartItem> getProductCartItemMap(){
        return productCartItemMap;
    }

    protected void updateCart(){
        cartFlowPane.getChildren().clear();
        for(ShoppingItem shoppingItem : shoppingCart.getItems()){
                cartFlowPane.getChildren().add(0,productCartItemMap.get(shoppingItem.getProduct().getName()));
        }
        totalLabel.setText("Totalt " + decimalFormat.format(shoppingCart.getTotal()) + " kr");
    }

    @FXML
    private void clickOnEmptyCartButton(){
        if(!productCartItemMap.isEmpty()) {
            deleteCartAnchorPane.setDisable(false);
            deleteCartAnchorPane.setVisible(true);
        }
    }

    @FXML
    private void pressedYesDeleteCart(){
        deleteCartItems();
        deleteCartAnchorPane.setDisable(true);
        deleteCartAnchorPane.setVisible(false);
    }

    @FXML
    private void pressedNoKeepCart(){
        deleteCartAnchorPane.setVisible(false);
        deleteCartAnchorPane.setDisable(true);
    }

    private void deleteCartItems(){
        while(!shoppingCart.getItems().isEmpty()){
            ShoppingItem removeItem = shoppingCart.getItems().get(0);
            removeItem.setAmount(0);
            shoppingCart.removeItem(removeItem);
            parentController.updateProductAmountInAllItems(removeItem);
        }
    }


}
;
