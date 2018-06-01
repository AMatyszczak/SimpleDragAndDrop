package com.example.adria.SimpleDragAndDrop;

import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.view.MotionEvent.INVALID_POINTER_ID;

public class ListViewCustom extends GridView
{
    private long currId;
    private long mPrevItemId;
    private long mNextItemId;
    private AdapterCustom mAdapter;

    ArrayList<String> mArrayList;

    private int mActivePointerId = INVALID_POINTER_ID;

    public void setList(ArrayList<String> ArrayList)
    {
        mArrayList = ArrayList;
    }

    public ListViewCustom(Context context) {
        super(context);
        mAdapter = (AdapterCustom)getAdapter();
        init();
    }

    public ListViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAdapter = (AdapterCustom)getAdapter();
        init();
    }

    public ListViewCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAdapter = (AdapterCustom)getAdapter();
        init();
    }

    void init()
    {
        setOnTouchListener(mOnTouchListener);
    }

    private AdapterView.OnTouchListener mOnTouchListener = new AdapterView.OnTouchListener()
    {
        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            mAdapter = (AdapterCustom)getAdapter();

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                final GridView parent = (GridView) v;

                int x = (int) event.getX();
                int y = (int) event.getY();

                int position = parent.pointToPosition(x, y);
                currId = mAdapter.getItemId(position);
                if (position > AdapterView.INVALID_POSITION) {

                    int count = parent.getChildCount();
                    for (int i = 0; i < count; i++)
                    {

                        final View curr = parent.getChildAt(i);

                        curr.setOnDragListener(new View.OnDragListener() {

                            @Override
                            public boolean onDrag(View v, DragEvent event) {
                                boolean result = true;
                                int action = event.getAction();
                                switch (action) {
                                    case DragEvent.ACTION_DRAG_STARTED:

                                        break;
                                    case DragEvent.ACTION_DRAG_LOCATION:
                                        break;
                                    case DragEvent.ACTION_DRAG_ENTERED:
                                        TextView textView = (TextView)v;
                                        int position = parent.pointToPosition((int)v.getX(), (int)v.getY());
                                        mNextItemId = mAdapter.getItemId(position);
                                        Log.e(TAG, textView.getText().toString() );
                                        mAdapter.swapItems((int)currId,(int)mNextItemId);
                                        mAdapter.notifyDataSetChanged();
                                        currId= mNextItemId;


                                        break;
                                    case DragEvent.ACTION_DRAG_EXITED:

                                        break;
                                    case DragEvent.ACTION_DROP:

                                        break;
                                    case DragEvent.ACTION_DRAG_ENDED:

                                        break;
                                    default:
                                        result = false;
                                        break;
                                }
                                return result;
                            }
                        });
                    }

                    int relativePosition = position - parent.getFirstVisiblePosition();
                    View target = (View) parent.getChildAt(relativePosition);
                    ClipData data = ClipData.newPlainText("DragData", "HOPA");
                    target.startDrag(data, new View.DragShadowBuilder(target), target, 0);
                }
            }
            return false;
        }
    };


}