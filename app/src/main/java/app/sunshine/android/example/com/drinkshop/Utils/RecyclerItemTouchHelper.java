package app.sunshine.android.example.com.drinkshop.Utils;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import app.sunshine.android.example.com.drinkshop.Interfaces.RecyclerItemTouchListener;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
RecyclerItemTouchListener itemTouchListener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs,RecyclerItemTouchListener itemTouchListener) {
        super(dragDirs, swipeDirs);
        this.itemTouchListener=itemTouchListener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
     if(itemTouchListener!=null)
         itemTouchListener.onSwiped(viewHolder,i,viewHolder.getAdapterPosition());
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof FavouriteAdapter.FavouriteViewHolder){
            View foreground=((FavouriteAdapter.FavouriteViewHolder)viewHolder).foreground;
            getDefaultUIUtil().clearView(foreground);
        }
        else  if(viewHolder instanceof CartAdapter.CartViewHolder){
            View foreground=((CartAdapter.CartViewHolder)viewHolder).foreground;
            getDefaultUIUtil().clearView(foreground);
        }

        }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder instanceof FavouriteAdapter.FavouriteViewHolder){
        if(viewHolder!=null){ View foreground=((FavouriteAdapter.FavouriteViewHolder)viewHolder).foreground;
            getDefaultUIUtil().onSelected(foreground);}}
        else  if(viewHolder instanceof CartAdapter.CartViewHolder){
            if(viewHolder!=null){ View foreground=((CartAdapter.CartViewHolder)viewHolder).foreground;
                getDefaultUIUtil().onSelected(foreground);}}

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
          @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(viewHolder instanceof FavouriteAdapter.FavouriteViewHolder){
        View foreground=((FavouriteAdapter.FavouriteViewHolder)viewHolder).foreground;
        getDefaultUIUtil().onDraw(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);}
        else  if(viewHolder instanceof CartAdapter.CartViewHolder){
            View foreground=((CartAdapter.CartViewHolder)viewHolder).foreground;
            getDefaultUIUtil().onDraw(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);
        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(viewHolder instanceof FavouriteAdapter.FavouriteViewHolder){
        View foreground=((FavouriteAdapter.FavouriteViewHolder)viewHolder).foreground;
        getDefaultUIUtil().onDrawOver(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);}
        else  if(viewHolder instanceof CartAdapter.CartViewHolder){
            View foreground=((CartAdapter.CartViewHolder)viewHolder).foreground;
            getDefaultUIUtil().onDrawOver(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);}
        }

}
