package app.sunshine.android.example.com.drinkshop.Utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import app.sunshine.android.example.com.drinkshop.Interfaces.ItemClickListener;
import app.sunshine.android.example.com.drinkshop.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView ItemImage;
    TextView  ItemName;
    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        ItemImage=itemView.findViewById(R.id.item_image);
        ItemName=itemView.findViewById(R.id.item_name);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view);
    }
}
