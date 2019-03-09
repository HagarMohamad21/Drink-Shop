package app.sunshine.android.example.com.drinkshop.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;

import com.loopj.android.http.TextHttpResponseHandler;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Cart;
import app.sunshine.android.example.com.drinkshop.Model.DataMessage;
import app.sunshine.android.example.com.drinkshop.Model.MyResponse;
import app.sunshine.android.example.com.drinkshop.Model.Order;
import app.sunshine.android.example.com.drinkshop.Model.OrderResult;
import app.sunshine.android.example.com.drinkshop.Model.Token;
import app.sunshine.android.example.com.drinkshop.Retrofit.IDrinkShop;
import app.sunshine.android.example.com.drinkshop.Retrofit.IFCMService;
import app.sunshine.android.example.com.drinkshop.Utils.CartAdapter;
import app.sunshine.android.example.com.drinkshop.Utils.Common;
import app.sunshine.android.example.com.drinkshop.Utils.RecyclerItemTouchHelper;
import app.sunshine.android.example.com.drinkshop.Interfaces.RecyclerItemTouchListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchListener {
    private static final String TAG = "CartActivity";
    private static final int PAYMENT_REQUST =7777 ;
    CompositeDisposable disposable = new CompositeDisposable();
    @BindView(app.sunshine.android.example.com.drinkshop.R.id.cartList)
    RecyclerView cartList;
    @BindView(app.sunshine.android.example.com.drinkshop.R.id.placeOrderBtn)
    Button placeOrderBtn;
    @BindView(app.sunshine.android.example.com.drinkshop.R.id.root)
    RelativeLayout root;
    List<Cart> localCarts = new ArrayList<>();
    CartAdapter adapter;
    MaterialEditText CommentEditText;
    RadioButton otherAddress;
    MaterialEditText otherAddressEditText;
    RadioButton userAddress;
String token,amount,orderComment,orderAddress;;
HashMap<String,String> params;
IDrinkShop mScalarSevice;
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.sunshine.android.example.com.drinkshop.R.layout.activity_cart);
        ButterKnife.bind(this);
        getCartList();
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(cartList);
        loadToken();
        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder();
            }
        });
    }

    private void loadToken() {
        final android.app.AlertDialog spots=new SpotsDialog.Builder().setContext(this).build();
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(Common.Access_Token, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
               spots.dismiss();
               placeOrderBtn.setEnabled(true);
               token=responseBody;
                Log.d(TAG, "onSuccess: token is ::::::::  "+token);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
            spots.dismiss();
                Toast.makeText(CartActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                placeOrderBtn.setEnabled(false);
            }
        });
    }

    private void placeOrder() {
        //create dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View placeOrderLayout = LayoutInflater.from(this).inflate(app.sunshine.android.example.com.drinkshop.R.layout.place_order_layout, null, false);
        CommentEditText=placeOrderLayout.findViewById(app.sunshine.android.example.com.drinkshop.R.id.CommentEditText);
        otherAddress=placeOrderLayout.findViewById(app.sunshine.android.example.com.drinkshop.R.id.other_address);
        userAddress=placeOrderLayout.findViewById(app.sunshine.android.example.com.drinkshop.R.id.userAddress);
        otherAddressEditText=placeOrderLayout.findViewById(app.sunshine.android.example.com.drinkshop.R.id.other_addressEditText);
        otherAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked)
                {
                    otherAddressEditText.setEnabled(true);
            }
            }
        });
        userAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    otherAddressEditText.setEnabled(false);
                }
            }
        });
      builder.setView(placeOrderLayout);
      builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
              dialogInterface.dismiss();
          }
      });
      builder.setPositiveButton("ORDER", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {

              if(userAddress.isChecked())
              orderAddress=Common.currentUser.getAddress();
              else
              orderAddress=otherAddressEditText.getText().toString();
              orderComment=CommentEditText.getText().toString();
              //payment
            DropInRequest request=new DropInRequest().clientToken(token);
            startActivityForResult(request.getIntent(CartActivity.this),PAYMENT_REQUST);


          }
      });
      builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PAYMENT_REQUST){
            Log.d(TAG, "onActivityResult: payment requseted");

                Log.d(TAG, "onActivityResult: result is okay ");
//                DropInResult dropInResult=data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
//               PaymentMethodNonce nonce= dropInResult.getPaymentMethodNonce();
//               String strNonce=nonce.getNonce();
               if(Common.cartRepository.sumPrices()>0){
                   params=new HashMap<>();
                   amount=String.valueOf(Common.cartRepository.sumPrices());
//                   params.put("nonce",strNonce);
//                   params.put("amount",amount);
                   //now send the payment
                   sendPayment();

               }
               else Toast.makeText(this, "You have nothing to buy", Toast.LENGTH_SHORT).show();
            }
            else if(requestCode==RESULT_CANCELED){
                Toast.makeText(this, "Transaction was cancelled", Toast.LENGTH_SHORT).show();
            }
            else {
                Log.d(TAG, "onActivityResult: "+data.getSerializableExtra(DropInActivity.EXTRA_ERROR));
            }
        }


    private void sendPayment() {
      mScalarSevice=Common.getScalarsAPI();
      mScalarSevice.sendPaymnets(params.get("nonce"),params.get("amount")).enqueue(new Callback<String>() {
          @Override
          public void onResponse(Call<String> call, Response<String> response) {
              Log.d(TAG, "onResponse: payment sent");



                  //now submit the order
                  disposable.add(Common.cartRepository.getCartItems().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Cart>>() {
                      @Override
                      public void accept(List<Cart> carts) throws Exception {
                          if(!TextUtils.isEmpty(orderAddress))
                              SendOrder(orderAddress,carts,orderComment,Common.cartRepository.sumPrices());
                          else Toast.makeText(CartActivity.this, "order address can't be empty'\n'Please enter your Address", Toast.LENGTH_SHORT).show();
                      }
                  }));



          }

          @Override
          public void onFailure(Call<String> call, Throwable t) {
              Log.d(TAG, "onFailure: "+t.getMessage());
          }
      });
    }

    private void SendOrder(String orderAddress, List<Cart>carts, String orderComment, float prices) {
        IDrinkShop mService=Common.getAPI();
        String orderDetails;
        if(carts.size()>0){
            orderDetails=new Gson().toJson(carts);

        mService.insertNewOrder(prices,orderDetails
                ,orderComment,orderAddress,Common.currentUser.getPhone()).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {

                if(response.isSuccessful()){
                    sendNotification(response.body());
                    Common.cartRepository.emptyCart();}
                    else {
                    Toast.makeText(CartActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.d(TAG, "onFailure: something went wrong"+t.getMessage());
            }
        });}
    }

    private void sendNotification(final Order orderResult) {

        //get server token
        IDrinkShop mService=Common.getAPI();
        mService.getTokenFromServer("CaffinaShop",
                "1").enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

                //now we have the token
                Map<String,String>contentSend=new HashMap<>();
                contentSend.put("title","Caffina");
                contentSend.put("message","You Have new Order with order ID :" +
                        " "+orderResult.getOrderID());
                DataMessage dataMessage=new DataMessage();
                Log.d(TAG, "onResponse: TOKEN IS"+response.body().getToken());
                if(response.body().getToken()!=null){
                    dataMessage.setTo(response.body().getToken());
               }
                dataMessage.setData(contentSend);
                IFCMService ifcmService=Common.getIFCMService();
                ifcmService.sendNotification(dataMessage).enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                        Log.d(TAG, "onResponse: "+response.code());
                        Log.d(TAG, "onResponse: "+response.message());


                        if(response.code()==200){

                            Log.d(TAG, "onResponse: response code is 200 ");
                            if(response.body().success==1){
                                Log.d(TAG, "onResponse: success=1");
                                Toast.makeText(CartActivity.this,
                                        "Thank you order is placed",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(CartActivity.this,
                                        "Failed to send notification", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        Toast.makeText(CartActivity.this,
                                t.getMessage()
                                ,
                                Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });

            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });

    }

    private void getCartList() {
        disposable.add(Common.cartRepository.getCartItems().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) throws Exception {
                        initCartList(carts);
                    }
                }));
    }

    private void initCartList(List<Cart> carts) {
        localCarts = carts;
        cartList.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        cartList.setHasFixedSize(true);
        adapter = new CartAdapter(carts, this);
        adapter.notifyDataSetChanged();
        cartList.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        disposable.clear();
        super.onStop();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.CartViewHolder) {
            final int deletedItemPos = viewHolder.getAdapterPosition();
            String name = localCarts.get(viewHolder.getAdapterPosition()).name;
            final Cart deletedItem = localCarts.get(viewHolder.getAdapterPosition());
            //delete item
            adapter.removeItem(deletedItemPos);
            //delete item from room database
            Common.cartRepository.DeletCartItem(deletedItem);
            Log.d(TAG, "onClick:deletingItem " + adapter.getItemCount());
            Snackbar snackbar = Snackbar.make(root, new StringBuilder(name).append(" removed from Cart"), Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
// restore item

                    adapter.restoreItem(deletedItemPos, deletedItem);
                    Log.d(TAG, "onClick:restoringItem " + adapter.getItemCount());
                    // add item to database
                    Common.cartRepository.InsertToCart(deletedItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
