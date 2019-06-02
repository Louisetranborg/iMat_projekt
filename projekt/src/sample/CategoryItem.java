package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.util.ArrayList;
import java.util.List;

public class CategoryItem extends MenuItem {

    public enum productCategory{
        ALL_CATEGORIES,
        MEAT,
        DAIRY,
        FRUIT,
        GREENS,
        BREAD,
        PANTRY,
        DRINKS,
        SWEET
    }

    private productCategory productCategory;
    private List<Product> productsInCategory;

    public CategoryItem(productCategory productCategory, SearchController parentController){
        super(parentController);

        this.productCategory = productCategory;
        productsInCategory = getProductsOfCategory(productCategory);

    }

    private List<Product> getProductsOfCategory(productCategory cat){
        List<Product> list = new ArrayList<>();
        switch (cat){
            case MEAT:
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.MEAT));
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.FISH));
                categoryButton.setText("Kött, fisk och fågel");
                break;
            case BREAD:
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.BREAD));
                categoryButton.setText("Bröd");
                break;
            case DAIRY:
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.DAIRIES));
                categoryButton.setText("Mejeri");
                break;
            case FRUIT:
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.CITRUS_FRUIT));
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.EXOTIC_FRUIT));
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.BERRY));
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.MELONS));
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.FRUIT));
                categoryButton.setText("Frukt");
                break;
            case SWEET:
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.SWEET));
                categoryButton.setText("Sötsaker");
                break;
            case DRINKS:
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.HOT_DRINKS));
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.COLD_DRINKS));
                categoryButton.setText("Drycker");
                break;
            case GREENS:
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.HERB));
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.VEGETABLE_FRUIT));
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.CABBAGE));
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.ROOT_VEGETABLE));
                categoryButton.setText("Grönsaker");
                break;
            case PANTRY:
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.POD));
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.FLOUR_SUGAR_SALT));
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.NUTS_AND_SEEDS));
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.PASTA));
                list.addAll(parentController.iMatDataHandler.getProducts(ProductCategory.POTATO_RICE));
                categoryButton.setText("Skafferi");
                break;
            case ALL_CATEGORIES:
                list.addAll(parentController.iMatDataHandler.getProducts());
                categoryButton.setText("Startsida");
                break;
        }

        return list;
    }

    @FXML
    protected void onClick(Event event){        //När man klickar på en kategori skall varorna i mitten uppdateras
        parentController.updateProductPaneFromCategory(productsInCategory);
        parentController.changeCategoryPageText(categoryButton.getText());
    }


}
