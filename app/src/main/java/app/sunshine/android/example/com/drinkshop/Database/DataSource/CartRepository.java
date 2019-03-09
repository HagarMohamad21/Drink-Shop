package app.sunshine.android.example.com.drinkshop.Database.DataSource;

import java.util.List;

import app.sunshine.android.example.com.drinkshop.Database.Local.CartDataSource;
import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Cart;
import io.reactivex.Flowable;

public class CartRepository implements ICartDataSource {
    ICartDataSource cartDataSource;
    private static CartRepository instance;

    public CartRepository(ICartDataSource cartDataSource) {
        this.cartDataSource = cartDataSource;
    }
public static CartRepository getInstance(CartDataSource cartDataSource){
        if(instance==null){
            instance=new CartRepository(cartDataSource);
        }
        return instance;
}
    @Override
    public Flowable<List<Cart>> getCartItems() {
        return cartDataSource.getCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int CartItemId) {
        return cartDataSource.getCartItemById(CartItemId);
    }

    @Override
    public int getCartItemsCount() {
        return cartDataSource.getCartItemsCount();
    }

    @Override
    public float sumPrices() {
        return cartDataSource.sumPrices();
    }

    @Override
    public void emptyCart() {
cartDataSource.emptyCart();
    }

    @Override
    public void InsertToCart(Cart... cart) {
cartDataSource.InsertToCart(cart);
    }

    @Override
    public void UpdateCartItem(Cart... carts) {
cartDataSource.UpdateCartItem(carts);
    }

    @Override
    public void DeletCartItem(Cart... carts) {
cartDataSource.DeletCartItem(carts);
    }
}
