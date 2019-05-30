package sample;

import se.chalmers.cse.dat216.project.Product;

public interface FavoriteObserver {
    public void update(Product product, Boolean isFavorite);
    public Product getProduct();
}
