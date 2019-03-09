package app.sunshine.android.example.com.drinkshop.Database.Local;

import java.util.List;

import app.sunshine.android.example.com.drinkshop.Database.DataSource.ICartDataSource;
import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Cart;
import io.reactivex.Flowable;

public class CartDataSource implements ICartDataSource {
   private static CartDataSource instance;

    public  CartDAO cartDAO;

    public CartDataSource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }
    public static CartDataSource getInstance(CartDAO cartDAO){
        if(instance==null){
            instance=new CartDataSource(cartDAO);
        }
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItems() {

        return cartDAO.getCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int CartItemId) {
        return cartDAO.getCartItemById(CartItemId);
    }

    @Override
    public int getCartItemsCount() {
        return cartDAO.getCartItemsCount();
    }

    @Override
    public float sumPrices() {
        return cartDAO.sumPrice();
    }

    @Override
    public void emptyCart() {
        cartDAO.emptyCart();
    }

    @Override
    public void InsertToCart(Cart... cart) {
cartDAO.InsertToCart(cart);
    }

    @Override
    public void UpdateCartItem(Cart... carts) {
cartDAO.UpdateCartItem(carts);
    }

    @Override
    public void DeletCartItem(Cart... carts) {
            cartDAO.DeletCartItem(carts);
    }
}
