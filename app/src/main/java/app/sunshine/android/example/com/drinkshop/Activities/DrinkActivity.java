package app.sunshine.android.example.com.drinkshop.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nex3z.notificationbadge.NotificationBadge;

import java.util.List;

import app.sunshine.android.example.com.drinkshop.Model.Drink;
import app.sunshine.android.example.com.drinkshop.R;
import app.sunshine.android.example.com.drinkshop.Retrofit.IDrinkShop;
import app.sunshine.android.example.com.drinkshop.Utils.Common;
import app.sunshine.android.example.com.drinkshop.Utils.DrinksAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DrinkActivity extends AppCompatActivity {
    ImageView CartIcon;
    NotificationBadge badge;
    IDrinkShop mService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.drinkList)
    RecyclerView drinkList;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private ImageView SearchIcon;
    private static final String TAG = "DrinkActivity";
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        ButterKnife.bind(this);
        mService = Common.getAPI();

intiToolbar();
swipeRefresh.post(new Runnable() {
    @Override
    public void run() {
        swipeRefresh.setRefreshing(true);
        updatCartCount();
        getDrinks(Common.currentSelectedCategory.getID());
        TextView tv =root.findViewById(R.id.toolbar_title);
        tv.setText(Common.currentSelectedCategory.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tv.setTextColor(getColor(R.color.darkBrown));
        }
        tv.setTextSize(20);
    }
});
  swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
          swipeRefresh.setRefreshing(true);
          updatCartCount();
          getDrinks(Common.currentSelectedCategory.getID());
          TextView tv =root.findViewById(R.id.toolbar_title);
          tv.setText(Common.currentSelectedCategory.getName());
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              tv.setTextColor(getColor(R.color.darkBrown));
          }
          tv.setTextSize(20);

      }
  });  }
    private void intiToolbar() {
        CartIcon=toolBar.findViewById(R.id.cart);
        SearchIcon = toolBar.findViewById(R.id.search_icon);
        badge=toolBar.findViewById(R.id.notification_badge);
        updatCartCount();
        CartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),CartActivity.class);
                startActivity(intent);
            }
        });
        SearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updatCartCount() {
        if(badge==null) return;
        else{
            if(Common.cartRepository.getCartItemsCount()==0){
                badge.setVisibility(View.INVISIBLE);
            }
            else{
                badge.setVisibility(View.VISIBLE);
                badge.setText(String.valueOf(Common.cartRepository.getCartItemsCount()));
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updatCartCount();
    }


    private void getDrinks(final String menuId) {
        compositeDisposable.add(mService.getDrink(menuId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Drink>>() {
                    @Override
                    public void accept(List<Drink> drinks) throws Exception {
                        //Log.d(TAG, "accept: "+menuId+drinks.get(2).Name+" "+" "+drinks.get(2).ID);
                        displayDrinks(drinks);

                    }
                }));
    }

    private void displayDrinks(List<Drink> drinks) {
        DrinksAdapter adapter = new DrinksAdapter(this, drinks);
        drinkList.setLayoutManager(new GridLayoutManager(this, 2));
        drinkList.setHasFixedSize(true);
        drinkList.setAdapter(adapter);
        swipeRefresh.setRefreshing(false);
}

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
