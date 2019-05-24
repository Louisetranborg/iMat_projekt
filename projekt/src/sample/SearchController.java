package sample;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.RadioButton;

import se.chalmers.cse.dat216.project.*;

import java.net.URL;
import java.util.*;

public class SearchController implements Initializable {

    @FXML private TextField searchBox;                                          //Detta är sökrutan
    @FXML private FlowPane categoryFlowPane;                                    //Detta är FlowPane för kategorierna, där vi stoppar in CategoryItem
    @FXML private FlowPane productFlowPane;                                     //FlowPane för produkterna, mittenraden där vi stoppar in ProductItem
    @FXML private Button minSidaButton;                                         //Detta är min sida-knappen
    @FXML private Label loginLable;                                             //Detta är texten i headers som just nu säger "inloggad som..."
    @FXML private ImageView helpIcon;
    @FXML private ScrollPane categoryScrollPane;                                //ScrollPane för kategorierna
    @FXML private ScrollPane productScrollPane;                                 //ScrollPane för produkterna i mitten av sidan
    @FXML private AnchorPane productDetailView;                                 //Detta är vår light-box som visar mer info om produkterna
    @FXML private AnchorPane frontPane;                                         //Detta är vår ancorpane för framsidan
    @FXML private ImageView closeUpImage;                                       //Detta är bilden på produkten i vår light-box
    @FXML private Label closeUpName;                                            //Detta är produktnamnet i vår light-box
    @FXML private AnchorPane cartPaneWrap;                                      //Detta är den ancorpane som vi fäster kundvagnen på
    @FXML private ImageView addButton;
    @FXML private ImageView removeButton;
    @FXML private TextField amountBox;
    @FXML protected AnchorPane wizardWrap;
    @FXML private ImageView backToStoreIcon;
    @FXML private Label backToStoreLabel;
    @FXML private Label itemNumber;
    @FXML private RadioButton test;
    @FXML private Label ecoInfo;
    @FXML private Label priceDetailView;
    @FXML private RadioButton meat;
    @FXML private RadioButton fruit;
    @FXML private RadioButton dairy;
    @FXML private RadioButton greens;
    @FXML private RadioButton sweet;
    @FXML private RadioButton bread;
    @FXML private RadioButton pantry;
    @FXML private RadioButton drinks;
    @FXML private RadioButton allCategories;
    @FXML private RadioButton searchClean;
    @FXML private Label categoryPageText;




    private ShoppingItem activeInDetailview;
    IMatDataHandler iMatDataHandler = IMatDataHandler.getInstance();                                                    //Vår iMatDataHandler
    private Map<String, ProductItem> productItemMap = new HashMap<String, ProductItem>();                               //Map som fylls med productItems
    ToggleGroup toggleGroup = new ToggleGroup();                                                                        //ToggleGroup för att fixa så att bara en kategori kan väljas åt gången
    ShoppingCartPane shoppingCartPane = new ShoppingCartPane(iMatDataHandler.getShoppingCart(), this);                   //Detta är vår kundvagn
    private Wizard wizard;






    Map<String, ShoppingItem> shoppingItemMap = new HashMap<String, ShoppingItem>();        //Map med shoppingitems, endast skapa dem en gång! Både productItem och cartItem pekar på samma shoppingItem.

    //Sätter light-boxen längs fram för att visa mer info om en produkt
    protected void openProductDetailView(ShoppingItem shoppingItem){
        populateProductDetailView(shoppingItem);
        productDetailView.toFront();
        activeInDetailview = shoppingItem;
        updateAmountInDetailView();
    }

    //Stänger light-boxen och återgår till föregående sida
    @FXML
    private void closeProductDetailView(){
        frontPane.toFront();
    }

    //Konsumerar ett event, används exempelvis för att light-boxen skall stängas när man klickar utanför den, ej när man klickar på själva light-boxen
    @FXML
    protected void mouseTrap(Event event){
        event.consume();
    }

    //Fyller light-boxen med rätt produkt-variabler
    private void populateProductDetailView(ShoppingItem shoppingItem){
        closeUpImage.setImage(iMatDataHandler.getFXImage(shoppingItem.getProduct()));
        closeUpName.setText(shoppingItem.getProduct().getName());
        itemNumber.setText("Artikelnummer: " + String.valueOf(shoppingItem.getProduct().getProductId()));
        ecoInfo.setText(isEcological(shoppingItem.getProduct()));
        priceDetailView.setText(shoppingItem.getProduct().getPrice() + " kr");
    }

    private String isEcological(Product product){
        if(product.isEcological()){
            return "Varan är ekologisk";
        } else{
            return "Varan är ej ekologisk";
        }

    }

/*
    //Fyller categoryFlowPane med alla kategorierna
    private void fillCategoryPane(){
        for(ProductCategory productCategory: ProductCategory.values()){
            CategoryItem categoryItem = new CategoryItem(productCategory,this);
            categoryFlowPane.getChildren().add(categoryItem);
        }
    }*/

    //Tillverkar alla möjliga productItems och lägger dem i vår Map(productItemMap)
    private void createProductItems(){
        for(Product product: iMatDataHandler.getProducts()){
            ShoppingItem shoppingItem = new ShoppingItem(product,0);
            shoppingItemMap.put(product.getName(), shoppingItem);                                                       //Här samlar vi våra shoppingItems!
            ProductItem productItem = new ProductItem(shoppingItem,this);
            productItemMap.put(product.getName(), productItem);
            productFlowPane.getChildren().add(productItem);                                                             //Lägger ut alla varorna på framsidan, ändra om vi vill ha annan förstasida
        }
    }
/*
    //Uppdaterar productFlowPane utifrån vilken kategori man väljer
    protected void updateProductPaneFromCategory(ProductCategory category){
        productFlowPane.getChildren().clear();
        List<Product> products = iMatDataHandler.getProducts(category);

        for(Product product1: products){
            productFlowPane.getChildren().add(productItemMap.get(product1.getName()));
        }
    }*/

//Skapar metoder för att uppdatera produktsidan och lägga i kategorier
    @FXML
    protected void updateMeat(){
        productFlowPane.getChildren().clear();
        List<Product> products = iMatDataHandler.getProducts(ProductCategory.MEAT);
        List<Product> products1 = iMatDataHandler.getProducts(ProductCategory.FISH);

        for (Product product: products1){
            products.add(product);
        }


        for(Product product12: products){
            productFlowPane.getChildren().add(productItemMap.get(product12.getName()));
        }
        categoryPageText.setText("Kött, fisk och fågel");
    }

    @FXML
    protected void updateDrinks(){
        productFlowPane.getChildren().clear();
        List<Product> products = iMatDataHandler.getProducts(ProductCategory.HOT_DRINKS);
        List<Product> products1 = iMatDataHandler.getProducts(ProductCategory.COLD_DRINKS);

        for (Product product: products1){
            products.add(product);
        }


        for(Product product12: products){
            productFlowPane.getChildren().add(productItemMap.get(product12.getName()));
        }
        categoryPageText.setText("Drycker");
    }

    @FXML
    protected void updateDaires(){
        productFlowPane.getChildren().clear();
        List<Product> products = iMatDataHandler.getProducts(ProductCategory.DAIRIES);


        for(Product product12: products){
            productFlowPane.getChildren().add(productItemMap.get(product12.getName()));
        }
        categoryPageText.setText("Mejeriprodukter");
    }

    @FXML
    protected void updateSweet(){
        productFlowPane.getChildren().clear();
        List<Product> products = iMatDataHandler.getProducts(ProductCategory.SWEET);


        for(Product product12: products){
            productFlowPane.getChildren().add(productItemMap.get(product12.getName()));
        }
        categoryPageText.setText("Sötsaker");
    }

    @FXML
    protected void updateGreens(){
        productFlowPane.getChildren().clear();
        List<Product> products = iMatDataHandler.getProducts(ProductCategory.HERB);
        List<Product> products1 = iMatDataHandler.getProducts(ProductCategory.VEGETABLE_FRUIT);

        for (Product product: products1){
            products.add(product);
        }

        products1 = iMatDataHandler.getProducts(ProductCategory.CABBAGE);

        for (Product product: products1){
            products.add(product);
        }
        products1 = iMatDataHandler.getProducts(ProductCategory.ROOT_VEGETABLE);

        for (Product product: products1){
            products.add(product);
        }


        for(Product product12: products){
            productFlowPane.getChildren().add(productItemMap.get(product12.getName()));
        }

        categoryPageText.setText("Grönsaker");
    }

    @FXML
    protected void updateFruits(){
        productFlowPane.getChildren().clear();
        List<Product> products = iMatDataHandler.getProducts(ProductCategory.CITRUS_FRUIT);
        List<Product> products1 = iMatDataHandler.getProducts(ProductCategory.EXOTIC_FRUIT);

        for (Product product: products1){
            products.add(product);
        }

        products1 = iMatDataHandler.getProducts(ProductCategory.BERRY);

        for (Product product: products1){
            products.add(product);
        }
        products1 = iMatDataHandler.getProducts(ProductCategory.MELONS);

        for (Product product: products1){
            products.add(product);
        }

        products1 = iMatDataHandler.getProducts(ProductCategory.FRUIT);

        for (Product product: products1){
            products.add(product);
        }


        for(Product product12: products){
            productFlowPane.getChildren().add(productItemMap.get(product12.getName()));
        }
        categoryPageText.setText("Frukter");
    }

    @FXML
    protected void updatePantry(){
        productFlowPane.getChildren().clear();
        List<Product> products = iMatDataHandler.getProducts(ProductCategory.POD);
        List<Product> products1 = iMatDataHandler.getProducts(ProductCategory.FLOUR_SUGAR_SALT);

        for (Product product: products1){
            products.add(product);
        }

        products1 = iMatDataHandler.getProducts(ProductCategory.NUTS_AND_SEEDS);

        for (Product product: products1){
            products.add(product);
        }
        products1 = iMatDataHandler.getProducts(ProductCategory.PASTA);

        for (Product product: products1){
            products.add(product);
        }

        products1 = iMatDataHandler.getProducts(ProductCategory.POTATO_RICE);

        for (Product product: products1){
            products.add(product);
        }


        for(Product product12: products){
            productFlowPane.getChildren().add(productItemMap.get(product12.getName()));
        }
        categoryPageText.setText("Skafferivaror");
    }


    @FXML
    protected void updateBread(){
        productFlowPane.getChildren().clear();
        List<Product> products = iMatDataHandler.getProducts(ProductCategory.BREAD);


        for(Product product12: products){
            productFlowPane.getChildren().add(productItemMap.get(product12.getName()));
        }
        categoryPageText.setText("Bröd");
    }




    //Fyller startsidan
    @FXML
    private void updateFrontPage(){
        productFlowPane.getChildren().clear();
        for(Product product: iMatDataHandler.getProducts()){
            productFlowPane.getChildren().add(productItemMap.get(product.getName()));                                                             //Lägger ut alla varorna på framsidan, ändra om vi vill ha annan förstasida
        }
        categoryPageText.setText("Rekommenderade produkter för dig");
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
        categoryPageText.setText("Sökresultat för ''" + searchBox.getCharacters().toString() + "''");
        searchClean.fire();
    }


    protected void updateAmount(ShoppingItem shoppingItem){     //Uppdaterar amount både i produkterna i kundvagnen och produkterna i flowpane i mitten
        shoppingCartPane.updateCart();
        productItemMap.get(shoppingItem.getProduct().getName()).updateAmountInProductItem();
        shoppingCartPane.getProductCartItemMap().get(shoppingItem.getProduct().getName()).updateAmountInCartItem();
        shoppingCartPane.getProductCartItemMap().get(shoppingItem.getProduct().getName()).getPrice().setText(shoppingItem.getTotal() + " kr");
    }

    protected void addItemToCart(ShoppingItem shoppingItem){
        shoppingItem.setAmount(shoppingItem.getAmount() + 1); //Ökar amount med ett
        updateAmount(shoppingItem); //Ser till att amount matchar med kundkorgens textfield
        shoppingCartPane.addProductToCart(shoppingItem);
    }

    protected void removeItemFromCart(ShoppingItem shoppingItem){
        if(shoppingItem.getAmount() > 0){
            shoppingItem.setAmount(shoppingItem.getAmount() - 1);

            if(shoppingItem.getAmount() < 1){
                shoppingCartPane.removeProductFromCart(shoppingItem);
            }

        } else{
            shoppingItem.setAmount(0);
        }

        updateAmount(shoppingItem);
    }

    @FXML
    protected void clickedOnAddButton(Event event){
        mouseTrap(event); //Infoboxen skall ej komma upp
        addItemToCart(activeInDetailview);
        updateAmountInDetailView();
    }

    @FXML
    protected void clickedOnRemoveButton(Event event){
        mouseTrap(event);
        removeItemFromCart(activeInDetailview);
        updateAmountInDetailView();
    }

    private void updateAmountInDetailView(){
        amountBox.textProperty().setValue(String.valueOf(activeInDetailview.getAmount()));
    }

    protected void wizardToFront(){
        wizardWrap.toFront();
        wizard.start();
        putCartInWizard();
        activateWizardView();
    }

    private void activateWizardView(){
        searchBox.setVisible(false);
        loginLable.setVisible(false);
        helpIcon.setVisible(false);
        minSidaButton.setVisible(false);
        backToStoreIcon.setVisible(true);
        backToStoreLabel.setVisible(true);
    }

    private void putCartInWizard(){
        wizard.getCartFlowPaneWrap().getChildren().clear();
        wizard.getCartFlowPaneWrap().getChildren().add(0,shoppingCartPane.getCartFlowPaneWrap().getChildren().get(0));       //flyttar varukorgen med alla items till wizard:ens varukorg
    }

    @FXML
    protected void wizardToBack(){
        wizardWrap.toBack();
        putCartInShopView();
        activateShoppingView();
    }

    private void putCartInShopView(){
        shoppingCartPane.getCartFlowPaneWrap().getChildren().clear();
        shoppingCartPane.getCartFlowPaneWrap().getChildren().add(0,wizard.getCartFlowPaneWrap().getChildren().get(0));      //flyttar wizard:ends varukorg med alla items till den vanliga varukorgen
    }



    protected void activateShoppingView(){
        searchBox.setVisible(true);
        loginLable.setVisible(true);
        helpIcon.setVisible(true);
        minSidaButton.setVisible(true);
        backToStoreLabel.setVisible(false);
        backToStoreIcon.setVisible(false);
    }

    //Vår initialize-metod, typ som en kontruktor
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        iMatDataHandler.getCustomer().setFirstName("Hjördis");                                                          //Sätter namnet till Hjördis sålänge.
        loginLable.setText("Inloggad som " + iMatDataHandler.getCustomer().getFirstName());                             //hämtar användarens namn och skriver ut det i headern.
        //fillCategoryPane();                                                                                             //kalla på metoden som fyller categoryPane
        productFlowPane.setHgap(40);                                                                                    //Avstånd mellan productItems i x-led
        productFlowPane.setVgap(40);                                                                                    //Avstånd mellan productItems i y-led
        createProductItems();                                                                                           //kalla på metod som skapar varorna
        cartPaneWrap.getChildren().add(shoppingCartPane);                                                               //Lägger till vår varukorg
        shoppingCartPane.createProductCartItems();  //För att ej få nullpointer, kan ej skapas innan productItems!
        wizard = new Wizard(this);
        wizardWrap.getChildren().add(wizard);
        allCategories.fire();

        amountBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                activeInDetailview.setAmount(Double.valueOf(amountBox.getText()));
                shoppingCartPane.addProductToCart(activeInDetailview);
                if(activeInDetailview.getAmount() < 1){       //Ändra om vi vill ha double-system
                    shoppingCartPane.removeProductFromCart(activeInDetailview);
                }
                updateAmount(activeInDetailview);
            }
        });
/*
        //Gör så att man inte kan skrolla horisontiellt i kategorierna
        categoryScrollPane.addEventFilter(ScrollEvent.SCROLL,new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0) {
                    event.consume();
                }
            }
        });*/

        //Gör så att man inte kan skrolla horisontiellt i bland produkterna i mitten
        productScrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0){
                    event.consume();
                }
            }
        });

        ToggleGroup categoryToggleGroup = new ToggleGroup();
        meat.setToggleGroup(categoryToggleGroup);
        dairy.setToggleGroup(categoryToggleGroup);
        fruit.setToggleGroup(categoryToggleGroup);
        drinks.setToggleGroup(categoryToggleGroup);
        sweet.setToggleGroup(categoryToggleGroup);
        greens.setToggleGroup(categoryToggleGroup);
        pantry.setToggleGroup(categoryToggleGroup);
        bread.setToggleGroup(categoryToggleGroup);
        allCategories.setToggleGroup(categoryToggleGroup);
        searchClean.setToggleGroup(categoryToggleGroup);

        meat.getStyleClass().remove("radio-button");
        meat.getStyleClass().add("toggle-button");

        dairy.getStyleClass().remove("radio-button");
        dairy.getStyleClass().add("toggle-button");

        fruit.getStyleClass().remove("radio-button");
        fruit.getStyleClass().add("toggle-button");

        drinks.getStyleClass().remove("radio-button");
        drinks.getStyleClass().add("toggle-button");

        sweet.getStyleClass().remove("radio-button");
        sweet.getStyleClass().add("toggle-button");

        greens.getStyleClass().remove("radio-button");
        greens.getStyleClass().add("toggle-button");

        pantry.getStyleClass().remove("radio-button");
        pantry.getStyleClass().add("toggle-button");

        bread.getStyleClass().remove("radio-button");
        bread.getStyleClass().add("toggle-button");

        allCategories.getStyleClass().remove("radio-button");
        allCategories.getStyleClass().add("toggle-button");

        searchClean.getStyleClass().remove("radio-button");

    }

}