package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.User;

import java.io.IOException;

public class MyPage extends AnchorPane {

    @FXML private AnchorPane myPageFront;
    @FXML private AnchorPane mypageFlow;


    private SearchController parentController;

    public MyPage(SearchController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("test.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);


        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }


        this.parentController = parentController;

    }
}
