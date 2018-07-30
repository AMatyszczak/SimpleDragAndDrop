package com.example.adria.SimpleDragAndDrop.GridViewCustom;

import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

public class CompositeListener implements AdapterView.OnItemLongClickListener
{
    private List<AdapterView.OnItemLongClickListener> mRegisteredListeners = new ArrayList<AdapterView.OnItemLongClickListener>();
    private static CompositeListener INSTANCE;

    public static CompositeListener getINSTANCE()
    {
        if(INSTANCE == null)
            INSTANCE = new CompositeListener();

        return INSTANCE;
    }

    public void registerListener(AdapterView.OnItemLongClickListener listener)
    {
        mRegisteredListeners.add(listener);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        for(AdapterView.OnItemLongClickListener listener: mRegisteredListeners)
        {
            listener.onItemLongClick(adapterView,view,i,l);
        }
        return false;
    }
}
