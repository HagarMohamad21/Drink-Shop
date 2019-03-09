package app.sunshine.android.example.com.drinkshop.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Cart;
import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Favourite;
import de.hdodenhof.circleimageview.CircleImageView;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import app.sunshine.android.example.com.drinkshop.Model.Drink;
import app.sunshine.android.example.com.drinkshop.R;

import static android.support.constraint.Constraints.TAG;
import static app.sunshine.android.example.com.drinkshop.Utils.Common.cupSize;

public class DrinksAdapter extends RecyclerView.Adapter<DrinkViewHolder> {
    Context context;
    List<Drink>drinkList;

    public DrinksAdapter(Context context, List<Drink> drinkList) {
        this.context = context;
        this.drinkList = drinkList;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.drinks_list_item,null,false);

        return new DrinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DrinkViewHolder drinkViewHolder, final int i) {
        Picasso.with(context).load(drinkList.get(i).Link).into(drinkViewHolder.drinkImage);
        drinkViewHolder.DrinkPrice.setText(new StringBuilder("$").append(drinkList.get(i).Price));
        drinkViewHolder.drinkName.setText(drinkList.get(i).Name);
        //Event
        drinkViewHolder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open shopping dialog
                OpenShoppingDialog(i);
            }
        });
        Log.d(TAG, "onBindViewHolder: "+drinkList.get(i).ID);
         Log.d(TAG, "onBindViewHolder: "+drinkList.get(i).Name);

        if(Common.favouriteRepository.isFavourite(Integer.parseInt(drinkList.get(i).ID))==1){
            drinkViewHolder.FavIcon.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
        else{
             drinkViewHolder.FavIcon.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }
            drinkViewHolder.FavIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Common.favouriteRepository.isFavourite(Integer.parseInt(drinkList.get(i).ID))!=1){
                       toggleFavourite(i,true,drinkViewHolder);
                    }
                    else{
                        toggleFavourite(i,false,drinkViewHolder);
                    }
                }
            });
          }

    private void toggleFavourite(int i, boolean isFav,DrinkViewHolder drinkViewHolder) {
        Favourite favourite=new Favourite();
        favourite.id=drinkList.get(i).ID;
        favourite.link=drinkList.get(i).Link;
        favourite.menuId=drinkList.get(i).MenuId;
        favourite.name=drinkList.get(i).Name;
        favourite.price=drinkList.get(i).Price;
        if(!isFav){
            //REMOVE FROM FAV..
            Common.favouriteRepository.deleteFavItem(favourite);
            drinkViewHolder.FavIcon.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            Toast.makeText(context, "Drink removed from your favourites", Toast.LENGTH_SHORT).show();

        }
        else if(isFav){
            //ADD TO FAV..
            drinkViewHolder.FavIcon.setImageResource(R.drawable.ic_favorite_black_24dp);
            Common.favouriteRepository.insertFavItem(favourite);
            Toast.makeText(context, "Drink add to your favourites", Toast.LENGTH_SHORT).show();

        }
     }

    private void OpenShoppingDialog(final int i) {
        AlertDialog.Builder ShoppingDialog =new AlertDialog.Builder(context);
        View shoppingLayout=LayoutInflater.from(context).inflate(R.layout.layout_shopping_dialog,null,false);
        ShoppingDialog.setView(shoppingLayout);
        final ElegantNumberButton elegantNumberButton=shoppingLayout.findViewById(R.id.Quantity);
        TextView DrinkName=shoppingLayout.findViewById(R.id.drinkName);
        CircleImageView  DrinkImage=shoppingLayout.findViewById(R.id.drinkImage);
        RadioButton SizeMeduim=shoppingLayout.findViewById(R.id.mediumCup);
        RadioButton SizeLarge=shoppingLayout.findViewById(R.id.largeCup);
        RadioButton SugarFull=shoppingLayout.findViewById(R.id.full);
        RadioButton SugarMeduim=shoppingLayout.findViewById(R.id.meduim);
        RadioButton SugarHalf=shoppingLayout.findViewById(R.id.half);
        RadioButton SugarFree=shoppingLayout.findViewById(R.id.free);
        RadioButton IceFull=shoppingLayout.findViewById(R.id.fullIced);
        RadioButton IceMeduim=shoppingLayout.findViewById(R.id.meduimIced);
        RadioButton IceHalf=shoppingLayout.findViewById(R.id.halfIced);
        RadioButton IceFree=shoppingLayout.findViewById(R.id.freeIced);
        RecyclerView toppingList=shoppingLayout.findViewById(R.id.toppingList);
        //get the topping list
        toppingList.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        toppingList.setHasFixedSize(true);
        ToppingListAdapter adapter=new ToppingListAdapter(Common.toppingList,context);
        Log.d(TAG, "OpenShoppingDialog: "+Common.toppingList);
        toppingList.setAdapter(adapter);
      DrinkName.setText(drinkList.get(i).Name);
      Picasso.with(context).load(drinkList.get(i).Link).into(DrinkImage);
        ShoppingDialog.setCancelable(false);
        SizeMeduim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Common.cupSize=0;
                }

            }
        });
        SizeLarge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Common.cupSize=1;
                }
            }
        });
        IceMeduim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Common.ice=70;
                }
            }
        });
        IceFree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Common.ice=0;
                }
            }
        });IceHalf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Common.ice=50;
                }
            }
        });IceFull.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Common.ice=100;
                }
            }
        });
        SugarFree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.sugar= 0;}
        });
         SugarHalf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.sugar= 50;}
        });SugarMeduim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.sugar= 70;}
        });SugarFull.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Common.sugar= 100;}
        });


        ShoppingDialog.setPositiveButton("Add to Cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i1) {
                if(Common.sugar==-1){
                    Toast.makeText(context, "Please Fill in all Fields", Toast.LENGTH_SHORT).show();
                    return;
                }if(Common.ice==-1){
                    Toast.makeText(context, "Please Fill in all Fields", Toast.LENGTH_SHORT).show();
                    return;
                }if(cupSize==-1){
                    Toast.makeText(context, "Please Fill in all Fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                //showConfirmDialog
                showConfirmDialog(i,elegantNumberButton.getNumber());
                dialogInterface.dismiss();
            }
        });
        ShoppingDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });
       ShoppingDialog.show();
    }

    private void showConfirmDialog(final int pos, final String quantity) {
        AlertDialog.Builder confirmOrder=new AlertDialog.Builder(context);
        View confirmLayout=LayoutInflater.from(context).inflate(R.layout.layout_confirm_order,null,false);
        confirmOrder.setView(confirmLayout);
        ImageView drinkImage=confirmLayout.findViewById(R.id.drinkImage);
        TextView drinkName=confirmLayout.findViewById(R.id.drinkName);
        TextView drinkPrice=confirmLayout.findViewById(R.id.PriceText);
        final TextView drinkSize=confirmLayout.findViewById(R.id.SizeText);
        TextView drinkSugar=confirmLayout.findViewById(R.id.sugarText);
        TextView dringIce=confirmLayout.findViewById(R.id.IceText);
        TextView drinkToppings=confirmLayout.findViewById(R.id.toppingText);
        TextView drinkQuantity=confirmLayout.findViewById(R.id.QuantityText);
        Picasso.with(context).load(drinkList.get(pos).Link).into(drinkImage);
        drinkName.setText(drinkList.get(pos).Name);
        drinkSize.setText(Common.cupSize==0?"Medium":"Large");
        dringIce.setText(Common.ice==0?"Free":Common.ice==50?"Half":Common.ice==70?"Medium":"Full");
        drinkSugar.setText(Common.ice==0?"Free":Common.ice==50?"Half":Common.ice==70?"Medium":"Full");
        final double Price=Double.parseDouble(quantity)*Double.parseDouble(drinkList.get(pos).Price)+Common.toppingTotalPrice;
        drinkPrice.setText(String.valueOf(Price)+" $");
        drinkQuantity.setText(quantity);
        final StringBuilder ToppingString=new StringBuilder();
        for(String string:Common.toppingAdded){
            ToppingString.append(string+"\n");
        }
        drinkToppings.setText(ToppingString);


        confirmOrder.setPositiveButton("Confirm Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //save request to database
                Cart cartItem=new Cart();
                cartItem.name=drinkList.get(pos).Name;
                cartItem.Ice=Common.ice;
                cartItem.Price=Price;
                cartItem.Sugar=Common.sugar;
                cartItem.Toppings=ToppingString.toString();
                cartItem.Quantity=Integer.parseInt(quantity);
                cartItem.Link=drinkList.get(pos).Link;
                cartItem.size=Common.cupSize;
                Common.cartRepository.InsertToCart(cartItem);
                Log.d(TAG, "Adding Cart to DataBase: "+new Gson().toJson(cartItem));

                Toast.makeText(context, "Cart added to database", Toast.LENGTH_SHORT).show();


            }
        });
        confirmOrder.show();
    }


    @Override
    public int getItemCount() {
        return drinkList.size();
    }
}
