package app.sunshine.android.example.com.drinkshop.Activities;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import app.sunshine.android.example.com.drinkshop.Model.CheckUserResponse;
import app.sunshine.android.example.com.drinkshop.Model.User;
import app.sunshine.android.example.com.drinkshop.R;
import app.sunshine.android.example.com.drinkshop.Retrofit.IDrinkShop;
import app.sunshine.android.example.com.drinkshop.Utils.Common;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE =1000 ;
    @BindView(R.id.continueBtn)
    Button continueBtn;
    IDrinkShop mService;
    private final int PERMISSION_REQUEST=1001;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //  set the default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mService=Common.getAPI();
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                    ,Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST);
            Log.d(TAG, "onCreate: ");
        }

        ///Auto Login
        if(AccountKit.getCurrentAccessToken()!=null){
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    Log.d(TAG, "onSuccess: ");
                    mService.checkUserExitance(account.getPhoneNumber().toString()).enqueue(new Callback<CheckUserResponse>() {
                        @Override
                        public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                            CheckUserResponse userResponse=response.body();
                            Log.d(TAG, "onResponse:  mService.checkUserExitance "+userResponse.isExists()+" "+account.getPhoneNumber().toString());
                            if(userResponse.isExists()){
                                Log.d(TAG, "onResponse: user already exist");
                                //user already in the system
                                mService.getUserInformation(account.getPhoneNumber().toString()).enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        Common.currentUser=response.body();
                                        // updateToken
                                        updateFirebaseToken();
                                        Log.d(TAG, "onResponse:  Common.currentUser   "+response.body());
                                       // waitingDialog.dismiss();
                                        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Log.d(TAG, "onFailure: Failed to get user info "+t.getMessage());
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckUserResponse> call, Throwable t) {

                        }
                    });

                }

                @Override
                public void onError(AccountKitError accountKitError) {

                }
            });
        }


    }

    //print the hash key for facebook account kit..
    private void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("app.sunshine.android.example.com.drinkshop",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest digest = MessageDigest.getInstance("SHA");
                digest.update(signature.toByteArray());
                Log.d(TAG, "printHashKey: " + Base64.encodeToString(digest.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.continueBtn)
    public void onViewClicked() {
     openLoginActivity(LoginType.PHONE);
    }

    private void openLoginActivity(LoginType loginType) {
        AccountKitConfiguration.AccountKitConfigurationBuilder builder=
                new AccountKitConfiguration.AccountKitConfigurationBuilder(loginType,AccountKitActivity.ResponseType.TOKEN);
        Intent intent=new Intent(this,AccountKitActivity.class);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,builder.build());
        startActivityForResult(intent,REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE){
            AccountKitLoginResult result=data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if(result.getError()!=null){
                Toast.makeText(this, "Error happened"+result.getError().toString(), Toast.LENGTH_SHORT).show();
            }
            else if(result.wasCancelled()){
                Toast.makeText(this, "Process was cancelled"+result.getError().toString(), Toast.LENGTH_SHORT).show();

            }
            else if(result.getAccessToken()!=null){
                Log.d(TAG, "onActivityResult: access token isn't null  "+result.getAccessToken().getToken());
               final AlertDialog waitingDialog=new SpotsDialog.Builder().setContext(MainActivity.this).build();
                waitingDialog.setMessage("Please Wait");
                waitingDialog.setCancelable(false);
                waitingDialog.show();
                Log.d(TAG, "onActivityResult: getCurrentAccount "+AccountKit.getCurrentAccessToken());
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {

                    @Override
                    public void onSuccess(final Account account) {
                        Log.d(TAG, "onSuccess: ");
                       mService.checkUserExitance(account.getPhoneNumber().toString()).enqueue(new Callback<CheckUserResponse>() {
                           @Override
                           public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                               CheckUserResponse userResponse=response.body();
                               Log.d(TAG, "onResponse:  mService.checkUserExitance "+userResponse.isExists()+" "+account.getPhoneNumber().toString());
                               if(userResponse.isExists()){
                                   Log.d(TAG, "onResponse: user already exist");
                                   //user already in the system
                                   mService.getUserInformation(account.getPhoneNumber().toString()).enqueue(new Callback<User>() {
                                       @Override
                                       public void onResponse(Call<User> call, Response<User> response) {
                                           Common.currentUser=response.body();
                                           waitingDialog.dismiss();
                                           Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                                           startActivity(intent);
                                           finish();
                                       }

                                       @Override
                                       public void onFailure(Call<User> call, Throwable t) {

                                       }
                                   });
                               }
                               else{
                                   // register new user
                                   Log.d(TAG, "onResponse: we are creating new user");
                                   ShowRegisterDialog(account.getPhoneNumber().toString());
                                   waitingDialog.dismiss();
                               }
                           }

                           @Override
                           public void onFailure(Call<CheckUserResponse> call, Throwable t) {

                           }
                       });

                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {
                        Log.d(TAG, "onError: "+accountKitError.getErrorType());
                    }
                });

            }
        }
    }

    private void ShowRegisterDialog(final String phone) {
        Log.d(TAG, "ShowRegisterDialog: ");
        final AlertDialog.Builder registerDialog=new AlertDialog.Builder(MainActivity.this);
        View RegisterLayout=LayoutInflater.from(this).inflate(R.layout.layout_register,null,false);
       final MaterialEditText NameEdit=RegisterLayout.findViewById(R.id.edit_name);
       final MaterialEditText AddressEdit=RegisterLayout.findViewById(R.id.edit_address);
       final MaterialEditText BirthdateEdit=RegisterLayout.findViewById(R.id.edit_birthdate);
       BirthdateEdit.addTextChangedListener(new PatternedTextWatcher("####-##-##"));
        registerDialog.setView(RegisterLayout);
        registerDialog.setCancelable(false);

        registerDialog.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(NameEdit.getText().toString())){
                    ShakeUI(NameEdit);
                   return;
                }else if(TextUtils.isEmpty(AddressEdit.getText().toString())){
                    ShakeUI(AddressEdit);
                    return;
                }else if(TextUtils.isEmpty(BirthdateEdit.getText().toString())){
                    ShakeUI(BirthdateEdit);
                   return;
                }
                final AlertDialog waitingDialog=new SpotsDialog.Builder().setContext(MainActivity.this).build();
                waitingDialog.setMessage("Please Wait");
                waitingDialog.setCancelable(false);
                waitingDialog.show();
                    //need to register new user
                    mService.RegisterNewUser(phone,NameEdit.getText().toString()
                                              ,BirthdateEdit.getText().toString(),
                                                AddressEdit.getText().toString()).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Log.d(TAG, "onResponse: "+response.body());
                            waitingDialog.dismiss();
                            Common.currentUser=response.body();
                            updateFirebaseToken();
                            User user=response.body();
                            if(TextUtils.isEmpty(user.getError_message())){
                                Log.d(TAG, "onResponse: No error  found");
                                Toast.makeText(MainActivity.this, "User Registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            waitingDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Registeration Failed "+t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


            }
        });

        registerDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        registerDialog.show();}


    public void ShakeUI(View view){
        YoYo.with(Techniques.Shake)
                .duration(700)
                .repeat(5)
                .playOn(findViewById(view.getId()));
    }

    private void updateFirebaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                IDrinkShop mService=Common.getAPI();
                mService.getToken(Common.currentUser.getPhone()
                        ,instanceIdResult.getToken(),"0").enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d(TAG, "onResponse: "+response);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d(TAG, "onFailure:Failed to get token "+t.getMessage());

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
