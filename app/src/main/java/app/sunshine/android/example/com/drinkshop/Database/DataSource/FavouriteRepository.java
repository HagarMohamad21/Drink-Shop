package app.sunshine.android.example.com.drinkshop.Database.DataSource;

import java.util.List;

import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Favourite;
import io.reactivex.Flowable;


public class FavouriteRepository implements IFavouriteDataSource {
    private IFavouriteDataSource favouriteDataSource;
    private static FavouriteRepository instance;

    public FavouriteRepository(IFavouriteDataSource favouriteDataSource) {
        this.favouriteDataSource = favouriteDataSource;
    }
public static FavouriteRepository getInstance(IFavouriteDataSource favouriteDataSource){
        if(instance==null)
            instance=new FavouriteRepository(favouriteDataSource);
        return instance;
}
    @Override
    public Flowable<List<Favourite>> getFavItems() {
        return favouriteDataSource.getFavItems();
    }

    @Override
    public int isFavourite(int itemId) {
        return favouriteDataSource.isFavourite(itemId);
    }

    @Override
    public void deleteFavItem(Favourite favourite) {
favouriteDataSource.deleteFavItem(favourite);
    }

    @Override
    public void insertFavItem(Favourite... favourites) {
        favouriteDataSource.insertFavItem(favourites);
    }
}
