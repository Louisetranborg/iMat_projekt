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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
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
    @FXML private Label errorLabel;

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

        emailField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                highlightEmailFormatError(emailField);
            }
        });

        zipCodeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                highlightDigitsOnlyError(zipCodeField);
            }
        });

        phoneNumberField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                highlightDigitsOnlyError(phoneNumberField);
            }
        });

        cardMonthField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(cardMonthField.getText().length() > 1) {
                    if (extractDigits(cardMonthField.getText()) > 12) {
                        cardMonthField.setText("12");
                    }
                    if (extractDigits(cardMonthField.getText()) == 0) {
                        cardMonthField.setText("01");
                    }
                }
            }
        });


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
        parentController.implementMaxLimitInTextfield(zipCodeField, 5);

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

        if(creditCard.getValidMonth() == 0){
            cardMonthField.clear();
        } else {
            cardMonthField.setText(Integer.toString(creditCard.getValidMonth()));
        }
        if(creditCard.getValidYear() == 0){
            cardYearField.clear();
        } else {
            cardYearField.setText(Integer.toString(creditCard.getValidYear()));
        }
        fillCreditCardNumberTextField();
        selectChosenCardType();
        clearGreenBorderOnAllFields();
    }

    private void clearGreenBorderOnAllFields(){
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
        resetGreenBordersOnTextField(cardnumberTextField1);
        resetGreenBordersOnTextField(cardnumberTextField2);
        resetGreenBordersOnTextField(cardnumberTextField3);
        resetGreenBordersOnTextField(cardnumberTextField4);
    }

    @FXML
    protected void onClickSaveChanges(){
        setGreenBordersOnTextField(firstnameField);
        customer.setFirstName(firstnameField.getText());
        setGreenBordersOnTextField(surnameField);
        customer.setLastName(surnameField.getText());

        saveEmailIfItsPossible();
        savePhoneIfItsPossible();
        saveZipIfItsPossible();

        setGreenBordersOnTextField(deliveryField);
        customer.setAddress(deliveryField.getText());
        setGreenBordersOnTextField(cityField);
        customer.setPostAddress(cityField.getText());

        saveCardNumber();
        saveSelectedCardType();

        setGreenBordersOnTextField(cardHolderField);
        creditCard.setHoldersName(cardHolderField.getText());

        setGreenBordersOnTextField(cardnumberTextField1);
        setGreenBordersOnTextField(cardnumberTextField2);
        setGreenBordersOnTextField(cardnumberTextField3);
        setGreenBordersOnTextField(cardnumberTextField4);

        if(cardMonthField.getText().equals("")){
            creditCard.setValidMonth(0);
        }
        else{
            creditCard.setValidMonth(Integer.parseInt(cardMonthField.getText()));
        }


        setGreenBordersOnTextField(cardYearField);

        if (cardYearField.getText().equals("")){
            creditCard.setValidYear(0);
        }
        else{
            creditCard.setValidYear(Integer.parseInt(cardYearField.getText()));
        }


        setGreenBordersOnTextField(cardMonthField);

        successfulChange.setVisible(true);
        transition.play();


        errorLabel.setVisible(false);
    }

    private void saveEmailIfItsPossible(){
        if(isEmailFormat(emailField)){
            customer.setAddress(emailField.getText());
            setGreenBordersOnTextField(emailField);
        }
    }

    private void savePhoneIfItsPossible(){
        if(containsDigitsOnly(phoneNumberField)){
            customer.setPhoneNumber(phoneNumberField.getText());
            setGreenBordersOnTextField(phoneNumberField);
        }
    }

    private void saveZipIfItsPossible(){
        if(containsDigitsOnly(zipCodeField)){
            customer.setPostCode(zipCodeField.getText());
            setGreenBordersOnTextField(zipCodeField);
        }
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

    private void implementCardnumberFormat(TextField tf1, TextField tf2){
        tf1.textProperty().addListener((obs, oldText, newText) -> {
            if(oldText.length() < 4 && newText.length() >= 4){
                tf2.requestFocus();
            }
        });
    }

    //------------------------------------------------------------------------------------------------------------------------Felhantering

    private boolean isEmailFormat(TextField textField){       //returnar true om textfieldens text innehåller @ och punkt
        if(textField.getText().contains("@") && textField.getText().contains(".")){
            return true;
        }
        return false;
    }

    private boolean containsDigitsOnly(TextField textField){        //returnar true om textfieldens text endast består av siffror, spacebars och bindestreck
        System.out.println(textField.getText());
        char[] chars = textField.getText().toCharArray();
        for(char c : chars){
            if(!String.valueOf(c).equals(" ") && !Character.isDigit(c) && !String.valueOf(c).equals("-")){
                System.out.print(textField.getText() + "doesnt only contain digits");
                return false;
            }
        }
        return true;
    }

    private void highlightDigitsOnlyError(TextField textField){      //visar vilken textfield som är fel om den är empty eller om den inte bara inntehåller siffror
        if(!containsDigitsOnly(textField)){
            setRedBordersOnTextField(textField);
            showErrorMessage(textField);
            errorLabel.setVisible(true);
        } else {
            resetBordersOnTextField(textField);
            errorLabel.setVisible(false);
        }
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

    private void showErrorMessage(TextField textField) {
        StringBuilder errorMessage = new StringBuilder();
        Tooltip tooltip = new Tooltip();
        if(!containsDigitsOnly(textField) && (textField.equals(cardMonthField) || textField.equals(cardYearField) || textField.equals(zipCodeField)
                || textField.equals(phoneNumberField))){
            errorMessage.append("Textfältet får endast innehålla siffror för att kunna sparas.\n");
        }

        if (!isEmailFormat(textField) && textField.equals(emailField)) {
            errorMessage.append("Textfältet måste innehålla en giltig email-adress för att kunna sparas.\n");
        }
        tooltip.setText(errorMessage.toString());
        tooltip.setFont(new Font(18));
        Tooltip.install(textField, tooltip);
    }
}
