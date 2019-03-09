package app.sunshine.android.example.com.drinkshop.Database.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Favourite;
import io.reactivex.Flowable;
@Dao
public interface FavouriteDAO {
    @Query("Select * From Favourite")
    Flowable<List<Favourite>>getFavItems();
    @Query("Select Exists (Select 1 From Favourite Where id=:itemId)")
    int isFavourite(int itemId);
    @Delete
    void deleteFavItem(Favourite favourite);
    @Insert
    void insertFavItem(Favourite...favourites);
}
