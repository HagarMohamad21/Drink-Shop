package app.sunshine.android.example.com.drinkshop.Retrofit;

import android.database.Observable;

import java.util.List;

import app.sunshine.android.example.com.drinkshop.Model.Banner;
import app.sunshine.android.example.com.drinkshop.Model.Category;
import app.sunshine.android.example.com.drinkshop.Model.CheckUserResponse;
import app.sunshine.android.example.com.drinkshop.Model.Drink;
import app.sunshine.android.example.com.drinkshop.Model.Order;
import app.sunshine.android.example.com.drinkshop.Model.OrderResult;
import app.sunshine.android.example.com.drinkshop.Model.Store;
import app.sunshine.android.example.com.drinkshop.Model.Token;
import app.sunshine.android.example.com.drinkshop.Model.User;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IDrinkShop {
    @FormUrlEncoded
    @POST("checkuser.php")
    Call<CheckUserResponse> checkUserExitance(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("registeruser.php")
    Call<User> RegisterNewUser(@Field("phone") String phone,
                               @Field("name") String name,
                               @Field("birthdate") String birthdate,
                               @Field("address") String address);

    @FormUrlEncoded
    @POST("getUerInfo.php")
    Call<User> getUserInformation(@Field("phone") String phone);
    @FormUrlEncoded
    @POST("insertOrder.php")
    Call<Order> insertNewOrder(@Field("orderPrice") float price
            , @Field("orderDetail")String orderDetail
            , @Field("orderComment")String orderComment ,
                                     @Field("orderAddress")String orderAddress
            , @Field("phone")String phone
    );

    @GET("getBanner.php")
    io.reactivex.Observable<List<Banner>> getBanners();

    @GET("getMenu.php")
    io.reactivex.Observable<List<Category>> getMenu();

    @GET("getAllDrinks.php")
    io.reactivex.Observable<List<Drink>> getAllDrinks();

    @FormUrlEncoded
    @POST("getDrink.php")
    io.reactivex.Observable<List<Drink>> getDrink(@Field("menuId") String menuId);

    @FormUrlEncoded
    @POST("braintree/checkout.php")
     Call<String> sendPaymnets(@Field("nonce") String nonce,@Field("amount")String amount);

      @Multipart
     @POST("upload.php")
    Call<String> updateAvata(@Part MultipartBody.Part phone, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("getOrders.php")
    io.reactivex.Observable<List<Order>> getOrders(@Field("user_phone") String userPhone,
                                                   @Field("order_status")String orderStaus);

    @FormUrlEncoded
    @POST("getToken.php")
    Call<String>
    getToken(@Field("user_phone")String phone,
             @Field("token")String token,
             @Field("is_server_token")String isServerToken);

    @FormUrlEncoded
    @POST("getNearbyStores.php")
    io.reactivex.Observable<List<Store>>getNearbyStores(@Field("lat")String Lat,
                                     @Field("lng")String Lng);
    @FormUrlEncoded
    @POST("CancelOrder.php")
    Call<String> cancelOrder(@Field("user_phone")String user_phone,@Field("order_id")
            String order_id,@Field("order_status")String order_status);

    @FormUrlEncoded
    @POST("getTokenFromServer.php")
    Call<Token>
    getTokenFromServer(@Field("user_phone")String phone,
             @Field("is_server_token")String isServerToken);
}
