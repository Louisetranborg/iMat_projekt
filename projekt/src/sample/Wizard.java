package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.*;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import se.chalmers.cse.dat216.project.*;

import java.io.IOException;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Wizard extends StackPane {

    private SearchController parentController;

    @FXML private AnchorPane personalInfoPane;
    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField telNumber;
    @FXML private TextField mail;
    @FXML private TextField postCode;
    @FXML private TextField city;
    @FXML private TextField adress;
    @FXML private Label sum1;
    @FXML private Label shipping1;
    @FXML private Label totalSum1;
    @FXML private Label moms1;
    @FXML private Label errorMessageStep2;


    @FXML private AnchorPane paymentInfoPane;
    @FXML private DatePicker datePicker;
    @FXML private TextField cardholderTextField;
    @FXML private ToggleButton visaButton;
    @FXML private ToggleButton mastercardButton;
    @FXML private TextField validMonthTextField;
    @FXML private TextField validYearTextField;
    @FXML private TextField cvcTextField;
    @FXML private Label sum2;
    @FXML private Label shipping2;
    @FXML private Label totalSum2;
    @FXML private Label moms2;
    @FXML private Label errorMessageStep3;
    @FXML private Button confirmPaymentButton;
    @FXML private TextField cardnumberTextField1;
    @FXML private TextField cardnumberTextField2;
    @FXML private TextField cardnumberTextField3;
    @FXML private TextField cardnumberTextField4;


    @FXML private AnchorPane receiptPane;
    @FXML private Label orderNumber;
    @FXML private Label orderDate;
    @FXML private Label deliveryAdr;
    @FXML private Label totalPrice;
    //@FXML private Label totalAmount;

    @FXML private Button stepOneButton;
    @FXML private Button stepTwoButton;
    @FXML private Button stepThreeButton;
    @FXML private Label confirmedLabel;
    @FXML private Rectangle stepLineOne;
    @FXML private Rectangle stepLineTwo;

    @FXML private FlowPane confirmCartFlowPane;

    @FXML private AnchorPane firstCartPane;
    @FXML private FlowPane firstCart;
    @FXML private ScrollPane checkoutScrollPane;
    @FXML private CheckBox saveCheckBox;
    @FXML private Label totalaPrisetLabel;
    @FXML private TextField nameYourListTextField;
    @FXML private Button backToStoreButton;

    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    ToggleGroup cardToggleGroup = new ToggleGroup();

    private Map<String, FirstCartItem> productFirstCartItemMap = new HashMap<String, FirstCartItem>();

    protected Map<String,FirstCartItem> getProductFirstCartItemMap(){
        return productFirstCartItemMap;
    }

    protected void createFirstProductCartItems(){
        for(Product product: parentController.iMatDataHandler.getProducts()){
            FirstCartItem firstCartItem = new FirstCartItem(product,parentController);
            productFirstCartItemMap.put(product.getName(),firstCartItem);
        }
    }

    public void updateFirstCartItems(){
        firstCart.getChildren().clear();
        for(ShoppingItem shoppingItem : parentController.iMatDataHandler.getShoppingCart().getItems()){
            firstCart.getChildren().add(0,productFirstCartItemMap.get(shoppingItem.getProduct().getName()));
        }
        totalaPrisetLabel.setText("Totalt " + decimalFormat.format(parentController.iMatDataHandler.getShoppingCart().getTotal()) + " kr");
    }

    public Wizard(SearchController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/checkout.fxml"));        //Laddar in rätt fxml-fil
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader. load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.parentController = parentController;

        selectCorrectStepButton(1);

        datePicker.editorProperty().get().setFont(new Font("Avenir Book", 18));

        shipping1.setText("45 kr");
        shipping2.setText("45 kr");


        checkoutScrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0) {
                    event.consume();
                }
            }
        });

        fillErrorIconMap();
        visaButton.setToggleGroup(cardToggleGroup);
        mastercardButton.setToggleGroup(cardToggleGroup);

        cvcTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.toCharArray().length > 3){
                    cvcTextField.setText(oldValue);
                }
            }
        });

        validMonthTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.toCharArray().length > 2){
                    validMonthTextField.setText(oldValue);
                }
            }
        });

        validYearTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.toCharArray().length > 2){
                    validYearTextField.setText(oldValue);
                }
            }
        });

        visaButton.getStyleClass().clear();
        visaButton.getStyleClass().add("cardNotError");
        mastercardButton.getStyleClass().clear();
        mastercardButton.getStyleClass().add("cardNotError");

        datePicker.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                highlightEmptyError(datePicker.getEditor());
            }
        });

        visaButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                highlightCardIsNotSelectedError();
            }
        });

        mastercardButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                highlightCardIsNotSelectedError();
            }
        });

        //TODO denna uppdaterar bara första gångne jag gör köp. Inte andra.
        datePicker.setDayCellFactory(picker -> new DateCell(){
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date, empty);
                LocalDate day = LocalDate.now().plusDays(2);

                setDisable(empty || date.compareTo(day) < 0);
            }

        });

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

        parentController.implementMaxLimitInTextfield(cardnumberTextField4, 4);
        parentController.implementMaxLimitInTextfield(cardnumberTextField3, 4);
        parentController.implementMaxLimitInTextfield(cardnumberTextField2, 4);
        parentController.implementMaxLimitInTextfield(cardnumberTextField1, 4);


        parentController.implementOnlyDigitsAllowed(validMonthTextField);
        parentController.implementOnlyDigitsAllowed(validYearTextField);
        parentController.implementOnlyDigitsAllowed(cvcTextField);
        parentController.implementOnlyDigitsAllowed(postCode);

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

    private void fillCreditCardNumberTextField(){
        if(!parentController.iMatDataHandler.getCreditCard().getCardNumber().isEmpty()){
            int length = parentController.iMatDataHandler.getCreditCard().getCardNumber().length();
            if(length >= 4) {
                cardnumberTextField1.setText(parentController.iMatDataHandler.getCreditCard().getCardNumber().substring(0, 4));
            }
            if(length >= 8) {
                cardnumberTextField2.setText(parentController.iMatDataHandler.getCreditCard().getCardNumber().substring(4, 8));
            }
            if(length >= 12) {
                cardnumberTextField3.setText(parentController.iMatDataHandler.getCreditCard().getCardNumber().substring(8, 12));
            }
            if(length == 16) {
                cardnumberTextField4.setText(parentController.iMatDataHandler.getCreditCard().getCardNumber().substring(12, 16));
            }
        }
    }

    private void selectChosenCardType(){
        if(!parentController.iMatDataHandler.getCreditCard().getCardType().isEmpty()) {
            if(parentController.iMatDataHandler.getCreditCard().getCardType().equals("Mastercard")) {
                mastercardButton.setSelected(true);
            }
            if(parentController.iMatDataHandler.getCreditCard().getCardType().equals("Visa")) {
                visaButton.setSelected(true);
            }
        }
    }

    private void confirmOrder(){
        totalPrice.setText(decimalFormat.format(parentController.iMatDataHandler.getShoppingCart().getTotal() + 45) + " kr");
        for(ShoppingItem item : parentController.iMatDataHandler.getShoppingCart().getItems())  {
            System.out.println(item.getAmount());
        }

        Order order = IMatDataHandler.getInstance().placeOrder(true);

        for (ShoppingItem shoppingItem : order.getItems()){
            parentController.updateProductAmountInAllItems(new ShoppingItem(shoppingItem.getProduct(), 0));
        }


        List<ShoppingItem> orderedShoppingItems = order.getItems();
        confirmCartFlowPane.getChildren().clear();
        for(ShoppingItem shoppingItem: orderedShoppingItems){
            confirmCartFlowPane.getChildren().add(new ReceiptItem(shoppingItem));
        }

        String month = order.getDate().toString().substring(4,7);

        int day = Integer.valueOf(order.getDate().toString().substring(8,10));
        String time = order.getDate().toString().substring(11,16);
        int year = Integer.valueOf(order.getDate().toString().substring(25,29));

        //TODO Ändra så att det faktiskt fungerar för andra datum och månader.
        orderDate.setText(day + "/" + parentController.convertMonthStringToInt(month) + " - " + year + " | " + time);


        orderNumber.setText(String.valueOf(order.getOrderNumber()));
        deliveryAdr.setText(adress.getText() + ", " + city.getText());
        clearCheckoutInfo();
        parentController.updateHistoryPage();
    }

    private void clearCheckoutInfo(){
        firstName.clear();
        lastName.clear();
        telNumber.clear();
        adress.clear();
        city.clear();
        postCode.clear();
        mail.clear();
        datePicker.getEditor().clear();
        cvcTextField.clear();
        validYearTextField.clear();
        validMonthTextField.clear();
        cardnumberTextField1.clear();
        cardnumberTextField2.clear();
        cardnumberTextField3.clear();
        cardnumberTextField4.clear();
        cardholderTextField.clear();
    }

    protected void setCheckoutInfoBasedOnPersonalInfo(){
        Customer customer = parentController.iMatDataHandler.getCustomer();
        CreditCard creditCard = parentController.iMatDataHandler.getCreditCard();
        firstName.setText(customer.getFirstName());
        lastName.setText(customer.getLastName());
        telNumber.setText(customer.getMobilePhoneNumber());
        mail.setText(customer.getEmail());
        postCode.setText(customer.getPostCode());
        city.setText(customer.getPostAddress());
        adress.setText(customer.getAddress());
        cardholderTextField.setText(customer.getFirstName() + " " + customer.getLastName());
        //TODO. förmodligen ändra så att det fylls i alla fyra.
        cardnumberTextField1.setText(creditCard.getCardNumber());
        validMonthTextField.setText(Integer.toString(creditCard.getValidMonth()));
        validYearTextField.setText(Integer.toString(creditCard.getValidYear()));
        //TODO lägg in änringen om 2 dagar från now.
        datePicker.setValue(LocalDate.now().plusDays(2));
        fillCreditCardNumberTextField();
        selectChosenCardType();
    }

    protected void updateOverlookLabels(){
        sum1.setText(decimalFormat.format(parentController.iMatDataHandler.getShoppingCart().getTotal()) + " kr");
        sum2.setText(decimalFormat.format(parentController.iMatDataHandler.getShoppingCart().getTotal()) + " kr");
        totalSum1.setText((decimalFormat.format(parentController.iMatDataHandler.getShoppingCart().getTotal() + 45) + " kr"));
        totalSum2.setText((decimalFormat.format(parentController.iMatDataHandler.getShoppingCart().getTotal() + 45) + " kr"));
        moms1.setText(decimalFormat.format(parentController.iMatDataHandler.getShoppingCart().getTotal() * 0.12) + " kr");
        moms2.setText(decimalFormat.format(parentController.iMatDataHandler.getShoppingCart().getTotal() * 0.12) + " kr");
    }

    @FXML
    protected void backToShopping(){
        parentController.wizardWrap.toBack();
        parentController.activateShoppingView();

        //Einar kommentar. Använd denna istället för att gå tillbaka.
        //TODO idag
        parentController.updateFrontPage();
    }

    protected void start(){
        updateTotalPriceInStep1();
        goToStep1();
    }

    protected void updateTotalPriceInStep1(){
        totalaPrisetLabel.setText("Totalt: " + decimalFormat.format(IMatDataHandler.getInstance().getShoppingCart().getTotal()) + " kr");
    }

    private void selectCorrectStepButton(int i){
        switch (i){
            case(1):
                stepOneButton.getStyleClass().clear();
                stepOneButton.getStyleClass().add("step-button-active");
                stepTwoButton.getStyleClass().clear();
                stepTwoButton.getStyleClass().add("step-button-inactive");
                stepThreeButton.getStyleClass().clear();
                stepThreeButton.getStyleClass().add("step-button-disabled");
                stepThreeButton.setDisable(true);
                stepLineOne.getStyleClass().clear();
                stepLineOne.getStyleClass().add("stepLineGrey");
                stepLineTwo.getStyleClass().clear();
                stepLineTwo.getStyleClass().add("stepLineGrey");
                stepLineTwo.setOpacity(0.5);
                break;
            case(2):
                stepOneButton.getStyleClass().clear();
                stepOneButton.getStyleClass().add("step-button-finished");
                stepTwoButton.getStyleClass().clear();
                stepTwoButton.getStyleClass().add("step-button-active");
                stepThreeButton.getStyleClass().clear();
                stepThreeButton.getStyleClass().add("step-button-inactive");
                stepThreeButton.setDisable(false);
                stepLineOne.getStyleClass().clear();
                stepLineOne.getStyleClass().add("stepLineGreen");
                stepLineTwo.getStyleClass().clear();
                stepLineTwo.getStyleClass().add("stepLineGrey");
                stepLineTwo.setOpacity(1);
                break;
            case(3):
                stepOneButton.getStyleClass().clear();
                stepOneButton.getStyleClass().add("step-button-finished");
                stepTwoButton.getStyleClass().clear();
                stepTwoButton.getStyleClass().add("step-button-finished");
                stepThreeButton.getStyleClass().clear();
                stepThreeButton.getStyleClass().add("step-button-active");
                stepLineOne.getStyleClass().clear();
                stepLineOne.getStyleClass().add("stepLineGreen");
                stepLineTwo.getStyleClass().clear();
                stepLineTwo.getStyleClass().add("stepLineGreen");
                stepLineTwo.setOpacity(1);
                break;
        }
    }




    //------------------------------------------------------------------------------------------------------------------ Navigation i wizard

    @FXML
    protected void goToStep1(){
        firstCartPane.setVisible(true);
        personalInfoPane.setVisible(false);
        paymentInfoPane.setVisible(false);
        receiptPane.setVisible(false);
        selectCorrectStepButton(1);
        parentController.setBackToStoreIconAble();
        parentController.setBackToStoreLabelAble();
        stepLineOne.setVisible(true);
        stepLineTwo.setVisible(true);
        stepOneButton.setVisible(true);
        stepTwoButton.setVisible(true);
        stepThreeButton.setVisible(true);
        confirmedLabel.setVisible(false);
    }

    @FXML
    private void goToStep2(){
        firstCartPane.setVisible(false);
        personalInfoPane.setVisible(true);
        paymentInfoPane.setVisible(false);
        receiptPane.setVisible(false);
        selectCorrectStepButton(2);
        updateOverlookLabels();
    }

    @FXML
    private void goToStep3(){
        if(isStep2Complete()) {
            firstCartPane.setVisible(false);
            personalInfoPane.setVisible(false);
            paymentInfoPane.setVisible(true);
            receiptPane.setVisible(false);
            selectCorrectStepButton(3);
            updateOverlookLabels();
            confirmPaymentButton.setText("Genomför köp på " + (decimalFormat.format(parentController.iMatDataHandler.getShoppingCart().getTotal() + 45) + " kr"));
        }
        handleErrorsStep2();
    }

    @FXML
    private void goToStep4(){
        if(isStep3Complete()) {
            firstCartPane.setVisible(false);
            personalInfoPane.setVisible(false);
            paymentInfoPane.setVisible(false);
            receiptPane.setVisible(true);
            parentController.setBackToStoreIconDisabled();
            parentController.setBackToStoreLabelDisabled();
            stepLineOne.setVisible(false);
            stepLineTwo.setVisible(false);
            stepOneButton.setVisible(false);
            stepTwoButton.setVisible(false);
            stepThreeButton.setVisible(false);
            confirmedLabel.setVisible(true);
            confirmOrder();
        }
        handleErrorStep3();
    }


    //------------------------------------------------------------------------------------------------------------------ Felhantering

    protected void resetErrorHandler(){
        hideErrorIcon(firstName);
        hideErrorIcon(lastName);
        hideErrorIcon(telNumber);
        hideErrorIcon(city);
        hideErrorIcon(adress);
        hideErrorIcon(postCode);
        hideErrorIcon(mail);
        hideErrorIcon(datePicker.getEditor());
        hideErrorIcon(cardnumberTextField1);
        hideErrorIcon(cardnumberTextField2);
        hideErrorIcon(cardnumberTextField3);
        hideErrorIcon(cardnumberTextField4);
        hideErrorIcon(cardholderTextField);
        hideErrorIcon(validMonthTextField);
        hideErrorIcon(validYearTextField);
        hideErrorIcon(cvcTextField);

        resetBorderOnCardTypeButton(visaButton);
        resetBorderOnCardTypeButton(mastercardButton);
        resetBordersOnTextField(firstName);
        resetBordersOnTextField(lastName);
        resetBordersOnTextField(telNumber);
        resetBordersOnTextField(city);
        resetBordersOnTextField(adress);
        resetBordersOnTextField(postCode);
        resetBordersOnTextField(mail);
        resetBordersOnTextField(datePicker.getEditor());
        resetBordersOnTextField(cardnumberTextField1);
        resetBordersOnTextField(cardnumberTextField2);
        resetBordersOnTextField(cardnumberTextField3);
        resetBordersOnTextField(cardnumberTextField4);
        resetBordersOnTextField(cardholderTextField);
        resetBordersOnTextField(validYearTextField);
        resetBordersOnTextField(validMonthTextField);
        resetBordersOnTextField(cvcTextField);

        errorMessageStep2.setVisible(false);
        errorMessageStep3.setVisible(false);
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

    private boolean isEmailFormat(TextField textField){       //returnar true om textfieldens text innehåller @ och punkt
        if(textField.getText().contains("@") && textField.getText().contains(".")){
            System.out.print(textField.getText() + "doesnt contain snabel a");
            return true;
        }
        return false;
    }

    private boolean isEmpty(TextField textField){       //returnar true om textfieldens textruta inte innehåller något
        return textField.getText().isEmpty();
    }

    private boolean isStep2Complete(){
        return !isEmpty(firstName) && !isEmpty(lastName) && !isEmpty(postCode) && !isEmpty(telNumber) && !isEmpty(mail) && !isEmpty(city)
                && !isEmpty(adress) && containsDigitsOnly(postCode) && isEmailFormat(mail) && containsDigitsOnly(telNumber);
    }

    private void handleErrorsStep2(){
        if(!isStep2Complete()){
            errorMessageStep2.setVisible(true);
        } else {
            errorMessageStep2.setVisible(false);
        }

        highlightEmptyError(firstName);
        highlightEmptyError(lastName);
        highlightEmptyError(adress);
        highlightEmptyError(city);
        highlightEmptyOrDigitsOnlyError(postCode);
        highlightEmptyOrDigitsOnlyError(telNumber);
        highlightEmptyOrEmailFormatError(mail);
    }

    private boolean isStep3Complete(){
        return !isEmpty(datePicker.getEditor()) && !isEmpty(cardnumberTextField1)  && !isEmpty(cardnumberTextField2)
                && !isEmpty(cardnumberTextField3)  && !isEmpty(cardnumberTextField4) && !isEmpty(cardholderTextField)
                && !isEmpty(validMonthTextField)   && !isEmpty(validYearTextField) && !isEmpty(cvcTextField)
                && isCardTypeSelected() && containsDigitsOnly(validYearTextField) && containsDigitsOnly(validMonthTextField)
                && containsDigitsOnly(cvcTextField);
    }

    private void handleErrorStep3(){
        if(!isStep3Complete()){
            errorMessageStep3.setVisible(true);
        } else{
            errorMessageStep3.setVisible(false);
        }

        highlightEmptyError(datePicker.getEditor());
        highlightEmptyError(cardholderTextField);
        highlightEmptyError(cardnumberTextField1);
        highlightEmptyError(cardnumberTextField2);
        highlightEmptyError(cardnumberTextField3);
        highlightEmptyError(cardnumberTextField4);
        highlightEmptyOrDigitsOnlyError(validMonthTextField);
        highlightEmptyOrDigitsOnlyError(validYearTextField);
        highlightEmptyOrDigitsOnlyError(cvcTextField);
        highlightCardIsNotSelectedError();

    }

    private void highlightEmptyError(TextField textField){      //visar vilken textfield som är fel om den är empty
        if(isEmpty(textField)){
            setRedBordersOnTextField(textField);
            showErrorIcon(textField);
            showErrorMessage(textField);
        } else {
            resetBordersOnTextField(textField);
            hideErrorIcon(textField);
        }
    }

    private void highlightEmptyOrDigitsOnlyError(TextField textField){      //visar vilken textfield som är fel om den är empty eller om den inte bara inntehåller siffror
        if(isEmpty(textField) || !containsDigitsOnly(textField)){
            setRedBordersOnTextField(textField);
            showErrorIcon(textField);
            showErrorMessage(textField);
        } else {
            resetBordersOnTextField(textField);
            hideErrorIcon(textField);
        }
    }

    private void highlightEmptyOrEmailFormatError(TextField textField){     //visar vilken textfield som är fel om den är empty eller ej i email-format
        if(isEmpty(textField) || !isEmailFormat(textField)){
            setRedBordersOnTextField(textField);
            showErrorIcon(textField);
            showErrorMessage(textField);
        } else {
            resetBordersOnTextField(textField);
            hideErrorIcon(textField);
        }
    }

    private void highlightCardIsNotSelectedError(){
        if(!isCardTypeSelected()){
            setRedBorderOnCardTypeButton(visaButton);
            setRedBorderOnCardTypeButton(mastercardButton);
        } else{
            resetBorderOnCardTypeButton(visaButton);
            resetBorderOnCardTypeButton(mastercardButton);
        }
    }

    private void setRedBorderOnCardTypeButton(ToggleButton toggleButton){
        toggleButton.getStyleClass().clear();
        toggleButton.getStyleClass().add("cardError");
    }

    private void resetBorderOnCardTypeButton(ToggleButton toggleButton){
        toggleButton.getStyleClass().clear();
        toggleButton.getStyleClass().add("cardNotError");
    }

    private void setRedBordersOnTextField(TextField textField){
        textField.setStyle("-fx-border-width: 3px; -fx-border-color: #FF0000;");
    }

    private void resetBordersOnTextField(TextField textField){
        textField.setStyle("");
    }

    private boolean isCardTypeSelected(){
        if(visaButton.isSelected()){
            return true;
        } else if(mastercardButton.isSelected()){
            return true;
        } else{
            return false;
        }
    }

    //------------------------------------------------------------------------------------------------------------------ Felhantering med icons

    @FXML private ImageView errorFirstNameIcon;
    @FXML private ImageView errorLastNameIcon;
    @FXML private ImageView errorTelNumberIcon;
    @FXML private ImageView errorMailIcon;
    @FXML private ImageView errorPostCodeIcon;
    @FXML private ImageView errorCityIcon;
    @FXML private ImageView errorAdressIcon;

    @FXML private ImageView errorDatePickerIcon;
    @FXML private ImageView errorCardTypeIcon;
    @FXML private ImageView errorCardNumberIcon1;
    @FXML private ImageView errorCardNumberIcon2;
    @FXML private ImageView errorCardNumberIcon3;
    @FXML private ImageView errorCardNumberIcon4;
    @FXML private ImageView errorCardHolderIcon;
    @FXML private ImageView errorValidMonthIcon;
    @FXML private ImageView errorValidYearIcon;
    @FXML private ImageView errorCVCIcon;

    Map<TextField, ImageView> errorIconMap = new HashMap<TextField, ImageView>();

    private void fillErrorIconMap(){
        errorIconMap.put(firstName,errorFirstNameIcon);
        errorIconMap.put(lastName,errorLastNameIcon);
        errorIconMap.put(telNumber,errorTelNumberIcon);
        errorIconMap.put(mail,errorMailIcon);
        errorIconMap.put(postCode,errorPostCodeIcon);
        errorIconMap.put(city,errorCityIcon);
        errorIconMap.put(adress,errorAdressIcon);
        errorIconMap.put(datePicker.getEditor(),errorDatePickerIcon);
        errorIconMap.put(cardnumberTextField1,errorCardNumberIcon1);
        errorIconMap.put(cardnumberTextField2,errorCardNumberIcon2);
        errorIconMap.put(cardnumberTextField3,errorCardNumberIcon3);
        errorIconMap.put(cardnumberTextField4,errorCardNumberIcon4);
        errorIconMap.put(cardholderTextField, errorCardHolderIcon);
        errorIconMap.put(validMonthTextField,errorValidMonthIcon);
        errorIconMap.put(validYearTextField,errorValidYearIcon);
        errorIconMap.put(cvcTextField,errorCVCIcon);
    }

    private void showErrorIcon(TextField textField){
        errorIconMap.get(textField).setVisible(true);
    }

    private void hideErrorIcon(TextField textField){
        errorIconMap.get(textField).setVisible(false);
    }

    private void showErrorMessage(TextField textField){
        StringBuilder errorMessage = new StringBuilder();
        Tooltip tooltip = new Tooltip();
        if(isEmpty(textField)){
            errorMessage.append("Textfältet får ej vara tomt.\n");
        }
        if(!containsDigitsOnly(textField) && (textField.equals(postCode) || textField.equals(telNumber) || textField.equals(validMonthTextField)
            || textField.equals(validYearTextField) || textField.equals(cvcTextField))){
            errorMessage.append("Textfältet får endast innehålla siffror.\n");
        }
        if(!isEmailFormat(textField) && textField.equals(mail)){
            errorMessage.append("Textfältet måste innehålla en giltig email-adress.\n");
        }
        ImageView errorIcon = errorIconMap.get(textField);
        tooltip.setText(errorMessage.toString());
        tooltip.setFont(new Font(18));
        Tooltip.install(textField, tooltip);
        Tooltip.install(errorIcon, tooltip);

    }


    //------------------------------------------------------------------------------------------------------------------


    @FXML
    private void hideOrShowShoppingListNameTextField(){     //visar/gömmer textfield där man skriver in namnet på inköpslistan
        if(!nameYourListTextField.isVisible()){
            nameYourListTextField.setVisible(true);

        } else if(nameYourListTextField.isVisible()){
            nameYourListTextField.setVisible(false);
        }
    }

}
