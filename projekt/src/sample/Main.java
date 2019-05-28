package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import se.chalmers.cse.dat216.project.IMatDataHandler;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("fxml_filer/sample.fxml"));
        primaryStage.setTitle("IMat");
        primaryStage.setScene(new Scene(root, 1440, 900));  //storleken på fömstret
        primaryStage.show();
        Font.loadFont(getClass().getResourceAsStream("src/Typsnitt/Roboto-Regular.ttf"),14);

        //Denna metod låter backend spara allt så att det finns kvar till nästa gång.
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                IMatDataHandler.getInstance().shutDown();
                Platform.exit();
                System.exit(0);
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
