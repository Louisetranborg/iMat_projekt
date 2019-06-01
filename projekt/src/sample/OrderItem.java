package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class OrderItem extends HBox {

    @FXML Label amount;
    @FXML Label price;
    @FXML Label date;
    //@FXML Label ;
    Order order;
    double totalAmountPrice;
    int totalAmount;

    SearchController parentController;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

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

        String month = order.getDate().toString().substring(4,7);
        //System.out.println(month);

        int day = Integer.valueOf(order.getDate().toString().substring(8,10));
        //System.out.println("\n" + day);
        //String time = order.getDate().toString().substring(12,20);

        //TODO Ändra så att det faktiskt fungerar för andra datum och månader.
        date.setText(day + "/" + convertMonthStringToInt(month));
        //TODO byt ut till amount for varje,
        amount.setText(totalAmount+" st");
        price.setText(decimalFormat.format(totalAmountPrice) +" kr");

    }

    //Tar in en månad med 3 bokstäver och koverterar den till en int.
    public int convertMonthStringToInt(String month){
        switch (month.toLowerCase()){
            case "jan":
                return 1;
            case "feb":
                return 2;
            case "mar":
                return 3;
            case "apr":
                return 4;
            case "may":
                return 5;
            case "jun":
                return 6;
            case "jul":
                return 7;
            case "aug":
                return 8;
            case "sep":
                return 9;
            case "oct":
                return 10;
            case "nov":
                return 11;
            case "dec":
                return 12;
        }
        return 0;
    }

    @FXML
    private void onClickShowItems(){
        parentController.updateHistoryShowItems(order);
    }
}
