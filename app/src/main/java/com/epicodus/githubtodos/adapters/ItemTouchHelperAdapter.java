package com.epicodus.githubtodos.adapters;

/**
 * Created by Guest on 12/16/16.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}