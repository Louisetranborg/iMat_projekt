package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.Button;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import se.chalmers.cse.dat216.project.IMatDataHandler;

import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

import java.text.DecimalFormat;
import java.util.List;


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

    @FXML private AnchorPane paymentInfoPane;
    @FXML private DatePicker datePicker;
    @FXML private TextField cardPersonName;
    @FXML private Label sum2;
    @FXML private Label shipping2;
    @FXML private Label totalSum2;
    @FXML private Label moms2;


    @FXML private AnchorPane receiptPane;
    @FXML private Label orderNumber;
    @FXML private Label orderDate;
    @FXML private Label deliveryAdr;
    @FXML private Label totalPrice;
    //@FXML private Label totalAmount;

    @FXML private Button stepOneButton;
    @FXML private Button stepTwoButton;
    @FXML private Button stepThreeButton;

    @FXML private FlowPane confirmCartFlowPane;

    @FXML private AnchorPane firstCartPane;
    @FXML private FlowPane firstCart;

    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

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

        datePicker.editorProperty().get().setFont(new Font("Roboto-Regular", 18));

        shipping1.setText("45 kr");
        shipping2.setText("45 kr");
    }

    private void confirmOrder(){
        totalPrice.setText(decimalFormat.format(parentController.iMatDataHandler.getShoppingCart().getTotal()) + " kr");
        //totalAmount.setText(String.valueOf(calculateTotalAmount()));
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

    /*
    private double calculateTotalAmount(){
        double totalAmount = 0;
        for(ShoppingItem shoppingItem: parentController.iMatDataHandler.getShoppingCart().getItems()){
            totalAmount = totalAmount + shoppingItem.getAmount();
        }return totalAmount;
    }

     */


    /*
    protected FlowPane getConfirmCartFlowPane(){
        return confirmCartFlowPane;
    }
     */


    protected void updateOverlookLabels(){
        sum1.setText(decimalFormat.format(parentController.iMatDataHandler.getShoppingCart().getTotal()) + " kr");
        sum2.setText(decimalFormat.format(parentController.iMatDataHandler.getShoppingCart().getTotal()) + " kr");
        totalSum1.setText((parentController.iMatDataHandler.getShoppingCart().getTotal() + 45) + " kr");
        totalSum2.setText((parentController.iMatDataHandler.getShoppingCart().getTotal() + 45) + " kr");
        moms1.setText(decimalFormat.format(parentController.iMatDataHandler.getShoppingCart().getTotal() * 0.12) + "kr");
        moms2.setText(decimalFormat.format(parentController.iMatDataHandler.getShoppingCart().getTotal() * 0.12) + " kr");
    }


    protected FlowPane getFirstCart(){
        return firstCart;
    }

    @FXML
    protected void backToShopping(){
        parentController.wizardWrap.toBack();
        parentController.activateShoppingView();
        //cartFlowPaneWrap.getChildren().clear();
    }

    protected void start(){
        firstCartPane.setVisible(true);
        personalInfoPane.setVisible(false);
        paymentInfoPane.setVisible(false);
        receiptPane.setVisible(false);
        selectCorrectStepButton(1);
    }

    @FXML
    private void fromStartToPersonal(){
        firstCartPane.setVisible(false);
        personalInfoPane.setVisible(true);
        paymentInfoPane.setVisible(false);
        receiptPane.setVisible(false);
        updateOverlookLabels();
    }

    @FXML
    private void fromPersonalToPayment(){
        firstCartPane.setVisible(false);
        personalInfoPane.setVisible(false);
        paymentInfoPane.setVisible(true);
        receiptPane.setVisible(false);
        selectCorrectStepButton(2);
        updateOverlookLabels();
    }

    @FXML
    private void fromPaymentToReceipt(){
        firstCartPane.setVisible(false);
        personalInfoPane.setVisible(false);
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
