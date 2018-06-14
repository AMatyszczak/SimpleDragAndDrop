package com.example.adria.SimpleDragAndDrop;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

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
                "item 20" , "item 21","item 22","item 23","item 24"
        ));
        //Log.e("TAG", "HOLAAAAA2" );
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Snackbar.make(grid,"HOLAAAAAAAAAAAA",Snackbar.LENGTH_SHORT).show();
                Log.e("TAG", "HOLAAAAA" );
                return true;
            }
        });
        grid.setAdapter(new AdapterCustom(values, this));



    }
}
