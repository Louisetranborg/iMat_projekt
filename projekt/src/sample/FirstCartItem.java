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
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.text.DecimalFormat;

public class FirstCartItem extends AnchorPane implements FavoriteObserver {

    private SearchController parentController;
    private Product product;
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

    public FirstCartItem(Product product, SearchController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/firstCartItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.product = product;
        this.parentController = parentController;
        image.setImage(IMatDataHandler.getInstance().getFXImage(product));
        title.setText(product.getName());

        //Lägger till som en FavoriteObserver
        parentController.addObservers(this);

        //Grönmarkrar de produkter som är i favoriter när de skapas.
        parentController.updateFavoriteItems(getProduct(), parentController.iMatDataHandler.isFavorite(getProduct()));

        amountBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String input = amountBox.getText();
                double amount = parentController.handleInput(input, product);

                parentController.setAmountInCart(product, amount);
            }
        });
    }

    @FXML
    private void clickedOnAddButton(){
        parentController.modifyAmountInCart(product,1);
    }

    @FXML
    private void clickedOnRemoveButton(){
        parentController.modifyAmountInCart(product, -1);
    }


    protected void updateAmountInFirstCartItem(ShoppingItem shoppingItem){
        amountBox.textProperty().setValue(String.valueOf(parentController.getAmountOfProductInCart(shoppingItem.getProduct())));
        price.setText(decimalFormat.format(shoppingItem.getTotal()) + " kr");
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
        parentController.addFavorite(product);
    }

    @FXML
    protected void removeFavorite(){
        parentController.removeFavorite(product);
    }

    public void update(Product p, Boolean isFavorite){
        if (isFavorite)
            greenHeart.toFront();
        else
            whiteHeart.toFront();
    }

    //TODO IDAG
    public Product getProduct(){
        return product;
    }


}
