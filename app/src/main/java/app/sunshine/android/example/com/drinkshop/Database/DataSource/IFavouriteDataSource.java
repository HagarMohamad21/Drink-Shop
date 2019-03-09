package app.sunshine.android.example.com.drinkshop.Database.DataSource;

import java.util.List;
import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Favourite;
import io.reactivex.Flowable;

public interface IFavouriteDataSource {

    Flowable<List<Favourite>> getFavItems();

    int isFavourite(int itemId);

    void deleteFavItem(Favourite favourite);
    void insertFavItem(Favourite...favourites);
}
