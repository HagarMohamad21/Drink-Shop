package app.sunshine.android.example.com.drinkshop.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient  {
    public static Retrofit client=null;
    public static Retrofit getClient(String Base_Url){
        if(client==null)
        {
            client= new Retrofit.Builder().
                    baseUrl(Base_Url).
                    addConverterFactory(GsonConverterFactory.create()).
                    addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                    build();
        }
        return client;
    }

}
