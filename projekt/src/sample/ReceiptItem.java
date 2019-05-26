package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.ShoppingItem;
import java.io.IOException;
import java.text.DecimalFormat;

public class ReceiptItem extends AnchorPane {

    @FXML
    private ImageView image;
    @FXML
    private Label title;
    @FXML
    private Label amount;
    @FXML
    private Label price;

    private ShoppingItem shoppingItem;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public ReceiptItem(ShoppingItem shoppingItem){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/receiptItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        } catch(IOException exception){
            throw new RuntimeException(exception);
        }

        this.shoppingItem = shoppingItem;
        image.setImage(IMatDataHandler.getInstance().getFXImage(shoppingItem.getProduct()));
        title.setText(shoppingItem.getProduct().getName());
        amount.setText(shoppingItem.getAmount() + shoppingItem.getProduct().getUnitSuffix());
        price.setText(decimalFormat.format(shoppingItem.getTotal()) + " kr");

    }
}
