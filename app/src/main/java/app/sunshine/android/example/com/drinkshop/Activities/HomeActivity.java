package app.sunshine.android.example.com.drinkshop.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.accountkit.AccountKit;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import app.sunshine.android.example.com.drinkshop.Database.DataSource.CartRepository;
import app.sunshine.android.example.com.drinkshop.Database.DataSource.FavouriteRepository;
import app.sunshine.android.example.com.drinkshop.Database.Local.FavouriteDataSource;
import app.sunshine.android.example.com.drinkshop.Database.Local.ShopDataBase;
import app.sunshine.android.example.com.drinkshop.Database.Local.CartDataSource;
import app.sunshine.android.example.com.drinkshop.Model.Banner;
import app.sunshine.android.example.com.drinkshop.Model.Category;
import app.sunshine.android.example.com.drinkshop.Model.Drink;
import app.sunshine.android.example.com.drinkshop.Utils.ProgressRequest;
import app.sunshine.android.example.com.drinkshop.R;
import app.sunshine.android.example.com.drinkshop.Retrofit.IDrinkShop;
import app.sunshine.android.example.com.drinkshop.Utils.CategoryAdapter;
import app.sunshine.android.example.com.drinkshop.Utils.Common;
import app.sunshine.android.example.com.drinkshop.Interfaces.UploadCallBack;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity implements UploadCallBack,
        NavigationView.OnNavigationItemSelectedListener {

    private static final int PICK_IMAGE_REQUEST = 1002;
    private Uri SelectedUri;
    @BindView(R.id.navView)
    NavigationView navView;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle bToggle;
    TextView address, name;
    ImageView profile;
    ImageView CartIcon;
    NotificationBadge badge;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.slider)
    SliderLayout slider;
    IDrinkShop mService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @BindView(R.id.menu_list)
    RecyclerView menuList;
    private static final String TAG = "HomeActivity";
    private ImageView SearchIcon;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        bToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.Nav_Open, R.string.Nav_Close);
        drawerLayout.addDrawerListener(bToggle);
        bToggle.syncState();
        View HeaderLayout = navView.getHeaderView(0);
        address = HeaderLayout.findViewById(R.id.address);
        name = HeaderLayout.findViewById(R.id.name);
        name.setText(Common.currentUser.getName());
        address.setText(Common.currentUser.getAddress());
        profile = HeaderLayout.findViewById(R.id.profile_pic);
        navView.setNavigationItemSelectedListener(this);
        //set profile image
        Picasso.with(this).load(String.valueOf(new StringBuilder(Common.LocalHost)
                .append(Common.currentUser
                .getAvatarUrl()))).into(profile);
        Log.d(TAG, "onCreate: "+new StringBuilder(Common.LocalHost)
                .append(Common.currentUser
                        .getAvatarUrl()));
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ask for permission if you don't have it
                //open file chooser
                chooseImage();
            }
        });
        mService = Common.getAPI();
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                getBanners();
                getMenu();
                getToppingList();
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                getBanners();
                getMenu();
                getToppingList();
            }
        });

        initDB();
        menuList.setHasFixedSize(true);
        menuList.setLayoutManager(new GridLayoutManager(this, 2));
        intiToolbar();

    }


    private void chooseImage() {
        //now open the file chooser intent
        startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent()
                , "Select an image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                if (data != null) {
                    SelectedUri = data.getData();
                    if (SelectedUri != null && !SelectedUri.getPath().isEmpty()) {
                        profile.setImageURI(SelectedUri);
                        uploadImage();
                    } else {
                        Toast.makeText(this,
                                "Error can't get the image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void uploadImage() {
        if (SelectedUri != null) {
            File file = FileUtils.getFile(this, SelectedUri);
            String FileName = new StringBuilder(Common.currentUser.getPhone())
                    .append(FileUtils.getExtension(file.toString())).toString();
            //now use multipart body to upload image to server
            ProgressRequest progressRequest = new ProgressRequest(file, this);
            final MultipartBody.Part body = MultipartBody.Part.createFormData
                    ("uploaded_file", FileName, progressRequest);
            final MultipartBody.Part userPhone = MultipartBody.Part.createFormData
                    ("phone", Common.currentUser.getPhone());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mService.updateAvata(userPhone, body).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Toast.makeText(HomeActivity.this, "uploaded", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(HomeActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }).start();
        }


    }

    private void intiToolbar() {
        CartIcon = toolBar.findViewById(R.id.cart);
        badge = toolBar.findViewById(R.id.notification_badge);
        SearchIcon = toolBar.findViewById(R.id.search_icon);
        updatCartCount();
        CartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });
        SearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });}

    private void updatCartCount() {
        if (badge == null) return;
        else {
            if (Common.cartRepository.getCartItemsCount() == 0) {
                badge.setVisibility(View.INVISIBLE);
            } else {
                badge.setVisibility(View.VISIBLE);
                badge.setText(String.valueOf(Common.cartRepository.getCartItemsCount()));
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updatCartCount();
        isBackButtonPressed = false;
    }

    private void initDB() {
        Common.shopDataBase = ShopDataBase.getInstance(this);
        Common.cartRepository = new CartRepository(CartDataSource.getInstance(Common.shopDataBase.cartDAO()));
        Common.favouriteRepository = new FavouriteRepository(FavouriteDataSource.getInstance(Common.shopDataBase.favouriteDAO()));
    }

    private void getToppingList() {
        compositeDisposable.add(mService.getDrink(Common.MenuId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Drink>>() {
                    @Override
                    public void accept(List<Drink> toppingList) throws Exception {
                        Common.toppingList = toppingList;
                        Log.d(TAG, "accept: "+toppingList);
                    }
                }));
    }

    private void getBanners() {
        compositeDisposable.add(mService.getBanners().subscribeOn(Schedulers.io()
        ).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Banner>>() {
            @Override
            public void accept(List<Banner> banners) throws Exception {
                //  Log.d(TAG, "accept: getBanners"+banners.get(1).getID());
                displayImages(banners);
            }
        }));
    }

    private void getMenu() {
        compositeDisposable.add(mService.getMenu().subscribeOn(Schedulers.io()
        ).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Category>>() {
            @Override
            public void accept(List<Category> categories) throws Exception {
                displaMenu(categories);
            }
        }));
    }

    private void displaMenu(List<Category> categories) {
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        menuList.setAdapter(adapter);
        swipeRefresh.setRefreshing(false);

    }

    private void displayImages(List<Banner> banners) {
        HashMap<String, String> hashMap = new HashMap<>();
        for (Banner item : banners)
            hashMap.put(item.getName(), item.getLink());
        for (String name : hashMap.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView.description(name).image(hashMap.get(name));
            textSliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
            slider.addSlider(textSliderView);
        }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (bToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onProgressUpdate(int percentage) {

    }

    boolean isBackButtonPressed = false;

    @Override
    public void onBackPressed() {
        if (isBackButtonPressed) {
            super.onBackPressed();
            return;
        }

        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        isBackButtonPressed = true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.d(TAG, "onNavigationItemSelected: before switch");
        switch (menuItem.getItemId()) {

            case R.id.signOut:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Sign out");
                builder.setMessage("Are you sure you want to exit from app?");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("Sign out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AccountKit.logOut();
                        //finish activity
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                });
                builder.show();
                break;
            case R.id.Favourite:
                Intent intent = new Intent(HomeActivity.this, FavouriteActivity.class);
                startActivity(intent);
                break;
            case R.id.Orders:
                intent=new Intent(HomeActivity.this,OrdersActivity.class);
                startActivity(intent);
                break;
            case R.id.NearbyPlaces:
                intent = new Intent(HomeActivity.this, NearbyStores.class);
                startActivity(intent);
                break;
            default:
                Log.d(TAG, "onNavigationItemSelected: after switch");
        }
        return false;
    }
}
