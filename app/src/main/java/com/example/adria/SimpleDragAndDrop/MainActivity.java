package com.example.adria.SimpleDragAndDrop;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ListViewCustom grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grid = (ListViewCustom) findViewById(R.id.listView);


        final ArrayList<String> values = new ArrayList<String> (Arrays.asList( "item 1", "item 2",
                "item 3", "item 4", "item 5", "item 6", "item 7", "item 8",
                "item 9", "item 10", "item 11", "item 12", "item 13", "item 14",
                "item 15","item 16","item 17", "item 18", "item 19",
                "item 20" , "item 21","item 22","item 23"
        ));

        grid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        grid.setMultiChoiceModeListener(new MyMultiChoiceListener());
        grid.setSelector(android.R.color.darker_gray);
        grid.setAdapter(new AdapterCustom(values, this));



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

            View view = grid.getAdapter().getView(i,null,grid);
            view.setBackgroundColor(getResources().getColor(R.color.colorAccent));

            final int checkedItemCount = grid.getCheckedItemCount();
            actionMode.setTitle(checkedItemCount + " Selected");

        }
    }
}
