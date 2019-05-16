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
    @FXML private AnchorPane productDetailView;
    @FXML private AnchorPane frontPane;
    @FXML private ImageView closeUpImage;
    @FXML private Label closeUpName;
    @FXML private AnchorPane cartPaneWrap;

    IMatDataHandler iMatDataHandler = IMatDataHandler.getInstance();     //Vår iMatDataHandler
    private Map<String, ProductItem> productItemMap = new HashMap<String, ProductItem>();    //Skall användas för att få rätt på produkterna i mitten
    ToggleGroup toggleGroup = new ToggleGroup(); //ToggleGroup för att fixa så att bara en kategori kan väljas åt gången
    ShoppingCartPane shoppingCartPane = new ShoppingCartPane(iMatDataHandler.getShoppingCart(), this);

   protected void openProductDetailView(Product product){ //Öppnar mer info om en produkt
       populateProductDetailView(product);
       productDetailView.toFront();
    }

    @FXML
    private void closeProductDetailView(){ //Stänger rutan med mer info om en produkt
        frontPane.toFront();
    }

    @FXML
    private void mouseTrap(Event event){ //konsumerar ett event
        event.consume();
    }

    private void populateProductDetailView(Product product){
       closeUpImage.setImage(iMatDataHandler.getFXImage(product));
       closeUpName.setText(product.getName());
    }

    private void fillCategoryPane(){    //Fyller categoryPane med alla kategorierna
        for(ProductCategory productCategory: ProductCategory.values()){
            CategoryItem categoryItem = new CategoryItem(productCategory,this);
            categoryFlowPane.getChildren().add(categoryItem);
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


    protected void updateProductPaneFromString(String string){ //Uppdaterar productFlowPane utifrån en string (sökning i sökrutan)
        productFlowPane.getChildren().clear();
        List<Product> products = iMatDataHandler.findProducts(string); //skapar en lista med produkter utifrån en redan skriven metod som hittar passande produkter

        for(Product product: products){ //Loopar igenom listan och lägger till dem i productFlowPane
            productFlowPane.getChildren().add(productItemMap.get(product.getName()));
        }
        //Följande for-loopar är för att varorna inom en kategori skall komma upp om man söker på denna kategori
        //Exempelvis om man söker på pasta, så skall alla pastasorter komma upp, trots att de inte innehåller pasta i sin rubrik, utan för att de ingår i den kategorin
        for(ProductCategory productCategory: ProductCategory.values()){ //Loopar igenom alla kategorierna
            if(productCategory.toString().toLowerCase().contains(string.toLowerCase())){ //Kollar om ens string matchar med någon kategori
                List<Product> productsByCategory = iMatDataHandler.getProducts(productCategory); //Skapar en lista med alla produkter inom denna kategori
            for(Product product: productsByCategory){ //Loopar igenom denna lista med produkter
                    productFlowPane.getChildren().add(productItemMap.get(product.getName())); //Lägger till dem i productFlowPane
                }
            }
        }
    }

    @FXML
    private void searchInSearchBox(){  //När man söker skall productFlowPane uppdateras efter sökning
        updateProductPaneFromString(searchBox.getCharacters().toString());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        iMatDataHandler.getCustomer().setFirstName("Hjördis"); //Sätter namnet till Hjördis sålänge.
        loginLable.setText("Inloggad som " + iMatDataHandler.getCustomer().getFirstName()); //hämtar användarens namn och skriver ut det i headern.
        fillCategoryPane(); //kalla på metoden som fyller categoryPane
        createProductItems(); //kalla på metod som skapar varorna
        productFlowPane.setHgap(42); //Avstånd mellan varu-bilderna
        productFlowPane.setVgap(42); //Avstånd mellan varu-bilderna

        cartPaneWrap.getChildren().add(shoppingCartPane);


        categoryScrollPane.addEventFilter(ScrollEvent.SCROLL,new EventHandler<ScrollEvent>() { //Gör så att man inte kan skrolla horisontiellt
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0) {
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
