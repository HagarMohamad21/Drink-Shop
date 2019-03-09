package app.sunshine.android.example.com.drinkshop.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitScalarsClient {
    public static Retrofit client=null;
    public static Retrofit  getScalarsClient(String Base_Url){
        if(client==null)
        {
            client= new Retrofit.Builder().
                    baseUrl(Base_Url).
                    addConverterFactory(ScalarsConverterFactory.create()).
                    build();
        }
        return client;
    }
}
