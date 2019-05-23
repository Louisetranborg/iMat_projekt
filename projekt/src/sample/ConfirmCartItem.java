package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class ConfirmCartItem extends AnchorPane {

    private ShoppingItem shoppingItem;
    private SearchController parentController;
    @FXML private ImageView image;
    @FXML private ImageView removeButton;
    @FXML private ImageView addButton;
    @FXML private TextField amountBox;
    @FXML private Label title;
    @FXML private Label price;


    public ConfirmCartItem(ShoppingItem shoppingItem, SearchController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/confirmCartItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.shoppingItem = shoppingItem;
        this.parentController = parentController;

        image.setImage(IMatDataHandler.getInstance().getFXImage(shoppingItem.getProduct()));
        title.setText(shoppingItem.getProduct().getName());
        price.setText(shoppingItem.getTotal() + " kr");
    }

    @FXML
    private void clickedOnAddButton(){
        parentController.addItemToCart(shoppingItem);
        parentController.updateBiggerCartInWizard();
    }

    @FXML
    private void clickedOnRemoveButton(){
        parentController.removeItemFromCart(shoppingItem);
        parentController.updateBiggerCartInWizard();
    }

    protected void updateAmountBoxInConfirmCartItem(){
        amountBox.textProperty().setValue(String.valueOf(shoppingItem.getAmount()));
    }

}
