package app.sunshine.android.example.com.drinkshop.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.Toast;


import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import app.sunshine.android.example.com.drinkshop.Activities.OrdersActivity;
import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Cart;
import app.sunshine.android.example.com.drinkshop.Interfaces.ItemClickListener;
import app.sunshine.android.example.com.drinkshop.Model.Order;
import app.sunshine.android.example.com.drinkshop.R;
import app.sunshine.android.example.com.drinkshop.Retrofit.IDrinkShop;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder>  {
    List<Order>orders;
    Context context;
    List<Cart>cartList;
    private static final String TAG = "OrderAdapter";
    public OrderAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View Layout=LayoutInflater.from(context).inflate(R.layout.order_list_item,
                viewGroup,false);
        Log.d(TAG, "onCreateViewHolder: ");
        return new OrderViewHolder(Layout);

    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder orderViewHolder, final int i) {
        setCartList(orders.get(i));
     orderViewHolder.OrderNumber.setText("# "+String.valueOf(orders.get(i).getOrderID()));
     orderViewHolder.OrderPrice.setText("$ "+String.valueOf(orders.get(i).getOrderPrice()));
     orderViewHolder.OrderStatus.setText(orders.get(i).getOrderText());
     orderViewHolder.OrderComment.setText(orders.get(i).getOrderComment());


     if(orders.get(i).getOrderStatus()==-1){
         orderViewHolder.CancelOrder.setText("replace");
         orderViewHolder.CancelOrder.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 // undo the order
                 final IDrinkShop mService=Common.getAPI();
                 mService.cancelOrder(Common.currentUser.getPhone(),
                         String.valueOf(orders.get(i).getOrderID()),"0").enqueue(new Callback<String>() {
                     @Override
                     public void onResponse(Call<String> call, Response<String> response) {
                         Toast.makeText(context, "Order number #"+String.valueOf(orders.get(i).getOrderID())+"has been replaced", Toast.LENGTH_SHORT).show();
                         //refresh the order list by removing it from recycler view
                         ((Activity)context).finish();
                         ( context).startActivity(new Intent(context,OrdersActivity.class));


                     }

                     @Override
                     public void onFailure(Call<String> call, Throwable t) {
                         Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 });

             }
         });
     }
     else {
         orderViewHolder.CancelOrder.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 // cancel the order
                 final IDrinkShop mService=Common.getAPI();
                 mService.cancelOrder(Common.currentUser.getPhone(),String.valueOf(orders.get(i).getOrderID()),"-1").enqueue(new Callback<String>() {
                     @Override
                     public void onResponse(Call<String> call, Response<String> response) {
                         Toast.makeText(context, "Order number #"+String.valueOf(orders.get(i).getOrderID())+"has been cancelled", Toast.LENGTH_SHORT).show();
                         //refresh the order list by removing it from recycler view
                         ((Activity)context).finish();
                         ( context).startActivity(new Intent(context,OrdersActivity.class));


                     }

                     @Override
                     public void onFailure(Call<String> call, Throwable t) {
                         Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 });
             }
         });
     }
     orderViewHolder.ToggleExpand.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
     orderViewHolder.ExpandableLayout.toggle(400,new AccelerateDecelerateInterpolator());



         }
     });

        // populate the recycler view
        orderViewHolder.orderDetailList.setLayoutManager
                (new GridLayoutManager(context,1));
        OrderDetailAdapter adapter=new OrderDetailAdapter(context,cartList);
        orderViewHolder.orderDetailList.setAdapter(adapter);

orderViewHolder.ExpandableLayout.setListener(new ExpandableLayoutListener() {
    @Override
    public void onAnimationStart() {

    }

    @Override
    public void onAnimationEnd() {

    }

    @Override
    public void onPreOpen() {

    }

    @Override
    public void onPreClose() {

    }

    @Override
    public void onOpened() {

    }

    @Override
    public void onClosed() {

    }
});
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setCartList(Order CurrentOrder){
        cartList=new Gson().fromJson(CurrentOrder.getOrderDetail(),new TypeToken<List<Cart>>(){}.getType());
        Log.d(TAG, "setCartList: "+cartList.size());
        }


}
