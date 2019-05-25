package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.text.DecimalFormat;

public class FirstCartItem extends AnchorPane {

    private SearchController parentController;
    private ShoppingItem shoppingItem;
    @FXML private ImageView image;
    @FXML private Label title;
    @FXML private TextField amountBox;
    @FXML private ImageView addButton;
    @FXML private ImageView removeButton;
    @FXML private Label price;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public FirstCartItem(ShoppingItem shoppingItem, SearchController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/firstCartItem.fxml"));
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
        price.setText(decimalFormat.format(shoppingItem.getTotal()) + " kr");

    }

    @FXML
    private void clickedOnAddButton(){
        parentController.addItemToCart(shoppingItem);
        //parentController.updateBiggerCartInWizard();
        parentController.putFirstCartInWizard();
    }

    @FXML
    private void clickedOnRemoveButton(){
        parentController.removeItemFromCart(shoppingItem);
        //parentController.updateBiggerCartInWizard();
        parentController.putFirstCartInWizard();
    }

    protected void updateAmountBoxInFirstCartItem(){
        amountBox.textProperty().setValue(String.valueOf(shoppingItem.getAmount()));
    }

}
