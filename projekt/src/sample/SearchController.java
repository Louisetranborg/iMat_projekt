package sample;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
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
    private ImageView detailRemoveButtonInactive;
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


    public void greenAddButtonToFrontDetail() {
        addButtonGreenDetail.toFront();
    }

    public void blackAddButtonToFrontDetail() {
        addButtonDetail.toFront();
    }

    public void brownRemoveButtonToFrontDetail() {
        removeButtonBrown.toFront();
    }

    public void blackRemoveButtonToFrontDetail() {
        detailRemoveButtonInactive.toFront();
    }

    @FXML
    private ScrollPane categoryScrollPane;
    @FXML
    private ImageView logo;
    @FXML
    private ImageView addButtonGreenDetail;
    @FXML
    private ImageView addButtonGreenHoverDetail;
    @FXML
    private ImageView removeButtonBrown;
    @FXML
    private ImageView removeButtonHover;
    @FXML
    private ImageView closeDetailView;
    @FXML
    private ImageView helpIcon;
    @FXML
    private ImageView minSidaIcon;

    private ShoppingItem activeShoppingItemInDetailView;


    IMatDataHandler iMatDataHandler = IMatDataHandler.getInstance();                                                    //Vår iMatDataHandler

    protected Map<String, ProductItem> productItemMap = new HashMap<String, ProductItem>();                               //Map som fylls med productItems

    ShoppingCartPane shoppingCartPane = new ShoppingCartPane(iMatDataHandler.getShoppingCart(), this);                   //Detta är vår kundvagn

    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public ToggleGroup menuToggleGroup;
    protected Wizard wizard;

    private Product activeInDetailview;
    public MyPage myPage;

    static boolean isUserOnMyPage = false;

    protected ShoppingCartPane getShoppingCartPane() {
        return shoppingCartPane;
    }

    @FXML
    private void glow(){
        closeDetailView.setEffect(new Glow(0.5));
    }

    @FXML
    private void removeGlow(){
        closeDetailView.setEffect(new Glow(0));
    }


   /* protected void resetEveryShoppingItem() {
        for (ShoppingItem item : shoppingItemMap.values()) {
            item.setAmount(0);
            updateProductAmountInAllItems(item);
            productItemMap.get(item.getProduct().getName()).setBlackButton();
        }
    }*/

    //Sätter light-boxen längs fram för att visa mer info om en produkt
    protected void openProductDetailView(Product product) {
        populateProductDetailView(product);
        productDetailView.toFront();
        activeInDetailview = product;
        updateDetailViewItem();
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
    private void populateProductDetailView(Product product) {
        closeUpImage.setImage(iMatDataHandler.getFXImage(product));
        closeUpName.setText(product.getName());
        itemNumber.setText("Artikelnummer: " + String.valueOf(product.getProductId()));
        ecoInfo.setText(isEcological(product));
        priceDetailView.setText(product.getPrice() + " " + product.getUnit());
        amountBox.textProperty().setValue(String.valueOf(0)); //TODO Fix amount to be loaded from a shoppingCart
    }

    private String isEcological(Product product) {
        if (product.isEcological()) {
            return "Varan är ekologisk";
        } else {
            return "Varan är ej ekologisk";
        }

    }

    public void changeCategoryPageText(String string) {
        categoryPageText.setText(string);
    }


    //Fyller categoryFlowPane med alla kategorier för produkter
    private void fillCategoryPane() {
        menuVbox.getChildren().clear();
        for (CategoryItem.productCategory productCategory : CategoryItem.productCategory.values()) {
            MenuItem categoryItem = new CategoryItem(productCategory, this);
            menuVbox.getChildren().add(categoryItem);
            if (productCategory.name() == "ALL_CATEGORIES") {
                categoryItem.categoryButton.fire();
            }
        }
    }

    //Fyller categoryFlowPane med alla kategorier för settings
    private void fillSettingsPane() throws IOException {    //Fyller categoryPane med alla kategorierna
        menuVbox.getChildren().clear();
        for (SettingsItem.SettingsCategory settings : SettingsItem.SettingsCategory.values()) {
            MenuItem settingsItem = new SettingsItem(this, settings);
            menuVbox.getChildren().add(settingsItem);
            if (settings.name() == "HISTORY") {
                settingsItem.categoryButton.fire();
            }
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
            //TODO Raden under blev ändrad , Gabriel
            ProductItem productItem = new ProductItem(product, this);
            productItemMap.put(product.getName(), productItem);
            productFlowPane.getChildren().add(productItem);                                                             //Lägger ut alla varorna på framsidan, ändra om vi vill ha annan förstasida
        }
    }

    //Uppdaterar productFlowPane utifrån vilken kategori man väljer
    protected void updateProductPaneFromCategory(List<Product> products) {
        productFlowPane.getChildren().clear();
        productScrollPane.setVvalue(0);

        //TODO lägg in så att productscrollpane och frontpane automatiskt dyker upp .toFront. (eventuellt en activateproductFLowPane)
        frontPane.toFront();

        for (Product product : products) {
            productFlowPane.getChildren().add(productItemMap.get(product.getName()));

        }
    }

    protected void updatePersonalDataPage() {
        myPage.loadCustomerInfo();
    }

    protected void updateHistoryPage() {
        myPage.historyOrderVbox.getChildren().clear();
        for (Order order : IMatDataHandler.getInstance().getOrders()) {
            myPage.historyOrderVbox.getChildren().add(0,new OrderItem(this, order));
            //   productFlowPane.getChildren().add(productItemMap.get(product1.getName()));

        }
    }

    //List<ShoppingItem> tempOrderProducts = new ArrayList<>();
    Order currentlyShownOrder = null;

    protected void updateHistoryShowItems(Order order) {
        myPage.historyProductTilePane.getChildren().clear();
       // tempOrderProducts.clear();
        for (ShoppingItem item : order.getItems()) {
            //TODO Här behöver vi ändra om , Gabriel
            ProductItem pItem = new ProductItem(item.getProduct(), this);
            pItem.changeToHistoryLayout(item);
            myPage.historyProductTilePane.getChildren().add(pItem);

            //tempOrderProducts.add(item);

        }
        currentlyShownOrder = order;
        myPage.historyItems.toFront();
    }

    protected void addAllHistoryItemsToCart() {
        System.out.println(myPage.historyProductTilePane.getChildren() + " :  ");

        for (ShoppingItem item : currentlyShownOrder.getItems()){
            modifyAmountInCart(item.getProduct(),item.getAmount());
       /*     for (int i = 0 ; i < item.getAmount(); i++) {
                //TODO Här behöver vi ändra om , Gabriel
                addItemToCart(shoppingItemMap.get(item.getProduct().getName()));
            }*/

            //   productFlowPane.getChildren().add(productItemMap.get(product1.getName()));

        }
    }

    protected void updateFavoritePage() {
        myPage.historyTilePane.getChildren().clear();
        for (Product favorite : iMatDataHandler.favorites()) {
            myPage.historyTilePane.getChildren().add(productItemMap.get(favorite.getName()));
        }
    }

    //Updaterar startsidan och lägger sig längst fram i synlighetshierarkin.
    @FXML
    public void updateFrontPage() {
        productFlowPane.getChildren().clear();
        productScrollPane.setVvalue(0);

        //Används för att toggla texten i MyPage-knappen mellan "Min Sida" och "Handla"
        isUserOnMyPage = false;
        updateMyPageButton();

        //TODO eventuellt bestämma vart dessa ska samlas. Förslagsvis här då och andra metoder för kalla på denna. Här kanske man kör ihop activateShoppingview också.
        productScrollPane.setContent(productFlowPane);
        fillCategoryPane();

        wizardWrap.setVisible(false);

        for (Product product : iMatDataHandler.getProducts()) {
            productFlowPane.getChildren().add(productItemMap.get(product.getName()));                                                             //Lägger ut alla varorna på framsidan, ändra om vi vill ha annan förstasida
        }
        categoryPageText.setText("Startsida");
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

        // String url = "sample/css_files/css_images/person_black.png";
        if (isUserOnMyPage != true) {
            changeViewToMyPage();

        } else {
            updateFrontPage();
        }
        updateMyPageButton();
    }

    public void updateMyPageButton() {
        if (isUserOnMyPage == true) {
            minSidaButton.setText("Handla");
            minSidaIcon.setImage(new Image("sample/css_files/css_images/search_icon.png"));

        } else {
            minSidaButton.setText("Min Sida");
            minSidaIcon.setImage(new Image("sample/css_files/css_images/person_black.png"));
        }
    }

    //TODO eventuellt ha en update för varje stor sida. e.g updateFrontPane, updateMyPage, updateCheckout osv. och skrota alla små metoder som vi inte håller koll på.

    //Byter view från mypage till productflowpane
    protected void changeViewToMyPage() {
        productScrollPane.setContent(myPage);
        myPage.historyPage.toFront();
        changeCategoryPageText("Historik");
        isUserOnMyPage = true;

        try {
            fillSettingsPane();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Onödig atm, kanske kommer till användning senare.
   /* protected void updateProductPaneFromSettings() throws IOException{       //Uppdaterar productFlowPane utifrån kategorierna
        //settingsScrollPane.toBack();
        productScrollPane.setContent(myPage);
        fillCategoryPane();


    }*/

    //När man söker skall productFlowPane uppdateras efter sökningen

    public void setAmountInCart(Product p, double amount){
        ShoppingCart shoppingCart = iMatDataHandler.getShoppingCart();
        ShoppingItem shoppingItem = getShoppingItemOfProductInCart(p);
        if(shoppingItem == null){
            shoppingCart.addProduct(p, 0);
            shoppingItem = getShoppingItemOfProductInCart(p);
        }

        if(amount <= 0) {
            shoppingCart.removeItem(getShoppingItemOfProductInCart(p));
            //shoppingItem.setAmount(0);
        } else{
            shoppingItem.setAmount(amount);
        }
       // System.out.println(shoppingItem == null);
        updateProductAmountInAllItems(shoppingItem);
       // System.out.println("Product "+ p.toString() +" set to: " + amount);
    }

    //Uppdatera antal i shoppingitem. Change är förändringen i antal.
    public void modifyAmountInCart(Product p, double change) {      // Använd getShoppingItemOfProductInCart för att ändra antal på produkten}
        ShoppingItem shoppingItem = getShoppingItemOfProductInCart(p);

        double currentAmount;

        if(shoppingItem == null){
            currentAmount = 0;
        } else{
            currentAmount = shoppingItem.getAmount();
        }

        setAmountInCart(p, currentAmount + change);
    }

    //Returnerar shoppingItem, OBS returnerar null om varan ej finns i varukorgen.
    public ShoppingItem getShoppingItemOfProductInCart(Product p) {// Använd getItems för att hitta rätt shoppingitem och retunera den
        for (ShoppingItem shoppingItem : iMatDataHandler.getShoppingCart().getItems()){
            if (shoppingItem.getProduct().getProductId() == p.getProductId())
                return shoppingItem;
        }

        return null;
    }

    public double getAmountOfProductInCart(Product p) {// Använd getItems för att hitta rätt shoppingitem och retunera den
        ShoppingItem shoppingItem = getShoppingItemOfProductInCart(p);

        if(shoppingItem == null)
            return 0;

        return shoppingItem.getAmount();
    }


    //TODO Om man skriver bara 2 bokstväer(ibland) uppdateras inte categoryPageText. Exempel är "se", "ve" och "me".
    @FXML
    private void searchInSearchBox() {
        updateProductPaneFromString(searchBox.getCharacters().toString());
        categoryPageText.setText("Sökresultat för ''" + searchBox.getCharacters().toString() + "''");
        productScrollPane.setContent(productFlowPane);
        menuToggleGroup.selectToggle(null);
    }


    //TODO Ska man flytta denna metod till den listener i ShoppingCartPane som avfyras när varukorgen ändras? Gabriel
    protected void updateProductAmountInAllItems(ShoppingItem shoppingItem) {     //Uppdaterar amount både i produkterna i kundvagnen och produkterna i flowpane i mitten
        shoppingCartPane.updateCart();
        updateDetailViewItem();
        productItemMap.get(shoppingItem.getProduct().getName()).updateProductItem();
        shoppingCartPane.getProductCartItemMap().get(shoppingItem.getProduct().getName()).updateAmountInCartItem(shoppingItem);
        shoppingCartPane.getProductCartItemMap().get(shoppingItem.getProduct().getName()).getPrice().setText(decimalFormat.format(shoppingItem.getTotal()) + " kr");
        System.out.println("Updated All Items! - " + shoppingItem.getProduct().getName());
    }

  /*  protected void addItemToCart(Product product) {
        modifyAmountInCart(product, 1); //Ökar amount med ett
       // updateAllItems(shoppingItem);
    }

    protected void subtractItemFromCart(ShoppingItem shoppingItem) {
        if (shoppingItem.getAmount() > 0) {
            shoppingItem.setAmount(shoppingItem.getAmount() - 1);

        }

        if ((shoppingItem.getAmount() <= 0)) {
            shoppingItem.setAmount(0);
            shoppingCartPane.removeProductFromCart(shoppingItem);

        }

    //    updateAllItems(shoppingItem);
    }*/

    @FXML
    protected void clickedOnAddButton(Event event) {
        modifyAmountInCart(activeInDetailview,1);
        //updateDetailViewItem();
    }

    @FXML
    protected void hoverOnAddButton(Event event) {
        addButtonGreenHoverDetail.toFront();

    }

    @FXML
    protected void hoverOffAddButton(Event event) {
        addButtonGreenHoverDetail.toBack();

    }

    @FXML
    protected void hoverOnRemoveButton(Event event) {
        removeButtonHover.toFront();
    }

    @FXML
    protected void hoverOffRemoveButton(Event event) {
        removeButtonHover.toBack();
    }

    @FXML
    protected void clickedOnRemoveButton(Event event) {
        modifyAmountInCart(activeInDetailview, -1);
       // updateDetailViewItem();
    }

    private void updateDetailViewItem() {
        if(activeInDetailview != null) {
            updateButtons();
            amountBox.textProperty().setValue(String.valueOf(getAmountOfProductInCart(activeInDetailview)));
        }
    }


    private void updateButtons(){
        if (getAmountOfProductInCart(activeInDetailview) <= 0) {
            addButtonDetail.toFront();
            detailRemoveButtonInactive.toFront();
            removeButtonBrown.setVisible(false);
            removeButtonHover.setVisible(false);
        } else {
            addButtonGreenDetail.toFront();
            removeButtonBrown.setVisible(true);
            removeButtonHover.setVisible(true);
            removeButtonBrown.toFront();
        }
    }


    protected void wizardToFront() {
        wizardWrap.toFront();
        wizard.start();
        updateFirstCartInWizard();
        activateWizardView();
        wizard.setCheckoutInfoBasedOnPersonalInfo();
        wizard.resetErrorHandler();
    }


    //TODO Slå ihop wizardToFront() med activate wizardview. De åstadkommer samma sak. Kolla vilka ställen som använder metoden innan remove.
    private void activateWizardView() {
        logo.setDisable(true);
        searchBox.setVisible(false);
        minSidaButton.setVisible(false);
        helpIcon.setVisible(false);
        backToStoreIcon.setVisible(true);
        backToStoreIcon.setDisable(false);
        backToStoreIcon.opacityProperty().setValue(1);
        backToStoreLabel.setVisible(true);
        backToStoreLabel.setDisable(false);
        wizardWrap.setVisible(true);
    }

    protected void updateFirstCartInWizard() {
        wizard.getFirstCart().getChildren().clear();
        for (ShoppingItem shoppingItem : IMatDataHandler.getInstance().getShoppingCart().getItems()) {
            FirstCartItem firstCartItem = new FirstCartItem(shoppingItem, this);
            wizard.getFirstCart().getChildren().add(firstCartItem);
            firstCartItem.updateAmountBoxInFirstCartItem();
        }
        wizard.updateTotalPriceInStep1();
        if (iMatDataHandler.getShoppingCart().getItems().isEmpty()) {
            wizardToBack();
        }
    }

    protected void setBackToStoreIconDisabled() {
        backToStoreIcon.setDisable(true);
        backToStoreIcon.opacityProperty().setValue(0.5);
    }

    protected void setBackToStoreLabelDisabled() {
        backToStoreLabel.setDisable(true);
    }

    protected void setBackToStoreIconAble() {
        backToStoreIcon.setDisable(false);
        backToStoreIcon.opacityProperty().setValue(1);
    }

    protected void setBackToStoreLabelAble() {
        backToStoreLabel.setDisable(false);
    }

    @FXML
    protected void wizardToBack() {
        wizardWrap.toBack();
        activateShoppingView();
        updateFrontPage();
    }

    //TODO undersöka en eventuell ihopkoppling av activateshoppingview och updateFrontPage. Så att det finns en tydlig funktion för att byta "view" åt alla håll.
    //TODO Istället göra en closeWizard som anropas i wizard?
    protected void activateShoppingView() {
        searchBox.setVisible(true);
        minSidaButton.setVisible(true);
        helpIcon.setVisible(true);
        backToStoreLabel.setVisible(false);
        backToStoreIcon.setVisible(false);
        logo.setDisable(false);
    }



    private double extractDigits(String value){
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {
            if (Character.isDigit(value.charAt(i)) || String.valueOf(value.charAt(i)).equals(".")) {
                stringBuilder.append(value.charAt(i));
            }
        }

        if(!stringBuilder.toString().isEmpty()) {
            return Double.valueOf(stringBuilder.toString());
        } else {
            return 0;
        }

    }

    private double handleInput(String value){
        double output = extractDigits(value);
        String unitSuffix = activeInDetailview.getUnitSuffix();

        if(unitSuffix.equals("st") || unitSuffix.equals("förp") || unitSuffix.equals("burk")){
            output = Math.round(output);
        }

        if(output < 0.1){
            return 0;
        } else {
            return output;
        }
    }


    //Vår initialize-metod, typ som en kontruktor
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       /* iMatDataHandler.getCustomer().setFirstName("Hjördis");                                                          //Sätter namnet till Hjördis sålänge.
        iMatDataHandler.getCustomer().setLastName("Johansson");
        iMatDataHandler.getCustomer().setMobilePhoneNumber("073-333 33 33");
        iMatDataHandler.getCustomer().setEmail("hjördis.johansson@gmail.se");
        iMatDataHandler.getCustomer().setPostCode("333 33");
        iMatDataHandler.getCustomer().setAddress("Kallebäcksvägen 3");
        iMatDataHandler.getCustomer().setPhoneNumber("Göteborg");*/


        //iMatDataHandler.getShoppingCart().clear();


        menuToggleGroup = new ToggleGroup();


        productFlowPane.setHgap(40);                                                                                    //Avstånd mellan productItems i x-led
        productFlowPane.setVgap(40);
        //Avstånd mellan productItems i y-led
        myPage = new MyPage(this);
        createProductItems();                                                                                           //kalla på metod som skapar varorna
        cartPaneWrap.getChildren().add(shoppingCartPane);                                                               //Lägger till vår varukorg
        shoppingCartPane.createProductCartItems();  //För att ej få nullpointer, kan ej skapas innan productItems!

        wizard = new Wizard(this);
        wizardWrap.getChildren().add(wizard);
        updateHistoryPage();
        for(ShoppingItem shoppingItem: iMatDataHandler.getShoppingCart().getItems()){
            updateProductAmountInAllItems(shoppingItem);
        }
        if(iMatDataHandler.getShoppingCart().getItems().isEmpty()){
            shoppingCartPane.getToCheckoutButton().setDisable(true);
        } else{
            shoppingCartPane.getToCheckoutButton().setDisable(false);
        }
        updateFavoritePage();
        updateFrontPage();




        //TODO Låg tidigare en allcategories.fire() här.


        amountBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String input = amountBox.getText();
                double amount = handleInput(input);
                setAmountInCart(activeInDetailview, amount);
               // updateAllItems(activeInDetailview);
            }
        });


        amountBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    amountBox.clear();
                } else {
                    updateDetailViewItem();
                }
            }
        });

        categoryScrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
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
                if (event.getDeltaX() != 0) {
                    event.consume();
                }
            }
        });

    }

    //
    //
    //-----------------------Metoder som har med Favoriter att göra ----------------------------------------------------------------
    //

    List<FavoriteObserver> favoriteState = new ArrayList();

    public void addObservers(FavoriteObserver fo) {
        favoriteState.add(fo);
    }

    public void removeObservers(FavoriteObserver fo) {
        favoriteState.remove(fo);
    }

    public void updateFavoriteItems(Product product, Boolean isFavorite) {
        for (FavoriteObserver fo : favoriteState) {
            if (fo.getProduct() == product)
                fo.update(product, isFavorite);
        }
    }

    protected void addFavorite(Product p) {
        iMatDataHandler.addFavorite(p);
        updateFavoriteItems(p, true);
    }

    protected void addAllToFavourite(Product p) {
    }

    protected void removeFavorite(Product p) {
        iMatDataHandler.removeFavorite(p);
        updateFavoriteItems(p, false);
    }

}
