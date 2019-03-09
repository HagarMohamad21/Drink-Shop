package app.sunshine.android.example.com.drinkshop.Utils;

import java.util.ArrayList;
import java.util.List;

import app.sunshine.android.example.com.drinkshop.Database.DataSource.CartRepository;
import app.sunshine.android.example.com.drinkshop.Database.DataSource.FavouriteRepository;
import app.sunshine.android.example.com.drinkshop.Database.Local.ShopDataBase;
import app.sunshine.android.example.com.drinkshop.Model.Category;
import app.sunshine.android.example.com.drinkshop.Model.Drink;
import app.sunshine.android.example.com.drinkshop.Model.Order;
import app.sunshine.android.example.com.drinkshop.Model.User;
import app.sunshine.android.example.com.drinkshop.Retrofit.FCMClient;
import app.sunshine.android.example.com.drinkshop.Retrofit.IDrinkShop;
import app.sunshine.android.example.com.drinkshop.Retrofit.IFCMService;
import app.sunshine.android.example.com.drinkshop.Retrofit.RetrofitClient;
import app.sunshine.android.example.com.drinkshop.Retrofit.RetrofitScalarsClient;

public class Common {
    //local host 10.0.2.2
    //genymotion 10.0.3.2
  //  http://localhost/drinkShop/
    public static final String Base_Url="http://10.0.2.2/drinkShop/";
    public static final String Access_Token="http://10.0.2.2/drinkShop/braintree/main.php";
    public static final String LocalHost="http://localhost/drinkShop/user_avatar/";
    public static final String FIREBASE_API="https://fcm.googleapis.com/";
public static IFCMService getIFCMService(){
    return FCMClient.getClient(FIREBASE_API).create(IFCMService.class);
}
    public static User currentUser=null;
    public static int ice=-1;
    public static int cupSize=-1;
    public static int sugar=-1;
    public static final String MenuId="7";
    public static List<String> toppingAdded =new ArrayList<>();
    public static List<Drink>toppingList=new ArrayList<>();
    public static double toppingTotalPrice=0.0;
    public static Category currentSelectedCategory=null;
    public static Order currentOrder=null;
    public static IDrinkShop getAPI(){
        return RetrofitClient.getClient(Base_Url).create(IDrinkShop.class);
    }
    public static IDrinkShop getScalarsAPI(){
        return RetrofitScalarsClient.getScalarsClient(Base_Url).create(IDrinkShop.class);
    }

    //DataBase
    public static ShopDataBase shopDataBase;
    public static CartRepository cartRepository;
    public static FavouriteRepository favouriteRepository;

}
