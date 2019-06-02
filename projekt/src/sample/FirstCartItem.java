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
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.text.DecimalFormat;

public class FirstCartItem extends AnchorPane implements FavoriteObserver {

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
    @FXML private ImageView whiteHeart;
    @FXML private ImageView greenHeart;

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

        //Lägger till som en FavoriteObserver
        parentController.addObservers(this);

        //Grönmarkrar de produkter som är i favoriter när de skapas.
        parentController.updateFavoriteItems(getProduct(), parentController.iMatDataHandler.isFavorite(getProduct()));



        /*
        amountBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!amountBox.getText().isEmpty()) {
                    //TODO lägg in handle input. HELST GÖRA DET CENTRALT OCH PUBLIKT.
                    shoppingItem.setAmount(Double.valueOf(amountBox.getText()));
                    parentController.shoppingCartPane.addProductToCart(shoppingItem);
                }
                if(shoppingItem.getAmount() <= 0 || amountBox.getText().isEmpty()){       //Ändra om vi vill ha double-system
                    shoppingItem.setAmount(0);
                    parentController.shoppingCartPane.removeProductFromCart(shoppingItem);
                }
                parentController.updateProductAmountInAllItems(shoppingItem);
                parentController.updateFirstCartInWizard();
            }
        });

         */

        price.setText(decimalFormat.format(shoppingItem.getTotal()) + " kr");

        amountBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    amountBox.clear();
                }
                else{
                    parentController.updateProductAmountInAllItems(shoppingItem);
                    parentController.updateFirstCartInWizard();
                }
            }
        });

        amountBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String input = amountBox.getText();
                double amount = handleInput(input);

                parentController.setAmountInCart(shoppingItem.getProduct(), amount);
                parentController.updateProductAmountInAllItems(shoppingItem);
                parentController.updateFirstCartInWizard();

            }
        });



    }

    private double extractDigits(String value){
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {
            if (Character.isDigit(value.charAt(i)) || (String.valueOf(value.charAt(i)).equals(".") && !stringBuilder.toString().contains("."))) {
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
        String unitSuffix = shoppingItem.getProduct().getUnitSuffix();

        if(unitSuffix.equals("st") || unitSuffix.equals("förp") || unitSuffix.equals("burk")){
            output = Math.round(output);
        }

        if(output < 0.1){
            return 0;
        } else {
            return output;
        }
    }

    @FXML
    private void clickedOnAddButton(){
        parentController.modifyAmountInCart(shoppingItem.getProduct(),1);
        //parentController.updateBiggerCartInWizard();
        parentController.updateFirstCartInWizard();
    }

    @FXML
    private void clickedOnRemoveButton(){
        parentController.modifyAmountInCart(shoppingItem.getProduct(), -1);
        //parentController.updateBiggerCartInWizard();
        parentController.updateFirstCartInWizard();
    }

    protected void updateAmountBoxInFirstCartItem(){
        amountBox.textProperty().setValue(String.valueOf(shoppingItem.getAmount()));
        price.setText(String.valueOf(decimalFormat.format(shoppingItem.getTotal())));
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

    //
    //
    //-----------------------Metoder som har med Favoriter att göra ----------------------------------------------------------------
    //

    @FXML
    protected void setFavorite(){
        parentController.addFavorite(shoppingItem.getProduct());
    }

    @FXML
    protected void removeFavorite(){
        parentController.removeFavorite(shoppingItem.getProduct());
    }

    public void update(Product p, Boolean isFavorite){
        if (isFavorite)
            greenHeart.toFront();
        else
            whiteHeart.toFront();
    }

    public Product getProduct(){
        return shoppingItem.getProduct();
    }

    private void implementOnlyDigitsAllowed(TextField textField){
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!containsDigitsOnly(newValue) || !textField.getText().contains(".")){
                    textField.setText(oldValue);
                }
            }
        });
    }

    private boolean containsDigitsOnly(String string){
        for(Character c : string.toCharArray()){
            if(!Character.isDigit(c)){
                return false;
            }
        } return true;
    }


}
