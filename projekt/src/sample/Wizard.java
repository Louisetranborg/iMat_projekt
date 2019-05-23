package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.Button;

import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

import se.chalmers.cse.dat216.project.IMatDataHandler;

import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

import java.util.List;


public class Wizard extends StackPane {

    private SearchController parentController;

    @FXML private AnchorPane personalInfoPane;
    @FXML private AnchorPane deliveryInfoPane;
    @FXML private AnchorPane paymentInfoPane;
    @FXML private AnchorPane receiptPane;


    @FXML private Button stepOneButton;
    @FXML private Button stepTwoButton;
    @FXML private Button stepThreeButton;
    @FXML private Button stepFourButton;

    @FXML private FlowPane confirmCartFlowPane;




    private void confirmOrder(){
        IMatDataHandler.getInstance().placeOrder(true);

        List<ShoppingItem> orderedShoppingItems = IMatDataHandler.getInstance().getOrders().get(IMatDataHandler.getInstance().getOrders().size() - 1).getItems();
        confirmCartFlowPane.getChildren().clear();
        for(ShoppingItem shoppingItem: orderedShoppingItems){
            confirmCartFlowPane.getChildren().add(new ReceiptItem(shoppingItem));
        }
        parentController.resetEveryShoppingItem();

        //parentController.shoppingCartPane.getCartFlowPaneWrap().getChildren().add(0,cartFlowPaneWrap.getChildren().get(0));     //sätter tillbaka den flowpane som hade alla CartItems till den vanliga varukorgen innan vi ger Wizard:ens ScrollPane en ny flowpane med ReceiptItems
        //cartFlowPaneWrap.getChildren().add(0,flowPane);     //sätter en flowpane med "kvitto"-items i wizard:ens ScrollPane

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
        selectCorrectStepButton(4);
        confirmOrder();
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

    }

    private void selectCorrectStepButton(int i){
        switch (i){
            case(1):
                stepOneButton.setStyle("-fx-background-color: #1565C0; -fx-text-fill: #FFFFFF;");
                stepTwoButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                stepThreeButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                stepFourButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                break;
            case(2):
                stepOneButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                stepTwoButton.setStyle("-fx-background-color: #1265c0; -fx-text-fill: #FFFFFF;");
                stepThreeButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                stepFourButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                break;
            case(3):
                stepOneButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                stepTwoButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                stepThreeButton.setStyle("-fx-background-color: #1565c0; -fx-text-fill: #FFFFFF;");
                stepFourButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                break;
            case(4):
                stepOneButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                stepTwoButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                stepThreeButton.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: #000000;");
                stepFourButton.setStyle("-fx-background-color: #1565c0; -fx-text-fill: #FFFFFF;");
        }
    }

}

