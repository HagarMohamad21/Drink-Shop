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
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Cart;
import app.sunshine.android.example.com.drinkshop.Database.ModelDb.Favourite;
import app.sunshine.android.example.com.drinkshop.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    List<Cart> cartList;
    Context context;

    public CartAdapter(List<Cart> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout=LayoutInflater.from(context).inflate(R.layout.cart_list_item_layout,viewGroup,false);
        return new CartViewHolder(layout);
    }


    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, final int i) {
        cartViewHolder.drinkSize.setText(cartList.get(i).size==0?"Medium":"Large");
       cartViewHolder.dringIce.setText(cartList.get(i).Ice==50?"Half":cartList.get(i).Ice==0?"Free":
               cartList.get(i).Ice==100?"Full":"Medium");
       cartViewHolder.drinkSugar.setText(cartList.get(i).Sugar==50?"Half":cartList.get(i).Sugar==0?"Free":
               cartList.get(i).Sugar==100?"Full":"Medium");
       cartViewHolder.drinkPrice.setText(String.valueOf(cartList.get(i).Price));
       cartViewHolder.drinkName.setText(cartList.get(i).name);
      Picasso.with(context).load(cartList.get(i).Link).into(cartViewHolder.drinkImage);
      cartViewHolder.amount.setNumber(String.valueOf(cartList.get(i).Quantity));
      cartViewHolder.amount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
          @Override
          public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
              Cart cart=cartList.get(i);
              cart.Quantity=newValue;
              Common.cartRepository.UpdateCartItem(cart);
              Toast.makeText(context, "Cart has been updated", Toast.LENGTH_SHORT).show();
          }
      });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView drinkImage;
         TextView drinkName,drinkPrice,drinkSize,drinkSugar,dringIce,drinkQuantity;
        LinearLayout foreground;
        RelativeLayout background;
         ElegantNumberButton amount;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            drinkImage  =itemView.findViewById(R.id.drinkImageCart);
            drinkName =itemView.findViewById(R.id.drinkName);
            drinkPrice =itemView.findViewById(R.id.PriceText);
            drinkSize =itemView.findViewById(R.id.SizeText);
            drinkSugar=itemView.findViewById(R.id.sugarText);
            dringIce=itemView.findViewById(R.id.IceText);
            drinkQuantity=itemView.findViewById(R.id.QuantityText);
            amount=itemView.findViewById(R.id.Quantity);
            foreground=itemView.findViewById(R.id.foregroundView);
            background=itemView.findViewById(R.id.backgroundView);
        }
    }
    public void removeItem(int pos){
        cartList.remove(pos);
        notifyItemRemoved(pos);
    }
    public void restoreItem(int pos ,Cart cart){
        cartList.add(pos,cart);
        notifyItemInserted(pos);
    }
}
