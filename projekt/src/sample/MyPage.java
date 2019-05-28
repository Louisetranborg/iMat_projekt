package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import se.chalmers.cse.dat216.project.ShoppingCart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyPage extends StackPane {

    @FXML VBox settingsPane;
    @FXML
    AnchorPane historyPage;
    @FXML AnchorPane favoritePage;
    @FXML AnchorPane savedCartPage;
    @FXML AnchorPane personalDataPage;
    @FXML VBox historyOrderVbox;
    @FXML
    TilePane historyTilePane;
    @FXML TilePane historyProductTilePane;
    @FXML AnchorPane historyItems;

    private List<ShoppingList> shoppingLists = new ArrayList<>();

    protected List<ShoppingList> getShoppingLists(){
        return shoppingLists;
    }


    protected void addNewShoppingList(ShoppingList shoppingList){       //lägger till en ny inköpslista och lägger den i flowpane
        shoppingLists.add(shoppingList);
    }

    public MyPage(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/myPage.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        } catch(IOException exception){
            throw new RuntimeException(exception);
        }

       // OrderItem order = new OrderItem();
       // OrderItem order2 = new OrderItem();
       // OrderItem order3 = new OrderItem();
       // historyOrderVbox.getChildren().addAll(order, order2, order3);

    }

    @FXML
    protected void onClick(){

    }
}
