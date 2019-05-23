package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MyPage extends StackPane {
    public MyPage(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/myPage.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        } catch(IOException exception){
            throw new RuntimeException(exception);
        }

    }

    @FXML
    protected void onClick(){

    }
}
