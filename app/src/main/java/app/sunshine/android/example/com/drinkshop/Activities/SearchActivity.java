package app.sunshine.android.example.com.drinkshop.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import java.util.ArrayList;
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

public class SearchActivity extends AppCompatActivity {
    List<String> suggestionsList = new ArrayList<>();
    List<Drink> drinkList = new ArrayList<>();
    IDrinkShop mService;
    CompositeDisposable disposable = new CompositeDisposable();
    @BindView(R.id.searchBar)
    MaterialSearchBar searchBar;
    @BindView(R.id.searchList)
    RecyclerView searchList;
DrinksAdapter adapter,searchAdapter;
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        searchBar.setHint("Enter drink name");
        mService=Common.getAPI();
        getDrinks();
        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest=new ArrayList<>();
              for(String searchedWord:suggestionsList){
                  if(searchedWord.toLowerCase().contains(searchBar.getText().toLowerCase())){
                      suggest.add(searchedWord);
                  }
              }
              searchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled){
                   searchList.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
               startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        List<Drink>result=new ArrayList<>();
        for(Drink drink:drinkList){
            if(drink.Name.toLowerCase().contains(text.toString().toLowerCase())){
                result.add(drink);
            }

        }
        searchAdapter =new DrinksAdapter(this,result);
        searchList.setAdapter(searchAdapter);
    }

    private void getDrinks() {
        disposable.add(mService.getAllDrinks().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Drink>>() {
            @Override
            public void accept(List<Drink> drinks) throws Exception {
                displayDrinks(drinks);
                buildSuggestionList(drinks);
            }
        }));
    }

    private void displayDrinks(List<Drink> drinks) {
        drinkList=drinks;
searchList.setHasFixedSize(true);
searchList.setLayoutManager(new GridLayoutManager(this,2));
adapter=new DrinksAdapter(this,drinks);
searchList.setAdapter(adapter);
    }

    private void  buildSuggestionList(List<Drink> drinks){
        for(Drink drink:drinks){
            suggestionsList.add(drink.Name);
        }
        searchBar.setLastSuggestions(suggestionsList);
    };
}
