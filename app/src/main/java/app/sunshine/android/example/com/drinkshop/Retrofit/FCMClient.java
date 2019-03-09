package app.sunshine.android.example.com.drinkshop.Retrofit;

import app.sunshine.android.example.com.drinkshop.Utils.Common;;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FCMClient {
   private static Retrofit retrofit=null;

    public static Retrofit getClient(String baseUrl){

        if(retrofit==null){
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor())
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Common.FIREBASE_API)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
