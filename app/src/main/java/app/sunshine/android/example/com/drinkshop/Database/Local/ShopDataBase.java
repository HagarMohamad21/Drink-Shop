package app.sunshine.android.example.com.drinkshop.Database.Local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Cart;
import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Favourite;

@Database(entities = {Cart.class,Favourite.class},version = 1)
public abstract class ShopDataBase extends RoomDatabase {
    private static ShopDataBase instance;
    public abstract CartDAO cartDAO();
    public abstract FavouriteDAO favouriteDAO();
    public static ShopDataBase getInstance(Context context){
      if(instance==null)  {
          instance=Room.databaseBuilder(context,ShopDataBase.class,"HAGAR_DrinkShopDb")
                  .allowMainThreadQueries()
                  .build();
      }
    return instance;
    }
}
