package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.CartEvent;
import se.chalmers.cse.dat216.project.ShoppingCart;
import se.chalmers.cse.dat216.project.ShoppingCartListener;

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.IOException;

public class Help extends AnchorPane {

    SearchController parentController;
    @FXML private ImageView closeHelpPage;

    @FXML private Button closeHelpButton;


    public Help(SearchController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml_filer/help.fxml"));        //Laddar in rätt fxml-fil
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.parentController = parentController;



    }


    @FXML
    private void closeHelp(){
        parentController.helpWrap.toBack();


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
