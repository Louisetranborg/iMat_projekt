package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.Customer;
import se.chalmers.cse.dat216.project.IMatDataHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchController implements Initializable {

    @FXML private TextField searchBox;
    @FXML private FlowPane categoryPane;
    @FXML private FlowPane cartPane;
    @FXML private Button minSidaButton;
    @FXML private Label loginLable;

    IMatDataHandler iMatDataHandler = IMatDataHandler.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        iMatDataHandler.getCustomer().setFirstName("Hjördis"); //Sätter namnet till Hjärdis sålänge. 
        loginLable.setText("Inloggad som " + iMatDataHandler.getCustomer().getFirstName()); //hämtar användarens namn och skriver ut det i headern.

    }
}
