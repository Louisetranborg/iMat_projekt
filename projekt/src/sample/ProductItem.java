package sample;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class ProductItem extends AnchorPane {

    private SearchController parentController;
    private ShoppingItem shoppingItem;
    @FXML private Label name;
    @FXML private ImageView image;
    @FXML private Label price;
    @FXML private ImageView addButton;
    @FXML private ImageView removeButton;
    @FXML private TextField amountBox;

    @FXML
    protected void onClick(){ //När man klickar på ett productItem skall info om produkten komma upp
        parentController.openProductDetailView(shoppingItem);
    }

    public ProductItem(ShoppingItem shoppingItem, SearchController parentController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/productItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        } catch (IOException exception){
            throw new RuntimeException(exception);
        }

        this.parentController = parentController;
        this.shoppingItem = shoppingItem;
        name.setText(shoppingItem.getProduct().getName());
        image.setImage(parentController.iMatDataHandler.getFXImage(shoppingItem.getProduct()));
        price.setText(shoppingItem.getProduct().getPrice() + " " + shoppingItem.getProduct().getUnit());
        updateAmountInProductItem(); //Skriver in default-amount (0) i textField

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
    }

    protected void updateAmountInProductItem(){ //Visa värdet på amount i amountBox
        amountBox.textProperty().setValue(String.valueOf(shoppingItem.getAmount()));
    }

    @FXML
    protected void clickedOnAddButton(Event event){ //När man klickar på plusset
        parentController.mouseTrap(event); //Infoboxen skall ej komma upp
        parentController.addItemToCart(shoppingItem);
    }

    @FXML
    protected void clickedOnRemoveButton(Event event) {
        parentController.mouseTrap(event);
        parentController.removeItemFromCart(shoppingItem);
    }

    @FXML
    private void glow(){
        addButton.setEffect(new Glow(0.5));
    }

    @FXML
    private void removeGlow(){
        addButton.setEffect(new Glow(0));
    }

}
