package app.sunshine.android.example.com.drinkshop.Interfaces;

import android.support.v7.widget.RecyclerView;

public interface RecyclerItemTouchListener {
     void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
