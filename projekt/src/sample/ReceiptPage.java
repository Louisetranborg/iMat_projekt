package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.text.DecimalFormat;

public class ReceiptPage extends AnchorPane {


    @FXML private Label orderNumber;
    @FXML private Label orderDate;
    @FXML private Label deliveryAdr;
    @FXML private Label totalPrice;

    double totalAmountPrice;
    int totalAmount;

    SearchController parentController;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

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

        for(ShoppingItem item : order.getItems()) {
            System.out.println(item.toString() + " : " + item.getTotal() + " : " + item.getAmount());

            totalAmountPrice += item.getTotal();
          //  totalAmount += item.getAmount();
        }
        totalAmountPrice += 45;

        orderNumber.setText(String.valueOf(order.getOrderNumber()));

        String month = order.getDate().toString().substring(4,7);

        int day = Integer.valueOf(order.getDate().toString().substring(8,10));
        String time = order.getDate().toString().substring(11,16);
        int year = Integer.valueOf(order.getDate().toString().substring(25,29));

        //TODO Ändra så att det faktiskt fungerar för andra datum och månader.
        orderDate.setText(day + "/" + parentController.convertMonthStringToInt(month) + " - " + year + " | " + time);

        totalPrice.setText(decimalFormat.format(totalAmountPrice)+ " kr");
    }

    @FXML
    protected void onClickBackToHistory(){
        parentController.productScrollPane.setVvalue(parentController.actualVValue);
        //TODO fixa så att man endast kallar en activate/showHistoryPage.
        parentController.productScrollPane.setContent(parentController.historyPage);
    }
}
