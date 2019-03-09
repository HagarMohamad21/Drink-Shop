package app.sunshine.android.example.com.drinkshop.Activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Favourite;
import app.sunshine.android.example.com.drinkshop.R;
import app.sunshine.android.example.com.drinkshop.Retrofit.IDrinkShop;
import app.sunshine.android.example.com.drinkshop.Utils.Common;
import app.sunshine.android.example.com.drinkshop.Utils.FavouriteAdapter;
import app.sunshine.android.example.com.drinkshop.Utils.RecyclerItemTouchHelper;
import app.sunshine.android.example.com.drinkshop.Interfaces.RecyclerItemTouchListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FavouriteActivity extends AppCompatActivity implements RecyclerItemTouchListener {
    private static final String TAG = "FavouriteActivity";
    IDrinkShop mService;
    CompositeDisposable disposable = new CompositeDisposable();
    @BindView(R.id.favList)
    RecyclerView favList;
    List<Favourite> localFavourites = new ArrayList<>();
    FavouriteAdapter adapter;
    @BindView(R.id.root)
    RelativeLayout root;
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        ButterKnife.bind(this);
        mService = Common.getAPI();
        getFavourites();
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(favList);
    }

    private void getFavourites() {
        disposable.add(Common.favouriteRepository.getFavItems().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Favourite>>() {
                    @Override
                    public void accept(List<Favourite> favourites) throws Exception {
                        showFavouriteItems(favourites);
                    }
                }));
    }

    //I should load the list again on onResume;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }


    private void showFavouriteItems(List<Favourite> favourites) {
        localFavourites = favourites;
        favList.setLayoutManager(new GridLayoutManager(this, 1));
        favList.setHasFixedSize(true);
        adapter = new FavouriteAdapter(favourites, this);
        favList.setAdapter(adapter);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
if(viewHolder instanceof FavouriteAdapter.FavouriteViewHolder){
    final int deletedItemPos = viewHolder.getAdapterPosition();
    String name = localFavourites.get(viewHolder.getAdapterPosition()).name;
    final Favourite deletedItem = localFavourites.get(viewHolder.getAdapterPosition());
    //delete item
    adapter.removeItem(deletedItemPos);
    //delete item from room database
    Common.favouriteRepository.deleteFavItem(deletedItem);

    Snackbar snackbar=Snackbar.make(root,new StringBuilder(name).append(" removed from favourite"),Snackbar.LENGTH_LONG);
    snackbar.setAction("UNDO", new View.OnClickListener() {
        @Override
        public void onClick(View view) {
// restore item

            adapter.restoreItem(deletedItemPos,deletedItem);
            // add item to database
            Common.favouriteRepository.insertFavItem(deletedItem);
        }
    });
    snackbar.setActionTextColor(Color.YELLOW);
    snackbar.show();
}


    }
}
