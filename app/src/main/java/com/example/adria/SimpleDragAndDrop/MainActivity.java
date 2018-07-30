package com.example.adria.SimpleDragAndDrop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;

import com.example.adria.SimpleDragAndDrop.GridViewCustom.GridViewCustom;
import com.example.adria.SimpleDragAndDrop.RecyclerViewCustom.Dragg.ItemTouchHelperCallback;
import com.example.adria.SimpleDragAndDrop.RecyclerViewCustom.Dragg.OnStartDrag;
import com.example.adria.SimpleDragAndDrop.RecyclerViewCustom.RecyclerListAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity  implements OnStartDrag {

    GridViewCustom gridView;
    private ItemTouchHelper mItemTouchHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ArrayList<String> values = new ArrayList<> (Arrays.asList( "item 1", "item 2",
                "item 3", "item 4", "item 5", "item 6", "item 7", "item 8",
                "item 9", "item 10", "item 11", "item 12", "item 13", "item 14",
                "item 15","item 16","item 17", "item 18", "item 19",
                "item 20" , "item 21","item 22"
        ));

        RecyclerListAdapter adapter = new RecyclerListAdapter( values);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    private class MyMultiChoiceListener implements AbsListView.MultiChoiceModeListener
    {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.contextual_menu,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {

            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }

        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

            View view = gridView.getAdapter().getView(i,null,gridView);
            view.setBackgroundColor(getResources().getColor(R.color.colorAccent));

            final int checkedItemCount = gridView.getCheckedItemCount();
            actionMode.setTitle(checkedItemCount + " Selected");

        }
    }
}
