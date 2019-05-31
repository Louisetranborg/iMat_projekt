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
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class ProductItem extends AnchorPane implements FavoriteObserver{

    private SearchController parentController;
    //private ShoppingItem shoppingItem;
    Product product;
    @FXML private Label name;
    @FXML private ImageView image;
    @FXML private Label price;
    @FXML private ImageView addButton;
    @FXML private ImageView removeButton;
    @FXML private ImageView addButton2;
    @FXML private ImageView addButtonHover;
    @FXML private ImageView removeButtonBrown;
    @FXML private ImageView removeButtonHover;
    @FXML private ImageView whiteHeart;
    @FXML private ImageView greenHeart;
    @FXML private TextField amountBox;
    @FXML private AnchorPane productPane;


    @FXML
    protected void onClick(){ //När man klickar på ett productItem skall info om produkten komma upp
        parentController.openProductDetailView(product);
    }

    public ProductItem(Product product, SearchController parentController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/productItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        } catch (IOException exception){
            throw new RuntimeException(exception);
        }

        this.parentController = parentController;
        this.product = product;
        name.setText(product.getName());
        image.setImage(parentController.iMatDataHandler.getFXImage(product));
        price.setText(product.getPrice() + " " + product.getUnit());
        updateProductItem(); //Skriver in default-amount (0) i textField

        //Lägger till som en FavoriteObserver
        parentController.addObservers(this);
        //Grönmarkrar de produkter som är i favoriter när de skapas.
        parentController.updateFavoriteItems(getProduct(), parentController.iMatDataHandler.isFavorite(getProduct()));

        amountBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String input = amountBox.getText();
                double amount = handleInput(input);

                parentController.setAmountInCart(product, amount);
            }
        });


        amountBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    amountBox.clear();
                } else {
                    updateProductItem();
                }
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


    protected void updateProductItem(){ //Visa värdet på amount i amountBox
        updateButtons();
        amountBox.textProperty().setValue(String.valueOf(parentController.getAmountOfProductInCart(product)));
        System.out.println("Updated ProductItem! - " + product.getName());
    }

    @FXML
    protected void clickedOnAddButton(Event event){ //När man klickar på plusset
        parentController.mouseTrap(event); //Infoboxen skall ej komma upp
        parentController.modifyAmountInCart(product,1);

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



    @FXML
    protected void clickedOnRemoveButton(Event event) {
        parentController.mouseTrap(event);
        parentController.modifyAmountInCart(product, -1);
    }


    private void updateButtons(){
        if (parentController.getAmountOfProductInCart(product) <= 0){
            addButton.toFront();
            removeButton.toFront();
            removeButtonBrown.setVisible(false);
            removeButtonHover.setVisible(false);
        } else {
            addButton2.toFront();
            removeButtonBrown.setVisible(true);
            removeButtonHover.setVisible(true);
            removeButtonBrown.toFront();
        }
    }

    protected void changeToHistoryLayout(ShoppingItem shoppingItem){
        addButton.setDisable(true);
        addButton.setVisible(false);
        addButton2.setDisable(true);
        addButton2.setVisible(false);
        addButtonHover.setDisable(true);
        addButtonHover.setVisible(false);
        removeButton.setDisable(true);
        removeButton.setVisible(false);
        removeButtonBrown.setDisable(true);
        removeButtonBrown.setVisible(false);
        removeButtonHover.setDisable(true);
        removeButtonHover.setVisible(false);
        amountBox.setDisable(true);
        amountBox.textProperty().setValue(String.valueOf(shoppingItem.getAmount()));
    }

    protected void changeToNormalLayout(){
        updateProductItem();
        addButton.setDisable(false);
        addButton2.setDisable(false);
        addButtonHover.setDisable(false);
        removeButton.setDisable(false);
        removeButtonBrown.setDisable(false);
        removeButtonHover.setDisable(false);
    }

    protected void setBlackButton(){
        addButton.toFront();
    }

    protected void setGreenButton(){
        addButton2.toFront();
    }

    //Våra ShoppingItem kan tydligen bara vara på ett ställe samtidigt. Därför kollar vi vart användaren befinner sig och uppdaterar sidan efter det.

    //
    //
    //-----------------------Metoder som har med Favoriter att göra ----------------------------------------------------------------
    //

    @FXML
    protected void setFavorite(Event event){
        parentController.mouseTrap(event);
        parentController.addFavorite(product);
    }


    @FXML
    protected void removeFavorite(Event event){
        parentController.mouseTrap(event);
        parentController.removeFavorite(product);

        //Denna rad gör så att den försvinner direkt om man är på mina favoriter.
        if(parentController.isUserOnMyPage)
            parentController.updateFavoritePage();
    }

    //Metoden som kallas av FavoriteObserver
    public void update(Product p, Boolean isFavorite){
        if (isFavorite)
            greenHeart.toFront();
        else
            whiteHeart.toFront();
    }

    public Product getProduct() {
        return product;

    }
    @FXML
    private void dropShadow(){
        productPane.setEffect(new DropShadow(20.0,Color.color(00,00,00)));
    }

    @FXML
    private void removeDropShadow(){
        productPane.setEffect(new DropShadow(10,Color.color(00,00,00)));
    }

}

