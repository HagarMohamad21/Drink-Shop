package app.sunshine.android.example.com.drinkshop.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import app.sunshine.android.example.com.drinkshop.Model.Drink;
import app.sunshine.android.example.com.drinkshop.R;

public class ToppingListAdapter extends RecyclerView.Adapter<ToppingListAdapter.ToppingViewHolder> {
    List<Drink> toppingList;
    Context context;

    public ToppingListAdapter(List<Drink> toppingList, Context context) {
        this.toppingList = toppingList;
        this.context = context;
    }

    @NonNull
    @Override
    public ToppingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.topping_list_item,null,false);
        return new ToppingViewHolder(view); }

    @Override
    public void onBindViewHolder(@NonNull ToppingViewHolder toppingViewHolder, final int i) {
        toppingViewHolder.toppingSelected.setText(toppingList.get(i).Name);
           toppingViewHolder.toppingSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                   if(isChecked){
                       Common.toppingAdded.add(compoundButton.getText().toString());
                       Common.toppingTotalPrice+=Double.parseDouble(toppingList.get(i).Price);
                   }
                   else{
                       Common.toppingAdded.remove(compoundButton.getText().toString());
                       Common.toppingTotalPrice-=Double.parseDouble(toppingList.get(i).Price);
                   }
               }
           });
    }

    @Override
    public int getItemCount() {
        return toppingList.size();
    }

    class ToppingViewHolder extends RecyclerView.ViewHolder {
        CheckBox toppingSelected;
        public ToppingViewHolder(@NonNull View itemView) {
            super(itemView);
             toppingSelected=itemView.findViewById(R.id.toppingChevBox);
        }
    }
}
