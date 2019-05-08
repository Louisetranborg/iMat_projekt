package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchController implements Initializable {

    @FXML private TextField searchBox;
    @FXML private FlowPane categoryPane;
    @FXML private Button minSidaButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
