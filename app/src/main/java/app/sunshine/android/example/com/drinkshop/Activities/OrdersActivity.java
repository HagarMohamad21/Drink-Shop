package app.sunshine.android.example.com.drinkshop.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.List;

import app.sunshine.android.example.com.drinkshop.Model.Order;
import app.sunshine.android.example.com.drinkshop.R;
import app.sunshine.android.example.com.drinkshop.Retrofit.IDrinkShop;
import app.sunshine.android.example.com.drinkshop.Utils.Common;
import app.sunshine.android.example.com.drinkshop.Utils.OrderAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrdersActivity extends AppCompatActivity  {
    IDrinkShop mService;
    CompositeDisposable disposable = new CompositeDisposable();
    @BindView(R.id.ordersList)
    RecyclerView ordersList;
    private static final String TAG = "OrdersActivity";
    @BindView(R.id.bottom_nav)
    BottomNavigationViewEx bottomNav;
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ButterKnife.bind(this);
        mService = Common.getAPI();
        getOrders("0");
        bottomNav.enableAnimation(false);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.newIcon:
                        getOrders("0");
                       return true;
                        case R.id.ProssingIcon:
                        getOrders("1");
                            return true;
                        case R.id.ShippingIcon:
                        getOrders("2");
                            return true;
                        case R.id.DeliveredIcon:
                        getOrders("3");
                            return true;
                        case R.id.Cancelled:
                        getOrders("-1");
                            return true;

                }
                return true;
            }
        });

    }

    private void getOrders(String s) {
        disposable.add(mService.getOrders(Common.currentUser.getPhone(), s).
                observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Order>>() {
                    @Override
                    public void accept(List<Order> orders) throws Exception {
                        displayOrders(orders);
                        Log.d(TAG, "accept: " + orders.size());
                        Log.d(TAG, "accept: " + Common.currentUser.getPhone());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "accept: throwable");
                        Toast.makeText(OrdersActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void displayOrders(List<Order> orders) {
        Log.d(TAG, "displayOrders: " + orders.size());
        ordersList.setLayoutManager(new
         LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ordersList.setHasFixedSize(true);
        OrderAdapter adapter = new OrderAdapter(orders, this);
        ordersList.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        disposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        getOrders("0");
        super.onResume();
    }
}
