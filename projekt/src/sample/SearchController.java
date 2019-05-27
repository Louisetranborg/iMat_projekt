package sample;


import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;

import se.chalmers.cse.dat216.project.*;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class SearchController implements Initializable {

    @FXML
    private TextField searchBox;                                          //Detta är sökrutan
    @FXML
    private VBox menuVbox;                                    //Detta är FlowPane för kategorierna, där vi stoppar in CategoryItem
    @FXML
    private FlowPane productFlowPane;                                     //FlowPane för produkterna, mittenraden där vi stoppar in ProductItem
    @FXML
    private Button minSidaButton;                                         //Detta är min sida-knappen
    @FXML
    private Label loginLable;                                             //Detta är texten i headers som just nu säger "inloggad som..."
    @FXML
    private ImageView helpIcon;
    @FXML
    private ScrollPane productScrollPane;                                 //ScrollPane för produkterna i mitten av sidan
    @FXML
    private AnchorPane productDetailView;                                 //Detta är vår light-box som visar mer info om produkterna
    @FXML
    private AnchorPane frontPane;                                         //Detta är vår ancorpane för framsidan
    @FXML
    private ImageView closeUpImage;                                       //Detta är bilden på produkten i vår light-box
    @FXML
    private Label closeUpName;                                            //Detta är produktnamnet i vår light-box
    @FXML
    private AnchorPane cartPaneWrap;                                      //Detta är den ancorpane som vi fäster kundvagnen på
    @FXML
    private ImageView addButtonDetail;
    @FXML
    private ImageView removeButton;
    @FXML
    private TextField amountBox;
    @FXML
    protected AnchorPane wizardWrap;
    @FXML
    private ImageView backToStoreIcon;
    @FXML
    private Label backToStoreLabel;
    @FXML
    private Label itemNumber;
    @FXML
    private Label ecoInfo;
    @FXML
    private Label priceDetailView;
    @FXML
    private Label categoryPageText;
    @FXML private ImageView addButtonGreenDetail;
    @FXML private ImageView addButtonGreenHoverDetail;
    @FXML private ImageView removeButtonBrown;
    @FXML private ImageView removeButtonHover;
    @FXML private ImageView closeDetailView;



    public void greenAddButtonToFrontDetail(){
        addButtonGreenDetail.toFront();
    }
    public void blackAddButtonToFrontDetail(){
        addButtonDetail.toFront();
    }
    public void brownRemoveButtonToFrontDetail(){
        removeButtonBrown.toFront();
    }
    public void blackRemoveButtonToFrontDetail(){
        removeButton.toFront();
    }
    @FXML
    private void glow(){
        closeDetailView.setEffect(new Glow(0.5));
    }

    @FXML
    private void removeGlow(){
        closeDetailView.setEffect(new Glow(0));
    }



    IMatDataHandler iMatDataHandler = IMatDataHandler.getInstance();                                                    //Vår iMatDataHandler

    private Map<String, ProductItem> productItemMap = new HashMap<String, ProductItem>();                               //Map som fylls med productItems
    Map<String, ShoppingItem> shoppingItemMap = new HashMap<String, ShoppingItem>();        //Map med shoppingitems, endast skapa dem en gång! Både productItem och cartItem pekar på samma shoppingItem.

    ShoppingCartPane shoppingCartPane = new ShoppingCartPane(iMatDataHandler.getShoppingCart(), this);                   //Detta är vår kundvagn

    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public ToggleGroup menuToggleGroup;
    private Wizard wizard;
    private ShoppingItem activeInDetailview;
    MyPage myPage = new MyPage();


    protected void resetEveryShoppingItem() {
        for (ShoppingItem item : shoppingItemMap.values()) {
            item.setAmount(0);
            updateAmount(item);
        }
    }

    //Sätter light-boxen längs fram för att visa mer info om en produkt
    protected void openProductDetailView(ShoppingItem shoppingItem) {
        populateProductDetailView(shoppingItem);
        productDetailView.toFront();
        activeInDetailview = shoppingItem;
        updateAmountInDetailView();
    }

    //Stänger light-boxen och återgår till föregående sida
    @FXML
    private void closeProductDetailView() {
        productDetailView.toBack();
    }

    //Konsumerar ett event, används exempelvis för att light-boxen skall stängas när man klickar utanför den, ej när man klickar på själva light-boxen
    @FXML
    protected void mouseTrap(Event event) {
        event.consume();
    }

    //TODO eventuellt slå ihop med en befitnligt activateLightBox metod?
    //Fyller light-boxen med rätt produkt-variabler
    private void populateProductDetailView(ShoppingItem shoppingItem) {
        closeUpImage.setImage(iMatDataHandler.getFXImage(shoppingItem.getProduct()));
        closeUpName.setText(shoppingItem.getProduct().getName());
        itemNumber.setText("Artikelnummer: " + String.valueOf(shoppingItem.getProduct().getProductId()));
        ecoInfo.setText(isEcological(shoppingItem.getProduct()));
        priceDetailView.setText(shoppingItem.getProduct().getPrice() + " " + shoppingItem.getProduct().getUnit());
    }

    private String isEcological(Product product) {
        if (product.isEcological()) {
            return "Varan är ekologisk";
        } else {
            return "Varan är ej ekologisk";
        }

    }

    public void changeCategoryPageText(String string){
        categoryPageText.setText(string);
    }


    //Fyller categoryFlowPane med alla kategorier för produkter
    private void fillCategoryPane() {
        menuVbox.getChildren().clear();
        for (CategoryItem.productCategory productCategory : CategoryItem.productCategory.values()) {
            MenuItem categoryItem = new CategoryItem(productCategory, this);
            menuVbox.getChildren().add(categoryItem);
        }
    }

    //Fyller categoryFlowPane med alla kategorier för settings
    private void fillSettingsPane() throws IOException {    //Fyller categoryPane med alla kategorierna
        menuVbox.getChildren().clear();
        for (SettingsItem.SettingsCategory settings : SettingsItem.SettingsCategory.values()) {
            MenuItem settingsItem = new SettingsItem(this, settings);
            menuVbox.getChildren().add(settingsItem);
        }
        Pane blankSpace = new Pane();
        blankSpace.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        VBox.setVgrow(blankSpace, Priority.ALWAYS); //Fyller ut tomrummet mellan settingsItem och Tillbaka-knappen.

        /*Button back = new Button();
        back.setCenterShape(true);
        back.setText("Back to Store");
        menuVbox.getChildren().addAll(blankSpace, back);*/

    }

    //Tillverkar alla möjliga productItems och lägger dem i vår Map(productItemMap)
    private void createProductItems() {
        for (Product product : iMatDataHandler.getProducts()) {
            ShoppingItem shoppingItem = new ShoppingItem(product, 0);
            shoppingItemMap.put(product.getName(), shoppingItem);                                                       //Här samlar vi våra shoppingItems!
            ProductItem productItem = new ProductItem(shoppingItem, this);
            productItemMap.put(product.getName(), productItem);
            productFlowPane.getChildren().add(productItem);                                                             //Lägger ut alla varorna på framsidan, ändra om vi vill ha annan förstasida
        }
    }

    //Uppdaterar productFlowPane utifrån vilken kategori man väljer
    protected void updateProductPaneFromCategory( List<Product> products) {
        productFlowPane.getChildren().clear();
        productScrollPane.setVvalue(0);

        //TODO lägg in så att productscrollpane och frontpane automatiskt dyker upp .toFront. (eventuellt en activateproductFLowPane)
        frontPane.toFront();

        for (Product product1 : products) {
            productFlowPane.getChildren().add(productItemMap.get(product1.getName()));

        }
    }

    //Updaterar startsidan och lägger sig längst fram i synlighetshierarkin.
    @FXML
    public void updateFrontPage() { //TODO Göra om funktionaliteten från en engångsupdate till en standard update?
        productFlowPane.getChildren().clear();
        productScrollPane.setVvalue(0);

        //TODO eventuellt bestämma vart dessa ska samlas. Förslagsvis här då och andra metoder för kalla på denna. Här kanske man kör ihop activateShoppingview också.
        productScrollPane.setContent(productFlowPane);
        frontPane.toFront();
        fillCategoryPane();
        wizardWrap.setVisible(false);

        for (Product product : iMatDataHandler.getProducts()) {
            productFlowPane.getChildren().add(productItemMap.get(product.getName()));                                                             //Lägger ut alla varorna på framsidan, ändra om vi vill ha annan förstasida
        }
        categoryPageText.setText("Rekommenderade produkter för dig");
    }


    @FXML
    private void clickOnLogo() {
        updateFrontPage();
        //TODO låg tidigare en allCategories.fire() här.
    }


    //Uppdaterar productFlowPane utifrån en String (sökning i sökrutan)
    protected void updateProductPaneFromString(String string) {
        List<Product> products = iMatDataHandler.findProducts(string);

        //TODO lägg in så att productscrollpane och frontpane automatiskt dyker upp .toFront. (eventuellt en activateproductFLowPane)
        frontPane.toFront();
        productScrollPane.setContent(productFlowPane);
        updateFrontPage();
        productFlowPane.getChildren().clear();

        for (Product product : products) {
            productFlowPane.getChildren().add(productItemMap.get(product.getName()));
        }
        //Följande for-loopar är för att varorna inom en kategori skall komma upp om man söker på denna kategori
        //Exempelvis om man söker på pasta, så skall alla pastasorter komma upp, trots att de inte innehåller pasta i sin rubrik, utan för att de ingår i den kategorin
        for (ProductCategory productCategory : ProductCategory.values()) {                                                 //Loopar igenom alla kategorierna
            if (productCategory.toString().toLowerCase().contains(string.toLowerCase())) {                                //Kollar om ens string matchar med någon kategori
                List<Product> productsByCategory = iMatDataHandler.getProducts(productCategory);                        //Skapar en lista med alla produkter inom denna kategori
                for (Product product : productsByCategory) {                                                                   //Loopar igenom denna lista med produkter
                    productFlowPane.getChildren().add(productItemMap.get(product.getName()));                           //Lägger till dem i productFlowPane
                }
            }
        }
    }


    //Byter view från productFlowpane till myPage.
    @FXML
    protected void myPagesButtonClicked() {
        updateSettingsPaneFromSettings();

        try {
            fillSettingsPane();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO eventuellt ha en update för varje stor sida. e.g updateFrontPane, updateMyPage, updateCheckout osv. och skrota alla små metoder som vi inte håller koll på.

    //Byter view från mypage till productflowpane
    protected  void updateSettingsPaneFromSettings(){
        productScrollPane.setContent(myPage);
    }


    //Onödig atm, kanske kommer till användning senare.
   /* protected void updateProductPaneFromSettings() throws IOException{       //Uppdaterar productFlowPane utifrån kategorierna
        //settingsScrollPane.toBack();
        productScrollPane.setContent(myPage);
        fillCategoryPane();


    }*/

    //När man söker skall productFlowPane uppdateras efter sökningen

    //TODO Om man skriver bara 2 bokstväer(ibland) uppdateras inte categoryPageText. Exempel är "se".
    @FXML
    private void searchInSearchBox() {
        updateProductPaneFromString(searchBox.getCharacters().toString());
        categoryPageText.setText("Sökresultat för ''" + searchBox.getCharacters().toString() + "''");
        productScrollPane.setContent(productFlowPane);
        //TODO låg tidigare en searchClean.fire() här.
    }


    protected void updateAmount(ShoppingItem shoppingItem) {     //Uppdaterar amount både i produkterna i kundvagnen och produkterna i flowpane i mitten
        shoppingCartPane.updateCart();
        productItemMap.get(shoppingItem.getProduct().getName()).updateAmountInProductItem();
        shoppingCartPane.getProductCartItemMap().get(shoppingItem.getProduct().getName()).updateAmountInCartItem();
        shoppingCartPane.getProductCartItemMap().get(shoppingItem.getProduct().getName()).getPrice().setText(decimalFormat.format(shoppingItem.getTotal()) + " kr");
    }

    protected void addItemToCart(ShoppingItem shoppingItem) {
        shoppingItem.setAmount(shoppingItem.getAmount() + 1); //Ökar amount med ett
        updateAmount(shoppingItem); //Ser till att amount matchar med kundkorgens textfield
        shoppingCartPane.addProductToCart(shoppingItem);
    }

    protected void removeItemFromCart(ShoppingItem shoppingItem) {
        if (shoppingItem.getAmount() > 0) {
            shoppingItem.setAmount(shoppingItem.getAmount() - 1);

            if (shoppingItem.getAmount() < 1) {
                shoppingCartPane.removeProductFromCart(shoppingItem);
            }

        } else {
            shoppingItem.setAmount(0);
        }

        updateAmount(shoppingItem);
    }

    @FXML
    protected void clickedOnAddButton(Event event) {
        mouseTrap(event); //Infoboxen skall ej komma upp
        addItemToCart(activeInDetailview);
        updateAmountInDetailView();
        addButtonGreenDetail.toFront();
        removeButtonBrown.toFront();
    }

    @FXML
    protected void hoverOnAddButton(Event event){
        addButtonGreenHoverDetail.toFront();

    }

    @FXML
    protected void hoverOffAddButton(Event event){
        addButtonGreenHoverDetail.toBack();

    }

    @FXML
    protected void hoverOnRemoveButton(Event event){
        removeButtonHover.toFront();
    }

    @FXML
    protected void hoverOffRemoveButton(Event event){
        removeButtonHover.toBack();
    }

    @FXML
    protected void clickedOnRemoveButton(Event event) {
        mouseTrap(event);
        removeItemFromCart(activeInDetailview);
        updateAmountInDetailView();
        if (activeInDetailview.getAmount()<1){
            addButtonDetail.toFront();
            removeButton.toFront();
        }
    }

    private void updateAmountInDetailView() {
        amountBox.textProperty().setValue(String.valueOf(activeInDetailview.getAmount()));
    }

    protected void wizardToFront() {
        wizardWrap.toFront();
        wizard.start();
        //putCartInWizard();
        putFirstCartInWizard();
        //updateBiggerCartInWizard();
        activateWizardView();
    }


    //TODO Slå ihop wizardToFront() med activate wizardview. De åstadkommer samma sak. Kolla vilka ställen som använder metoden innan remove.
    private void activateWizardView() {
        searchBox.setVisible(false);
        loginLable.setVisible(false);
        helpIcon.setVisible(false);
        minSidaButton.setVisible(false);
        backToStoreIcon.setVisible(true);
        backToStoreLabel.setVisible(true);
        wizardWrap.setVisible(true);
    }

    protected void putFirstCartInWizard() {
        wizard.getFirstCart().getChildren().clear();
        for (ShoppingItem shoppingItem : IMatDataHandler.getInstance().getShoppingCart().getItems()) {
            FirstCartItem firstCartItem = new FirstCartItem(shoppingItem, this);
            wizard.getFirstCart().getChildren().add(firstCartItem);
            firstCartItem.updateAmountBoxInFirstCartItem();
        }
    }


    @FXML
    protected void wizardToBack() {
        wizardWrap.toBack();
        //putCartInShopView();
        activateShoppingView();
        updateFrontPage();
    }

    //TODO undersöka en eventuell ihopkoppling av activateshoppingview och updateFrontPage. Så att det finns en tydlig funktion för att byta "view" åt alla håll.
    protected void activateShoppingView() {
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
        iMatDataHandler.getCustomer().setLastName("Johansson");
        iMatDataHandler.getCustomer().setMobilePhoneNumber("073-333 33 33");
        iMatDataHandler.getCustomer().setEmail("hjördis.johansson@gmail.se");
        iMatDataHandler.getCustomer().setPostCode("333 33");
        iMatDataHandler.getCustomer().setAddress("Kallebäcksvägen 3");
        iMatDataHandler.getCustomer().setPhoneNumber("Göteborg");

        menuToggleGroup = new ToggleGroup();

        loginLable.setText("Inloggad som " + iMatDataHandler.getCustomer().getFirstName());                             //hämtar användarens namn och skriver ut det i headern.

        productFlowPane.setHgap(40);                                                                                    //Avstånd mellan productItems i x-led
        productFlowPane.setVgap(40);                                                                                    //Avstånd mellan productItems i y-led
        createProductItems();                                                                                           //kalla på metod som skapar varorna
        cartPaneWrap.getChildren().add(shoppingCartPane);                                                               //Lägger till vår varukorg
        shoppingCartPane.createProductCartItems();  //För att ej få nullpointer, kan ej skapas innan productItems!
        wizard = new Wizard(this);
        wizardWrap.getChildren().add(wizard);

        updateFrontPage();

        //TODO Låg tidigare en allcategories.fire() här.

        amountBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                activeInDetailview.setAmount(Double.valueOf(amountBox.getText()));
                shoppingCartPane.addProductToCart(activeInDetailview);
                if (activeInDetailview.getAmount() < 1) {       //Ändra om vi vill ha double-system
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
                if (event.getDeltaX() != 0) {
                    event.consume();
                }
            }
        });
    }
}
