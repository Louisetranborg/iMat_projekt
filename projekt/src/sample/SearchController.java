package sample;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
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

    @FXML private TextField searchBox;                                          //Detta är sökrutan
    @FXML private FlowPane categoryFlowPane;                                    //Detta är FlowPane för kategorierna, där vi stoppar in CategoryItem
    @FXML private FlowPane productFlowPane;                                     //FlowPane för produkterna, mittenraden där vi stoppar in ProductItem
    @FXML private Button minSidaButton;                                         //Detta är min sida-knappen
    @FXML private Label loginLable;                                             //Detta är texten i headers som just nu säger "inloggad som..."
    @FXML private ScrollPane categoryScrollPane;                                //ScrollPane för kategorierna
    @FXML private ScrollPane productScrollPane;                                 //ScrollPane för produkterna i mitten av sidan
    @FXML private AnchorPane productDetailView;                                 //Detta är vår light-box som visar mer info om produkterna
    @FXML private AnchorPane frontPane;                                         //Detta är vår ancorpane för framsidan
    @FXML private ImageView closeUpImage;                                       //Detta är bilden på produkten i vår light-box
    @FXML private Label closeUpName;                                            //Detta är produktnamnet i vår light-box
    @FXML private AnchorPane cartPaneWrap;                                      //Detta är den ancorpane som vi fäster kundvagnen på

    IMatDataHandler iMatDataHandler = IMatDataHandler.getInstance();                                                    //Vår iMatDataHandler
    private Map<String, ProductItem> productItemMap = new HashMap<String, ProductItem>();                               //Map som fylls med categoryItems
    ToggleGroup toggleGroup = new ToggleGroup();                                                                        //ToggleGroup för att fixa så att bara en kategori kan väljas åt gången
    ShoppingCartPane shoppingCartPane = new ShoppingCartPane(iMatDataHandler.getShoppingCart(), this);    //Detta är vår kundvagn

   //Sätter light-boxen längs fram för att visa mer info om en produkt
   protected void openProductDetailView(Product product){
       populateProductDetailView(product);
       productDetailView.toFront();
    }

    //Stänger light-boxen och återgår till föregående sida
    @FXML
    private void closeProductDetailView(){
        frontPane.toFront();
    }

    //Konsumerar ett event, används exempelvis för att light-boxen skall stängas när man klickar utanför den, ej när man klickar på själva light-boxen
    @FXML
    private void mouseTrap(Event event){
        event.consume();
    }

    //Fyller light-boxen med rätt produkt-variabler
    private void populateProductDetailView(Product product){
       closeUpImage.setImage(iMatDataHandler.getFXImage(product));
       closeUpName.setText(product.getName());
    }

    //Fyller categoryFlowPane med alla kategorierna
    private void fillCategoryPane(){
        for(ProductCategory productCategory: ProductCategory.values()){
            CategoryItem categoryItem = new CategoryItem(productCategory,this);
            categoryFlowPane.getChildren().add(categoryItem);
        }
    }

    //Tillverkar alla möjliga productItems och lägger dem i vår Map(productItemMap)
    private void createProductItems(){
        for(Product product: iMatDataHandler.getProducts()){
            ProductItem productItem = new ProductItem(product,this);
            productItemMap.put(product.getName(), productItem);
            productFlowPane.getChildren().add(productItem);                                                             //Lägger ut alla varorna på framsidan, ändra om vi vill ha annan förstasida
        }
    }

    //Uppdaterar productFlowPane utifrån vilken kategori man väljer
    protected void updateProductPaneFromCategory(ProductCategory category){
        productFlowPane.getChildren().clear();
        List<Product> products = iMatDataHandler.getProducts(category);

        for(Product product1: products){
            productFlowPane.getChildren().add(productItemMap.get(product1.getName()));
        }
    }

    //Uppdaterar productFlowPane utifrån en String (sökning i sökrutan)
    protected void updateProductPaneFromString(String string){
        productFlowPane.getChildren().clear();
        List<Product> products = iMatDataHandler.findProducts(string);

        for(Product product: products){
            productFlowPane.getChildren().add(productItemMap.get(product.getName()));
        }
        //Följande for-loopar är för att varorna inom en kategori skall komma upp om man söker på denna kategori
        //Exempelvis om man söker på pasta, så skall alla pastasorter komma upp, trots att de inte innehåller pasta i sin rubrik, utan för att de ingår i den kategorin
        for(ProductCategory productCategory: ProductCategory.values()){                                                 //Loopar igenom alla kategorierna
            if(productCategory.toString().toLowerCase().contains(string.toLowerCase())){                                //Kollar om ens string matchar med någon kategori
                List<Product> productsByCategory = iMatDataHandler.getProducts(productCategory);                        //Skapar en lista med alla produkter inom denna kategori
            for(Product product: productsByCategory){                                                                   //Loopar igenom denna lista med produkter
                    productFlowPane.getChildren().add(productItemMap.get(product.getName()));                           //Lägger till dem i productFlowPane
                }
            }
        }
    }

    //När man söker skall productFlowPane uppdateras efter sökningen
    @FXML
    private void searchInSearchBox(){
        updateProductPaneFromString(searchBox.getCharacters().toString());
    }


    //Vår initialize-metod, typ som en kontruktor
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        iMatDataHandler.getCustomer().setFirstName("Hjördis");                                                          //Sätter namnet till Hjördis sålänge.
        loginLable.setText("Inloggad som " + iMatDataHandler.getCustomer().getFirstName());                             //hämtar användarens namn och skriver ut det i headern.
        fillCategoryPane();                                                                                             //kalla på metoden som fyller categoryPane
        createProductItems();                                                                                           //kalla på metod som skapar varorna
        productFlowPane.setHgap(42);                                                                                    //Avstånd mellan productItems i x-led
        productFlowPane.setVgap(42);                                                                                    //Avstånd mellan productItems i y-led
        cartPaneWrap.getChildren().add(shoppingCartPane);                                                               //Lägger till vår varukorg


        //Gör så att man inte kan skrolla horisontiellt i kategorierna
        categoryScrollPane.addEventFilter(ScrollEvent.SCROLL,new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0) {
                    event.consume();
                }
            }
        });

        //Gör så att man inte kan skrolla horisontiellt i bland produkterna i mitten
        productScrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0){
                    event.consume();
                }
            }
        });

    }
}
