package com.example.adria.SimpleDragAndDrop.RecyclerViewCustom;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adria.SimpleDragAndDrop.R;
import com.example.adria.SimpleDragAndDrop.RecyclerViewCustom.Dragg.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private ArrayList<String> mArrayList;



    class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.text);

        }

        void update(String text)
        {
            mTextView.setText(text);
            mTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ((AppCompatActivity)view.getContext()).startSupportActionMode(actionModeCallbacks);
                    return true;
                }
            });
        }

    }

    public RecyclerListAdapter(ArrayList<String> items) {
        this.mArrayList = items;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View root = layoutInflater.inflate(R.layout.list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(root);
        root.setTag(viewHolder);

        return (ViewHolder)root.getTag();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.update(mArrayList.get(position));

    }


    @Override
    public int getItemCount() {
        return mArrayList.size();
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mArrayList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mArrayList.remove(position);
        notifyItemRemoved(position);
    }

    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {

        private ArrayList<String> mSelectedItems;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };
}