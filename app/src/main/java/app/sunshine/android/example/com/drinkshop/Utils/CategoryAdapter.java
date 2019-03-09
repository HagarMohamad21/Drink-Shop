package app.sunshine.android.example.com.drinkshop.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.sunshine.android.example.com.drinkshop.Activities.DrinkActivity;
import app.sunshine.android.example.com.drinkshop.Interfaces.ItemClickListener;
import app.sunshine.android.example.com.drinkshop.Model.Category;
import app.sunshine.android.example.com.drinkshop.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder>  {
    Context context;
    List<Category> categories;
    private static final String TAG = "CategoryAdapter";

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View Item=LayoutInflater.from(context).inflate(R.layout.menu_list_item,null,false);
        return new CategoryViewHolder(Item);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int i) {
        Picasso.with(context).load(categories.get(i).getLink())

                .into(holder.ItemImage);
        holder.ItemName.setText(categories.get(i).getName());
        //Event
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v) {
                Common.currentSelectedCategory=categories.get(i);
                Log.d(TAG, "onClick: "+categories.get(i).getID());
                Intent i=new Intent(context,DrinkActivity.class);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


}
