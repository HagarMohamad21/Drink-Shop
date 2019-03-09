package app.sunshine.android.example.com.drinkshop.Database.DataSource;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Cart;
import io.reactivex.Flowable;

public interface ICartDataSource {
    Flowable<List<Cart>> getCartItems();

    Flowable<List<Cart>> getCartItemById(int CartItemId);


    int getCartItemsCount();

    float sumPrices();

    void emptyCart();


    void InsertToCart(Cart...cart);


    void UpdateCartItem(Cart...carts);


    void DeletCartItem(Cart...carts);
}
