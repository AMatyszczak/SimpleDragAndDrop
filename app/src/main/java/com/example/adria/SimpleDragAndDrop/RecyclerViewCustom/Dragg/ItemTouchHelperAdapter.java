package com.example.adria.SimpleDragAndDrop.RecyclerViewCustom.Dragg;

public interface ItemTouchHelperAdapter
{

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
