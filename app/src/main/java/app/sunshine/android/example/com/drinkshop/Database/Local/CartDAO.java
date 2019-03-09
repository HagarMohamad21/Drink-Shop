package app.sunshine.android.example.com.drinkshop.Database.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Cart;
import io.reactivex.Flowable;
@Dao
public interface CartDAO {

    @Query("Select * From Cart")
    Flowable<List<Cart>> getCartItems();

    @Query("Select * From Cart Where id=:CartItemId")
    Flowable<List<Cart>> getCartItemById(int CartItemId);

    @Query("Select COUNT(*) From Cart")
    int getCartItemsCount();

    @Query("Select SUM(price) From Cart")
    float sumPrice();

    @Query("Delete From Cart")
    void emptyCart();

    @Insert
    void InsertToCart(Cart...cart);

    @Update
    void UpdateCartItem(Cart...carts);

    @Delete
    void DeletCartItem(Cart...carts);
}
