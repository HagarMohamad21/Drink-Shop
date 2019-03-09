package app.sunshine.android.example.com.drinkshop.Database.Local;

import java.util.List;

import app.sunshine.android.example.com.drinkshop.Database.DataSource.IFavouriteDataSource;
import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Favourite;
import io.reactivex.Flowable;

public class FavouriteDataSource implements IFavouriteDataSource {
private FavouriteDAO favouriteDAO;
private static FavouriteDataSource instance;

    public FavouriteDataSource(FavouriteDAO favouriteDAO) {
        this.favouriteDAO = favouriteDAO;
    }
public static FavouriteDataSource getInstance(FavouriteDAO favouriteDAO){
        if(instance==null){
            instance=new FavouriteDataSource(favouriteDAO);
        }
        return instance;
}
    @Override
    public Flowable<List<Favourite>> getFavItems() {
        return favouriteDAO.getFavItems();
    }

    @Override
    public int isFavourite(int itemId) {
        return favouriteDAO.isFavourite(itemId);
    }

    @Override
    public void deleteFavItem(Favourite favourite) {
        favouriteDAO.deleteFavItem(favourite);
    }

    @Override
    public void insertFavItem(Favourite... favourites) {
        favouriteDAO.insertFavItem(favourites);
    }
}
