package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.CartEvent;
import se.chalmers.cse.dat216.project.ShoppingCart;
import se.chalmers.cse.dat216.project.ShoppingCartListener;

import javax.swing.text.html.ImageView;
import java.io.IOException;

public class Help extends AnchorPane {

    SearchController parentController;
    Node node;
    @FXML private ImageView closeHelpPage;
    @FXML private Button closeHelpButton;

    public Help(SearchController parentController, Node node) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/help.fxml"));        //Laddar in rätt fxml-fil
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.node = node;
        this.parentController = parentController;
    }

    @FXML
    private void closeHelp(){
        //parentController.helpWrap.toBack();
        parentController.productScrollPane.setContent(node);
    }
    @FXML
    private void glow(){
        closeHelpButton.setEffect(new Glow(0.7));
    }

    @FXML
    private void removeGlow(){
        closeHelpButton.setEffect(new Glow(0));
    }

}
