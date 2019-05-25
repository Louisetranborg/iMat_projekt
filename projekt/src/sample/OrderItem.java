package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class OrderItem extends HBox {

    //TODO Lägg in order som krav för konstruktor för att koppla saker.
    public OrderItem(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/orderItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        } catch(IOException exception){
            throw new RuntimeException(exception);
        }

    }
}
