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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import se.chalmers.cse.dat216.project.CreditCard;
import se.chalmers.cse.dat216.project.Customer;

import java.io.IOException;

public class PersonalDataPage extends AnchorPane {

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
    @FXML private TextField cardnumberTextField1;
    @FXML private TextField cardnumberTextField2;
    @FXML private TextField cardnumberTextField3;
    @FXML private TextField cardnumberTextField4;

    Customer customer;
    CreditCard creditCard;

    Timeline transitionRemoveSuccessfulChange;
    SequentialTransition transition;
    ToggleGroup cardtoggleGroup = new ToggleGroup();

    SearchController parentController;

    public PersonalDataPage(SearchController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/personalDataPage.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.parentController = parentController;

        customer = parentController.iMatDataHandler.getCustomer();
        creditCard = parentController.iMatDataHandler.getCreditCard();


        visaButton.setToggleGroup(cardtoggleGroup);
        mastercardButton.setToggleGroup(cardtoggleGroup);

        transitionRemoveSuccessfulChange = new Timeline(
                new KeyFrame(Duration.seconds(3), new KeyValue(successfulChange.opacityProperty(), 0))
        );

        transition = new SequentialTransition(transitionRemoveSuccessfulChange);

    }

    public void setUpPersonalDataPage(){
        implementCardnumberFormat(cardnumberTextField1, cardnumberTextField2);
        implementCardnumberFormat(cardnumberTextField2, cardnumberTextField3);
        implementCardnumberFormat(cardnumberTextField3, cardnumberTextField4);

        implementCardnumberFormatGoBack(cardnumberTextField4, cardnumberTextField3);
        implementCardnumberFormatGoBack(cardnumberTextField3, cardnumberTextField2);
        implementCardnumberFormatGoBack(cardnumberTextField2, cardnumberTextField1);

        registerCaretListener(cardnumberTextField1);
        registerCaretListener(cardnumberTextField2);
        registerCaretListener(cardnumberTextField3);
        registerCaretListener(cardnumberTextField4);

        parentController.implementOnlyDigitsAllowed(cardnumberTextField1);
        parentController.implementOnlyDigitsAllowed(cardnumberTextField2);
        parentController.implementOnlyDigitsAllowed(cardnumberTextField3);
        parentController.implementOnlyDigitsAllowed(cardnumberTextField4);

        parentController.implementOnlyDigitsAllowed(cardYearField);
        parentController.implementOnlyDigitsAllowed(cardMonthField);

        parentController.implementMaxLimitInTextfield(cardnumberTextField4, 4);
        parentController.implementMaxLimitInTextfield(cardnumberTextField3, 4);
        parentController.implementMaxLimitInTextfield(cardnumberTextField2, 4);
        parentController.implementMaxLimitInTextfield(cardnumberTextField1, 4);
        parentController.implementMaxLimitInTextfield(cardMonthField, 2);
        parentController.implementMaxLimitInTextfield(cardYearField, 2);
        parentController.implementMaxLimitInTextfield(zipCodeField, 2);
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

        saveCardNumber();
        saveSelectedCardType();

        creditCard.setHoldersName(cardHolderField.getText());


        creditCard.setValidMonth(Integer.parseInt(cardMonthField.getText()));
        creditCard.setValidYear(Integer.parseInt(cardYearField.getText()));

        successfulChange.setVisible(true);
        transition.play();
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

    private void saveCardNumber(){
        StringBuilder cardNumber = new StringBuilder();
        cardNumber.append(cardnumberTextField1.getCharacters());
        cardNumber.append(cardnumberTextField2.getCharacters());
        cardNumber.append(cardnumberTextField3.getCharacters());
        cardNumber.append(cardnumberTextField4.getCharacters());

        creditCard.setCardNumber(cardNumber.toString());
    }

    private void fillCreditCardNumberTextField(){
        if(!creditCard.getCardNumber().isEmpty()){
            int length = creditCard.getCardNumber().length();
            if(length >= 4) {
                cardnumberTextField1.setText(creditCard.getCardNumber().substring(0, 4));
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
}
