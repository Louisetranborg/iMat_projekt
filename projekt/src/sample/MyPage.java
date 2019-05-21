package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.User;

import java.io.IOException;

public class MyPage extends AnchorPane {

    @FXML private AnchorPane myPageFront;
    @FXML private AnchorPane mypageFlow;
    @FXML private RadioButton favoriter;
    @FXML private RadioButton historik;
    @FXML private RadioButton personuppgifter;
    @FXML private RadioButton sparade;
    @FXML private AnchorPane openCatMinaSidor;


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
        ToggleGroup minaSidorKategorier = new ToggleGroup();
        favoriter.setToggleGroup(minaSidorKategorier);
        historik.setToggleGroup(minaSidorKategorier);
        personuppgifter.setToggleGroup(minaSidorKategorier);
        sparade.setToggleGroup(minaSidorKategorier);

        favoriter.getStyleClass().remove("radio-button"); //Tar bort utseendet för radio-button
        favoriter.getStyleClass().add("toggle-button");      //Ändrar utseendet så det ser ut som en kanpp
        historik.getStyleClass().remove("radio-button"); //Tar bort utseendet för radio-button
        historik.getStyleClass().add("toggle-button");
        personuppgifter.getStyleClass().remove("radio-button"); //Tar bort utseendet för radio-button
        personuppgifter.getStyleClass().add("toggle-button");
        sparade.getStyleClass().remove("radio-button"); //Tar bort utseendet för radio-button
        sparade.getStyleClass().add("toggle-button");


    }

    @FXML
     public void onClick(){
        openCatMinaSidor.toFront();

    }


}
