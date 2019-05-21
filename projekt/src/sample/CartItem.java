package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class CartItem extends AnchorPane {

    private SearchController parentController;
    private ShoppingItem shoppingItem;
    @FXML private ImageView cartImage;
    @FXML private Label cartName;
    @FXML private TextField amountBox;
    @FXML private ImageView removeButton;
    @FXML private ImageView addButton;
    @FXML private Label price;

    protected ShoppingItem getShoppingItem(){
        return shoppingItem;
    }

    protected Label getPrice(){
        return price;
    }

    @FXML
    private void clickedOnAddButton(){
        parentController.addItemToCart(shoppingItem);
    }

    @FXML
    private void clickedOnRemoveButton(){
        parentController.removeItemFromCart(shoppingItem);
    }

    protected void updateAmountInCartItem(){
        amountBox.textProperty().setValue(String.valueOf(shoppingItem.getAmount()));
    }

    public CartItem(ShoppingItem shoppingItem, SearchController parentController) {
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

        amountBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                shoppingItem.setAmount(Double.valueOf(amountBox.getText()));
                parentController.shoppingCartPane.addProductToCart(shoppingItem);
                if(shoppingItem.getAmount() < 1){       //Ändra om vi vill ha double-system
                    parentController.shoppingCartPane.removeProductFromCart(shoppingItem);
                }
                parentController.updateAmount(shoppingItem);
            }
        });

        price.setText(shoppingItem.getTotal() + " kr");

    }


}
