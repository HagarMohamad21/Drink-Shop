package app.sunshine.android.example.com.drinkshop.Utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;

import app.sunshine.android.example.com.drinkshop.Interfaces.ItemClickListener;
import app.sunshine.android.example.com.drinkshop.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
    ItemClickListener itemClickListener;
    TextView OrderNumber,OrderStatus,OrderPrice,OrderComment;
   ImageView ToggleExpand;
ExpandableLinearLayout ExpandableLayout;
RecyclerView orderDetailList;
Button CancelOrder;
    private static final String TAG = "OrderViewHolder";
    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        OrderNumber=itemView.findViewById(R.id.OrderNumber);
        OrderStatus=itemView.findViewById(R.id.OrderStaus);
        OrderPrice=itemView.findViewById(R.id.OrderPrice);
        OrderComment=itemView.findViewById(R.id.OrderComment);
        ToggleExpand=itemView.findViewById(R.id.toggleExpandBtn);
        ExpandableLayout=itemView.findViewById(R.id.expandableLayout);
        orderDetailList=itemView.findViewById(R.id.orderDetailsList);
        CancelOrder=itemView.findViewById(R.id.CancelOrder);



    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public boolean onLongClick(View view) {
        Log.d(TAG, "onLongClick: ");
        itemClickListener.onClick(view);
        return true;
    }
}
