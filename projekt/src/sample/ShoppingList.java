package sample;

import se.chalmers.cse.dat216.project.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Används ej för tillfället. Denna klassen används för sparade inköpslistor. Om man ska implementera bör denna klassen loada en fxml-fil som visar upp samtliga produkter eller något
 */

public class ShoppingList {

    private List<ShoppingItem> savedShoppingItems = new ArrayList<>();
    private String name;
    private double total;
    private int itemAmount;         //antalet olika varor. Annars om man lägger till 2kg melon räknas detta som 2 produkter.

    public ShoppingList(List<ShoppingItem> savedShoppingItems, String name){
        this.savedShoppingItems = savedShoppingItems;
        this.name = name;
        total = calculateTotal();
        itemAmount = savedShoppingItems.size();
    }


    public double calculateTotal(){
        double sum = 0;
        if(savedShoppingItems.size() > 0){
            for(ShoppingItem item: savedShoppingItems){
                sum = sum + item.getTotal();
            }
        }
        return sum;
    }

    protected List<ShoppingItem> getSavedShoppingItems(){
        return savedShoppingItems;
    }

}
