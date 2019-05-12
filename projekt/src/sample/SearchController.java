package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class SearchController implements Initializable {

    @FXML private TextField searchBox;          //Detta är sökrutan
    @FXML private FlowPane categoryPane;        //Detta är FlowPane för kategorierna, där vi stoppar in CategoryItem
    @FXML private FlowPane cartPane;            //Detta är FlowPane för varukorgen
    @FXML private FlowPane productFlowPane;
    @FXML private Button minSidaButton;         //Detta är min sida-knappen
    @FXML private Label loginLable;             //Detta är texten i headers som just nu säger "inloggad som..."
    @FXML private ScrollPane categoryScrollPane;//ScrollPane för kategorierna
    @FXML private ScrollPane cartScrollPane;    //ScrollPane för varukorgen
    @FXML private ScrollPane productScrollPane; //ScrollPane för produkterna i mitten av sidan

    IMatDataHandler iMatDataHandler = IMatDataHandler.getInstance();    //Vår iMatDataHandler
    private Map<String, ProductItem> productItemMap = new HashMap<String, ProductItem>();    //Skall användas för att få rätt på produkterna i mitten
    ToggleGroup toggleGroup = new ToggleGroup(); //ToggleGroup för att fixa så att bara en kategori kan väljas åt gången

    private void fillCategoryPane(){    //Fyller categoryPane med alla kategorierna
        for(ProductCategory productCategory: ProductCategory.values()){
            CategoryItem categoryItem = new CategoryItem(productCategory,this);
            categoryPane.getChildren().add(categoryItem);
        }
    }

    private void createProductItems(){ //Tillverkar alla varorna och lägger dem i vår Map(categoryItemMap)
        for(Product product: iMatDataHandler.getProducts()){
            ProductItem productItem = new ProductItem(product,this);
            productItemMap.put(product.getName(), productItem);
            productFlowPane.getChildren().add(productItem); //Lägger ut alla varorna på framsidan, ändra om vi vill ha annan förstasida
        }
    }

    protected void updateProductPaneFromCategory(ProductCategory category){       //Uppdaterar productFlowPane utifrån kategorierna
        productFlowPane.getChildren().clear();                                    //Clear flowpane
        List<Product> products = iMatDataHandler.getProducts(category);           //Hämtar en lista med produkterna utifrån dess kategori

        for(Product product1: products){                                                    //Loopar igenom listan med de utvalda produkterna
            productFlowPane.getChildren().add(productItemMap.get(product1.getName()));      //Lägger ut dem i flowPane
        }

    }

    protected void updateProductPaneFromSearchBox(String string){ //Uppdaterar productFlowPane utifrån en string (sökning i sökrutan)
        productFlowPane.getChildren().clear();
        List<Product> products = iMatDataHandler.findProducts(string);

        for(Product product: products){
            productFlowPane.getChildren().add(productItemMap.get(product.getName()));
        }
    }

    @FXML
    private void searchInSearchBox(){  //När man söker skall productFlowPane uppdateras efter sökning
        updateProductPaneFromSearchBox(searchBox.getCharacters().toString());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        iMatDataHandler.getCustomer().setFirstName("Hjördis"); //Sätter namnet till Hjördis sålänge.
        loginLable.setText("Inloggad som " + iMatDataHandler.getCustomer().getFirstName()); //hämtar användarens namn och skriver ut det i headern.
        fillCategoryPane(); //kalla på metoden som fyller categoryPane
        createProductItems(); //kalla på metod som skapar varorna
        productFlowPane.setHgap(42); //Avstånd mellan varu-bilderna
        productFlowPane.setVgap(42); //Avstånd mellan varu-bilderna

        categoryScrollPane.addEventFilter(ScrollEvent.SCROLL,new EventHandler<ScrollEvent>() { //Gör så att man inte kan skrolla horisontiellt
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0) {
                    event.consume();
                }
            }
        });

        cartScrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() { //Gör så att man inte kan skrolla horisontiellt
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0){
                    event.consume();
                }
            }
        });

        productScrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() { //Gör så att man inte kan skrolla horisontiellt.
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0){
                    event.consume();
                }
            }
        });

    }
}
