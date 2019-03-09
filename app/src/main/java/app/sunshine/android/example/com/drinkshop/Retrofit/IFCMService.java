package app.sunshine.android.example.com.drinkshop.Retrofit;

import app.sunshine.android.example.com.drinkshop.Model.DataMessage;
import app.sunshine.android.example.com.drinkshop.Model.MyResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAtz1akFA:APA91bH3k6Kb-atYdB0FM0s-gE1xzPnd4MuJ4t3If7x9t6IUArNjOSH2QMzLusZmbr_I_YBmfRG-mOba75-gBdx_p2jJ_0kiPQAaT4CQbdYJHl5OaTFVAygMCfWNjyQcLWknfIMSgUa_"})
    @POST("fcm/send/")
    Call<MyResponse> sendNotification(@Body DataMessage body);
}
