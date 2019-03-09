package app.sunshine.android.example.com.drinkshop.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Favourite;
import app.sunshine.android.example.com.drinkshop.R;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {
List<Favourite>favouriteList;
Context context;

    public FavouriteAdapter(List<Favourite> favouriteList, Context context) {
        this.favouriteList = favouriteList;
        this.context = context;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View favLayout=LayoutInflater.from(context).inflate(R.layout.fav_list_item,viewGroup,false);
        return new FavouriteViewHolder(favLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder favouriteViewHolder, int i) {
       favouriteViewHolder.DrinkPrice.setText(favouriteList.get(i).price);
       favouriteViewHolder.DrinkName.setText(favouriteList.get(i).name);
        Picasso.with(context).load(favouriteList.get(i).link).into(favouriteViewHolder.DrinkImage);
    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder{
        LinearLayout foreground;
        RelativeLayout background;
        TextView DrinkName,DrinkPrice;
        ImageView DrinkImage;
        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);

            DrinkName=itemView.findViewById(R.id.drinkName);
            DrinkPrice=itemView.findViewById(R.id.PriceText);
            DrinkImage=itemView.findViewById(R.id.drinkImageCart);
            foreground=itemView.findViewById(R.id.foregroundView);
            background=itemView.findViewById(R.id.backgroundView);

        }
    }
    public void removeItem(int pos){
        favouriteList.remove(pos);
        notifyItemRemoved(pos);
    }
    public void restoreItem(int pos ,Favourite favourite){
        favouriteList.add(pos,favourite);
        notifyItemInserted(pos);
    }
}
