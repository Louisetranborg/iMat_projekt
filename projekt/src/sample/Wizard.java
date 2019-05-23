package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

import se.chalmers.cse.dat216.project.Customer;
import se.chalmers.cse.dat216.project.IMatDataHandler;

import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

import java.util.List;


public class Wizard extends StackPane {

    private SearchController parentController;

    @FXML private AnchorPane personalInfoPane;
    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField telNumber;
    @FXML private TextField mail;

    @FXML private AnchorPane deliveryInfoPane;
    @FXML private TextField postCode;
    @FXML private TextField city;
    @FXML private TextField adress;

    @FXML private AnchorPane paymentInfoPane;
    @FXML private TextField cardPersonName;

    @FXML private AnchorPane receiptPane;
    @FXML private Label orderNumber;
    @FXML private Label orderDate;
    @FXML private Label deliveryAdr;
    @FXML private Label totalPrice;

    @FXML private Button stepOneButton;
    @FXML private Button stepTwoButton;
    @FXML private Button stepThreeButton;

    @FXML private FlowPane confirmCartFlowPane;

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
        firstName.setText(parentController.iMatDataHandler.getCustomer().getFirstName());
        lastName.setText(parentController.iMatDataHandler.getCustomer().getLastName());
        telNumber.setText(parentController.iMatDataHandler.getCustomer().getMobilePhoneNumber());
        mail.setText(parentController.iMatDataHandler.getCustomer().getEmail());
        postCode.setText(parentController.iMatDataHandler.getCustomer().getPostCode());
        city.setText(parentController.iMatDataHandler.getCustomer().getPhoneNumber());
        adress.setText(parentController.iMatDataHandler.getCustomer().getAddress());
        cardPersonName.setText(parentController.iMatDataHandler.getCustomer().getFirstName() + " " + parentController.iMatDataHandler.getCustomer().getLastName());
    }

    private void confirmOrder(){
        totalPrice.setText(parentController.iMatDataHandler.getShoppingCart().getTotal() + " kr");
        IMatDataHandler.getInstance().placeOrder(true);

        List<ShoppingItem> orderedShoppingItems = IMatDataHandler.getInstance().getOrders().get(IMatDataHandler.getInstance().getOrders().size() - 1).getItems();
        confirmCartFlowPane.getChildren().clear();
        for(ShoppingItem shoppingItem: orderedShoppingItems){
            confirmCartFlowPane.getChildren().add(new ReceiptItem(shoppingItem));
        }
        parentController.resetEveryShoppingItem();

        orderNumber.setText(String.valueOf(parentController.iMatDataHandler.getOrders().get(parentController.iMatDataHandler.getOrders().size() - 1).getOrderNumber()));
        orderDate.setText(String.valueOf(parentController.iMatDataHandler.getOrders().get(parentController.iMatDataHandler.getOrders().size() - 1).getDate()));
        deliveryAdr.setText(adress.getText() + ", " + city.getText());
    }

    protected FlowPane getConfirmCartFlowPane(){
        return confirmCartFlowPane;
    }

    @FXML
    protected void backToShopping(){
        parentController.wizardWrap.toBack();
        parentController.activateShoppingView();
        //cartFlowPaneWrap.getChildren().clear();
    }

    protected void start(){
        personalInfoPane.setVisible(true);
        deliveryInfoPane.setVisible(false);
        paymentInfoPane.setVisible(false);
        receiptPane.setVisible(false);
        selectCorrectStepButton(1);
    }

    @FXML
    private void fromPersonalToDelivery(){
        personalInfoPane.setVisible(false);
        deliveryInfoPane.setVisible(true);
        paymentInfoPane.setVisible(false);
        receiptPane.setVisible(false);
        selectCorrectStepButton(2);
    }

    @FXML
    private void fromDeliveryToPayment(){
        personalInfoPane.setVisible(false);
        deliveryInfoPane.setVisible(false);
        paymentInfoPane.setVisible(true);
        receiptPane.setVisible(false);
        selectCorrectStepButton(3);
    }

    @FXML
    private void fromPaymentToReceipt(){
        personalInfoPane.setVisible(false);
        deliveryInfoPane.setVisible(false);
        paymentInfoPane.setVisible(false);
        receiptPane.setVisible(true);
        confirmOrder();
    }

    private void selectCorrectStepButton(int i){
        switch (i){
            case(1):
                stepOneButton.setStyle("-fx-background-color: #66bb6a; -fx-text-fill: #FFFFFF;");
                stepTwoButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                stepThreeButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                break;
            case(2):
                stepOneButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                stepTwoButton.setStyle("-fx-background-color: #66bb6a; -fx-text-fill: #FFFFFF;");
                stepThreeButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                break;
            case(3):
                stepOneButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                stepTwoButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                stepThreeButton.setStyle("-fx-background-color: #66bb6a; -fx-text-fill: #FFFFFF;");
                break;
        }
    }

}

