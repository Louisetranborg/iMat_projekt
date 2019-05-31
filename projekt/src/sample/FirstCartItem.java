package sample;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
    @FXML private ImageView addButtonHover;
    @FXML private ImageView removeButtonHover;

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

        amountBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!amountBox.getText().isEmpty()) {
                    shoppingItem.setAmount(Double.valueOf(amountBox.getText()));
                    parentController.shoppingCartPane.addProductToCart(shoppingItem);
                }
                if(shoppingItem.getAmount() <= 0 || amountBox.getText().isEmpty()){       //Ändra om vi vill ha double-system
                    shoppingItem.setAmount(0);
                    parentController.shoppingCartPane.removeProductFromCart(shoppingItem);
                }
                parentController.updateAllItems(shoppingItem);
                parentController.updateFirstCartInWizard();
            }
        });

        price.setText(decimalFormat.format(shoppingItem.getTotal()) + " kr");

    }

    @FXML
    private void clickedOnAddButton(){
        parentController.addItemToCart(shoppingItem);
        //parentController.updateBiggerCartInWizard();
        parentController.updateFirstCartInWizard();
    }

    @FXML
    private void clickedOnRemoveButton(){
        parentController.subtractItemFromCart(shoppingItem);
        //parentController.updateBiggerCartInWizard();
        parentController.updateFirstCartInWizard();
    }

    protected void updateAmountBoxInFirstCartItem(){
        amountBox.textProperty().setValue(String.valueOf(shoppingItem.getAmount()));
    }

    @FXML
    protected void hoverOnAddButton(Event event){
        addButtonHover.toFront();
    }

    @FXML
    protected void hoverOffAddButton(Event event){
        addButtonHover.toBack();
    }

    @FXML
    protected void hoverOnRemoveButton(Event event){
        removeButtonHover.toFront();
    }

    @FXML
    protected void hoverOffRemoveButton(Event event){
        removeButtonHover.toBack();
    }

}
