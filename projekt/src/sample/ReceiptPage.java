package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;

import java.io.IOException;

public class ReceiptPage extends AnchorPane {


    @FXML private Label orderNumber;
    @FXML private Label orderDate;
    @FXML private Label deliveryAdr;
    @FXML private Label totalPrice;

    SearchController parentController;

    public ReceiptPage(Order order, SearchController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/receipt.fxml"));        //Laddar in rätt fxml-fil
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }


        this.parentController = parentController;
        orderNumber.setText(String.valueOf(order.getOrderNumber()));
        orderDate.setText(String.valueOf(order.getDate()));
        totalPrice.setText(String.valueOf("100"));
    }

    @FXML
    protected void onClickAddHistoryToCart(){
        parentController.addAllHistoryItemsToCart();
    }

    @FXML
    protected void onClickBackToHistory(){
        parentController.productScrollPane.setVvalue(parentController.actualVValue);
        //TODO fixa så att man endast kallar en activate/showHistoryPage.
        parentController.productScrollPane.setContent(parentController.historyPage);
    }
}
