package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.text.DecimalFormat;

public class CartItem extends AnchorPane {

    private SearchController parentController;
    private Product product;
    @FXML private ImageView cartImage;
    @FXML private Label cartName;
    @FXML private TextField amountBox;
    @FXML private ImageView removeButton;
    @FXML private ImageView removeButtonHover;
    @FXML private ImageView addButtonGreen;
    @FXML private ImageView addButtonGreenHover;
    @FXML private Label price;

    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    protected Label getPrice(){
        return price;
    }

    @FXML
    private void clickedOnAddButton(){
        parentController.modifyAmountInCart(product, 1);
    }

    @FXML
    private void clickedOnRemoveButton(){
        parentController.modifyAmountInCart(product, -1);
    }

    @FXML
    protected void hoverOnRemoveButton(Event event){
        removeButtonHover.toFront();
    }

    @FXML
    protected void hoverOnAddButton(Event event){
        addButtonGreenHover.toFront();
    }

    @FXML
    protected void hoverOffAddButton(Event event){
        addButtonGreenHover.toBack();
    }

    @FXML
    protected void hoverOffRemoveButton(Event event){
        removeButtonHover.toBack();
    }

    protected void updateAmountInCartItem(ShoppingItem shoppingItem){
        amountBox.textProperty().setValue(String.valueOf(shoppingItem.getAmount()));
    }

    private double extractDigits(String value){
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {
            if (Character.isDigit(value.charAt(i)) || String.valueOf(value.charAt(i)).equals(".")) {
                stringBuilder.append(value.charAt(i));
            }
        }

        if(!stringBuilder.toString().isEmpty()) {
            return Double.valueOf(stringBuilder.toString());
        } else {
            return 0;
        }

    }

    private double handleInput(String value){
        double output = extractDigits(value);
        String unitSuffix = product.getUnitSuffix();

        if(unitSuffix.equals("st") || unitSuffix.equals("förp") || unitSuffix.equals("burk")){
            output = Math.round(output);
        }

        if(output < 0.1){
            return 0;
        } else {
            return output;
        }
    }

    public CartItem(Product product, SearchController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/productCartItem.fxml")); //Laddar in rätt fxml-fil
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.product = product;
        this.parentController = parentController;
        cartImage.setImage(parentController.iMatDataHandler.getFXImage(product));
        cartName.setText(product.getName());

        amountBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String input = amountBox.getText();
                double amount = handleInput(input);

               parentController.setAmountInCart(product, amount);

            }
        });

       // price.setText(shoppingItem.getTotal() + " kr");


        amountBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    amountBox.clear();
                } else {
                    //TODO OM PANIK KOLLA HIT
                    updateAmountInCartItem(parentController.getShoppingItemOfProductInCart(product));
                }
            }
        });
    }



}
