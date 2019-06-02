package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import se.chalmers.cse.dat216.project.CreditCard;
import se.chalmers.cse.dat216.project.Customer;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class MyPage extends StackPane {

    @FXML VBox settingsPane;
    @FXML AnchorPane historyPage;
    @FXML AnchorPane favoritePage;
    @FXML AnchorPane personalDataPage;
    @FXML VBox historyOrderVbox;
    @FXML TilePane historyTilePane;
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
    @FXML TextField cardHolderField;
    @FXML TextField cardMonthField;
    @FXML TextField cardYearField;
    @FXML ToggleButton visaButton;
    @FXML ToggleButton mastercardButton;
    @FXML private TextField cardnumberTextField;
    @FXML private TextField cardnumberTextField2;
    @FXML private TextField cardnumberTextField3;
    @FXML private TextField cardnumberTextField4;
    @FXML private Label errorLabel;


    Customer customer;
    CreditCard creditCard;

    Timeline transitionRemoveSuccessfulChange;
    SequentialTransition transition;


    SearchController parentController;
    private List<ShoppingList> shoppingLists = new ArrayList<>();
    ToggleGroup cardtoggleGroup = new ToggleGroup();

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
        creditCard = parentController.iMatDataHandler.getCreditCard();

        try{
            fxmlLoader.load();
        } catch(IOException exception) {
            throw new RuntimeException(exception);
        }

        visaButton.setToggleGroup(cardtoggleGroup);
        mastercardButton.setToggleGroup(cardtoggleGroup);

        transitionRemoveSuccessfulChange = new Timeline(
                new KeyFrame(Duration.seconds(3), new KeyValue(successfulChange.opacityProperty(), 0))
        );

        transition = new SequentialTransition(transitionRemoveSuccessfulChange);


        implementCardnumberFormat(cardnumberTextField, cardnumberTextField2);
        implementCardnumberFormat(cardnumberTextField2, cardnumberTextField3);
        implementCardnumberFormat(cardnumberTextField3, cardnumberTextField4);

        implementCardnumberFormatGoBack(cardnumberTextField4, cardnumberTextField3);
        implementCardnumberFormatGoBack(cardnumberTextField3, cardnumberTextField2);
        implementCardnumberFormatGoBack(cardnumberTextField2, cardnumberTextField);

        registerCaretListener(cardnumberTextField);
        registerCaretListener(cardnumberTextField2);
        registerCaretListener(cardnumberTextField3);
        registerCaretListener(cardnumberTextField4);

        implementOnlyDigitsAllowed(cardnumberTextField);
        implementOnlyDigitsAllowed(cardnumberTextField2);
        implementOnlyDigitsAllowed(cardnumberTextField3);
        implementOnlyDigitsAllowed(cardnumberTextField4);

        implementOnlyDigitsAllowed(cardYearField);
        implementOnlyDigitsAllowed(cardMonthField);

        cardnumberTextField4.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length() > 4){
                    cardnumberTextField4.setText(oldValue);
                }
            }
        });

        cardnumberTextField3.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length() > 4){
                    cardnumberTextField3.setText(oldValue);
                }
            }
        });

        cardnumberTextField2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length() > 4){
                    cardnumberTextField2.setText(oldValue);
                }
            }
        });

        cardnumberTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length() > 4){
                    cardnumberTextField.setText(oldValue);
                }
            }
        });

        cardMonthField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length() > 2){
                    cardMonthField.setText(oldValue);
                }
            }
        });

        cardYearField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length() > 2){
                    cardYearField.setText(oldValue);
                }
            }
        });

        zipCodeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length() > 6){
                    zipCodeField.setText(oldValue);
                }
            }
        });

        emailField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                highlightEmailFormatError(emailField);
            }
        });

    }

    private void implementCardnumberFormat(TextField tf1, TextField tf2){
        tf1.textProperty().addListener((obs, oldText, newText) -> {
            if(oldText.length() < 4 && newText.length() >= 4){
                tf2.requestFocus();
            }
        });
    }

    private void implementCardnumberFormatGoBack(TextField tf1, TextField tf2){
        tf1.textProperty().addListener((obs, oldText, newText) -> {
            if((oldText.length() > 0 && newText.length() == 0)){
                tf2.requestFocus();
                tf2.positionCaret(4);
            }
        });
    }

    private void registerCaretListener(TextField textField){
        textField.caretPositionProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if((int)newValue != textField.getText().length()){
                    textField.positionCaret(textField.getText().length());
                }
            }
        });

        textField.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                textField.positionCaret(textField.getText().length());
            }
        });
    }

    private void implementOnlyDigitsAllowed(TextField textField){
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!containsDigitsOnly(newValue)){
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

    private void saveCardNumber(){
        StringBuilder cardNumber = new StringBuilder();
        cardNumber.append(cardnumberTextField.getCharacters());
        cardNumber.append(cardnumberTextField2.getCharacters());
        cardNumber.append(cardnumberTextField3.getCharacters());
        cardNumber.append(cardnumberTextField4.getCharacters());

        creditCard.setCardNumber(cardNumber.toString());
    }

    private void fillCreditCardNumberTextField(){
        if(!creditCard.getCardNumber().isEmpty()){
            int length = creditCard.getCardNumber().length();
            if(length >= 4) {
                cardnumberTextField.setText(creditCard.getCardNumber().substring(0, 4));
            }
            if(length >= 8) {
                cardnumberTextField2.setText(creditCard.getCardNumber().substring(4, 8));
            }
            if(length >= 12) {
                cardnumberTextField3.setText(creditCard.getCardNumber().substring(8, 12));
            }
            if(length == 16) {
                cardnumberTextField4.setText(creditCard.getCardNumber().substring(12, 16));
            }
        }
    }

    private void saveSelectedCardType(){
        if(mastercardButton.isSelected()){
            creditCard.setCardType("Mastercard");
        }
        if(visaButton.isSelected()){
            creditCard.setCardType("Visa");
        }
    }

    private void selectChosenCardType(){
        if(!creditCard.getCardType().isEmpty()) {
            if (creditCard.getCardType().equals("Mastercard")) {
                mastercardButton.setSelected(true);
            }
            if (creditCard.getCardType().equals("Visa")) {
                visaButton.setSelected(true);
            }
        }
    }

    @FXML
    protected void onClickBackToHistory(){
        parentController.productScrollPane.setVvalue(parentController.actualVValue);
        //TODO fixa så att man endast kallar en activate/showHistoryPage.
        parentController.myPage.historyPage.toFront();
    }

    @FXML
    protected void onClickAddHistoryToCart(){
        parentController.addAllHistoryItemsToCart();
    }

    public void loadCustomerInfo(){
        firstnameField.setText(customer.getFirstName());
        surnameField.setText(customer.getLastName());
        phoneNumberField.setText(customer.getMobilePhoneNumber());
        emailField.setText(customer.getEmail());
        deliveryField.setText(customer.getAddress());
        zipCodeField.setText(customer.getPostCode());
        cityField.setText(customer.getPostAddress());
        cardHolderField.setText(creditCard.getHoldersName());
        cardMonthField.setText(Integer.toString(creditCard.getValidMonth()));
        cardYearField.setText(Integer.toString(creditCard.getValidYear()));
        fillCreditCardNumberTextField();
        selectChosenCardType();

        resetGreenBordersOnTextField(emailField);
        resetGreenBordersOnTextField(firstnameField);
        resetGreenBordersOnTextField(surnameField);
        resetGreenBordersOnTextField(phoneNumberField);
        resetGreenBordersOnTextField(deliveryField);
        resetGreenBordersOnTextField(zipCodeField);
        resetGreenBordersOnTextField(cityField);
        resetGreenBordersOnTextField(cardHolderField);
        resetGreenBordersOnTextField(cardYearField);
        resetGreenBordersOnTextField(cardMonthField);
        resetGreenBordersOnTextField(cardnumberTextField);
        resetGreenBordersOnTextField(cardnumberTextField2);
        resetGreenBordersOnTextField(cardnumberTextField3);
        resetGreenBordersOnTextField(cardnumberTextField4);
    }

    private void saveEmailIfItsPossible(){
        if(isEmailFormat(emailField)){
            customer.setAddress(emailField.getText());
            setGreenBordersOnTextField(emailField);
        }
    }

    @FXML
    protected void onClickSaveChanges(){
        setGreenBordersOnTextField(firstnameField);
        customer.setFirstName(firstnameField.getText());
        setGreenBordersOnTextField(surnameField);
        customer.setLastName(surnameField.getText());

        setGreenBordersOnTextField(phoneNumberField);
        customer.setMobilePhoneNumber(phoneNumberField.getText());

        saveEmailIfItsPossible();

        setGreenBordersOnTextField(deliveryField);
        customer.setAddress(deliveryField.getText());
        setGreenBordersOnTextField(zipCodeField);
        customer.setPostCode(zipCodeField.getText());
        setGreenBordersOnTextField(cityField);
        customer.setPostAddress(cityField.getText());

        saveCardNumber();
        saveSelectedCardType();

        setGreenBordersOnTextField(cardHolderField);
        creditCard.setHoldersName(cardHolderField.getText());
        setGreenBordersOnTextField(cardMonthField);
        creditCard.setValidMonth(Integer.parseInt(cardMonthField.getText()));
        setGreenBordersOnTextField(cardYearField);
        creditCard.setValidYear(Integer.parseInt(cardYearField.getText()));

        successfulChange.setVisible(true);
        transition.play();

        setGreenBordersOnTextField(cardnumberTextField);
        setGreenBordersOnTextField(cardnumberTextField2);
        setGreenBordersOnTextField(cardnumberTextField3);
        setGreenBordersOnTextField(cardnumberTextField4);

        errorLabel.setVisible(false);
    }

    //------------------------------------------------------------------------------------------------------------------------Felhantering

    private boolean isEmailFormat(TextField textField){       //returnar true om textfieldens text innehåller @ och punkt
        if(textField.getText().contains("@") && textField.getText().contains(".")){
            return true;
        }
        return false;
    }

    private void highlightEmailFormatError(TextField textField){     //visar vilken textfield som är fel om den är empty eller ej i email-format
        if(!isEmailFormat(textField)){
            setRedBordersOnTextField(textField);
            showErrorMessage(textField);
            errorLabel.setVisible(true);
        } else {
            resetBordersOnTextField(textField);
            errorLabel.setVisible(false);
        }
    }

    private void setRedBordersOnTextField(TextField textField){
        textField.setStyle("-fx-border-width: 3px; -fx-border-color: #FF0000;");
    }

    private void resetBordersOnTextField(TextField textField){
        textField.setStyle("");
    }

    private void setGreenBordersOnTextField(TextField textField){
        textField.setStyle("-fx-border-width: 3px; -fx-border-color: #66bb6a;");
    }

    private void resetGreenBordersOnTextField(TextField textField){
        textField.setStyle("");
    }

    private void showErrorMessage(TextField textField){
        StringBuilder errorMessage = new StringBuilder();
        Tooltip tooltip = new Tooltip();
        /*
        if(!containsDigitsOnly(textField) && (textField.equals(postCode) || textField.equals(telNumber) || textField.equals(validMonthTextField)
                || textField.equals(validYearTextField) || textField.equals(cvcTextField))){
            errorMessage.append("Textfältet får endast innehålla siffror.\n");
        }

         */
        if(!isEmailFormat(textField) && textField.equals(emailField)){
            errorMessage.append("Textfältet måste innehålla en giltig email-adress för att kunna sparas.\n");
        }
        tooltip.setText(errorMessage.toString());
        tooltip.setFont(new Font(18));
        Tooltip.install(textField, tooltip);
    }

}
