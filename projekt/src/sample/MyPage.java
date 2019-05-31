package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.CreditCard;
import se.chalmers.cse.dat216.project.Customer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyPage extends StackPane {

    @FXML VBox settingsPane;
    @FXML
    AnchorPane historyPage;
    @FXML AnchorPane favoritePage;
    @FXML AnchorPane personalDataPage;
    @FXML VBox historyOrderVbox;
    @FXML
    TilePane historyTilePane;
    @FXML TilePane historyProductTilePane;
    @FXML AnchorPane historyItems;
    @FXML Label successfulChange;
    @FXML TextField firstnameField;
    @FXML TextField surnameField;
    @FXML TextField phoneNumberField;
    @FXML TextField emailField;
    @FXML TextField deliveryField;
    @FXML TextField zipCodeField;
    @FXML TextField cityField;
    @FXML TextField cardNumberField;
    @FXML TextField cardHolderField;
    @FXML TextField cardMonthField;
    @FXML TextField cardYearField;
    @FXML ImageView visaImage;
    @FXML ImageView mastercardImage;

    Customer customer;
    CreditCard card;


    SearchController parentController;
    private List<ShoppingList> shoppingLists = new ArrayList<>();

    protected List<ShoppingList> getShoppingLists(){
        return shoppingLists;
    }


    protected void addNewShoppingList(ShoppingList shoppingList){       //lägger till en ny inköpslista och lägger den i flowpane
        shoppingLists.add(shoppingList);
    }

    public MyPage(SearchController parentController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/myPage.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        this.parentController = parentController;
        customer = parentController.iMatDataHandler.getCustomer();
        card = parentController.iMatDataHandler.getCreditCard();

        try{
            fxmlLoader.load();
        } catch(IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    @FXML
    protected void onClick(){

    }

    @FXML
    protected void onClickBackToHistory(){
        parentController.updateHistoryPage();

        //TODO fixa så att man endast kallar en activate/showHistoryPage.
        parentController.myPage.historyPage.toFront();
    }

    @FXML
    protected void onClickAddHistoryToCart(){
        parentController.addAllHistoryItemsToCart();
        //parentController.addItemsToCart();
    }

    public void loadCustomerInfo(){
        firstnameField.setText(customer.getFirstName());
        surnameField.setText(customer.getLastName());
        phoneNumberField.setText(customer.getMobilePhoneNumber());
        emailField.setText(customer.getEmail());
        deliveryField.setText(customer.getAddress());
        zipCodeField.setText(customer.getPostCode());
        cityField.setText(customer.getPostAddress());
        cardNumberField.setText(card.getCardNumber());
        cardHolderField.setText(card.getHoldersName());
        cardMonthField.setText(Integer.toString(card.getValidMonth()));
        cardYearField.setText(Integer.toString(card.getValidYear()));

    }

    @FXML
    protected void onClickSaveChanges(){

        customer.setFirstName(firstnameField.getText());
        customer.setLastName(surnameField.getText());
        customer.setMobilePhoneNumber(phoneNumberField.getText());
        customer.setEmail(emailField.getText());
        customer.setAddress(deliveryField.getText());
        customer.setPostCode(zipCodeField.getText());
        customer.setPostAddress(cityField.getText());

        //card.setCardType();
        card.setCardNumber(cardNumberField.getText());
        card.setHoldersName(cardHolderField.getText());


        card.setValidMonth(Integer.parseInt(cardMonthField.getText()));
        card.setValidYear(Integer.parseInt(cardYearField.getText()));

        successfulChange.setVisible(true);

    }
}
