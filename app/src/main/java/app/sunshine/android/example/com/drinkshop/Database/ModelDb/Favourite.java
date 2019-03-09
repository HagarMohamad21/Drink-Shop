package app.sunshine.android.example.com.drinkshop.Database.ModelDb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Favourite")
public class Favourite {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    public String id;
    @ColumnInfo(name = "name")
    public String name;
     @ColumnInfo(name = "menuId")
     public String menuId;
    @ColumnInfo(name = "link")
    public  String link;

    @ColumnInfo(name = "price")
    public  String price;

}
