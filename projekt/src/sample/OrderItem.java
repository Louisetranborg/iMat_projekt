package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class OrderItem extends HBox {

    @FXML Label amount;
    @FXML Label price;
    @FXML Label date;
    //@FXML Label ;
    Order order;
    double totalAmountPrice;
    int totalAmount;

    SearchController parentController;

    public OrderItem(SearchController parentController, Order order){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/orderItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        } catch(IOException exception){
            throw new RuntimeException(exception);
        }
        this.parentController = parentController;
        this.order = order;


       // order.getDate().
        for(ShoppingItem item : order.getItems()) {
            System.out.println(item.toString() + " : " + item.getTotal() + " : " + item.getAmount());
            totalAmountPrice += item.getTotal();
            totalAmount += item.getAmount();
        }
        totalAmountPrice += 45;

        //TODO Ändra så att det faktiskt fungerar för andra datum och månader.
        date.setText(order.getDate().toString().substring(0,7));
        //TODO byt ut till amount for varje,
        amount.setText(totalAmount+" st");
        price.setText(totalAmountPrice+" kr");

    }

    @FXML
    private void onClickShowItems(){
        parentController.updateHistoryShowItems(order);
    }
}
