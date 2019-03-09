package app.sunshine.android.example.com.drinkshop.Utils;


import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import app.sunshine.android.example.com.drinkshop.R;

public class DrinkViewHolder extends RecyclerView.ViewHolder{
ImageView drinkImage,FavIcon;
TextView drinkName,DrinkPrice;
ImageButton addToCart;
    public DrinkViewHolder(@NonNull View itemView) {
        super(itemView);
        drinkImage=itemView.findViewById(R.id.item_image);
        drinkName=itemView.findViewById(R.id.item_name);
        DrinkPrice=itemView.findViewById(R.id.price);
        FavIcon=itemView.findViewById(R.id.FavIcon);
        addToCart=itemView.findViewById(R.id.addToCart);

    }
}
