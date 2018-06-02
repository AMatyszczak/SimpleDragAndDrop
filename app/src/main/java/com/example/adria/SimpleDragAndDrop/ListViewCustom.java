package com.example.adria.SimpleDragAndDrop;

import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.view.MotionEvent.INVALID_POINTER_ID;

public class ListViewCustom extends GridView
{

    private int MOVE_DURATION = 100;


    private long currId;
    private long mPrevItemId;
    private long mNextItemId;
    private AdapterCustom mAdapter;
    private boolean Drag = false;

    ArrayList<String> mArrayList;

    private TextView nextTextView;
    private TextView currTextView;
    private View draggedView;

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
        setOnItemLongClickListener(mOnLongClickListener);
    }

    private AdapterView.OnItemLongClickListener mOnLongClickListener = new AdapterView.OnItemLongClickListener()
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


            return true;
        }
    };

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

                if (position > AdapterView.INVALID_POSITION) {
                    currId = mAdapter.getItemId(position);

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

                                        currTextView = (TextView)getViewFromId(currId);
                                        int position = parent.pointToPosition((int)v.getX(), (int)v.getY());
                                        mNextItemId = mAdapter.getItemId(position);
                                        nextTextView = (TextView)getViewFromId(mNextItemId);

                                        Log.e(TAG, "currId:" + currId );
                                        Log.e(TAG, "nextitemId: " + mNextItemId );
                                        if (currId > -1 && mNextItemId > -1) {
                                            animateDragToStart(currTextView, nextTextView);
                                            mAdapter.swapItems((int)currId,(int)mNextItemId);
                                            mAdapter.notifyDataSetChanged();
                                            currId = mNextItemId;
                                        }


                                        break;
                                    case DragEvent.ACTION_DRAG_EXITED:

                                        break;
                                    case DragEvent.ACTION_DROP:
                                        if(nextTextView != null )
                                            nextTextView.setVisibility(VISIBLE);
                                        if(currTextView != null)
                                            currTextView.setVisibility(VISIBLE);
                                        if(draggedView != null)
                                            draggedView.setVisibility(VISIBLE);

                                        break;
                                    case DragEvent.ACTION_DRAG_ENDED:

                                        final View droppedView = (View) event.getLocalState();
                                        droppedView.post(new Runnable(){
                                            @Override
                                            public void run() {
                                                droppedView.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        if(nextTextView != null )
                                            nextTextView.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    nextTextView.setVisibility(VISIBLE);
                                                }
                                            });

                                        if(currTextView != null)
                                            currTextView.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    currTextView.setVisibility(VISIBLE);
                                                }
                                            });





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
                    draggedView = target;
                    draggedView.setVisibility(INVISIBLE);

                }
            }

            return false;
        }

    };

    private void animateDragToStart(View currView, View nextView) {
        if(currView!= null && nextView!= null)
        {
            float topMargin = nextView.getY() - currView.getTop();
            float leftMargin = nextView.getX() - currView.getLeft();

            // Animation translateAnimation = new TranslateAnimation(leftMargin - (currView.getWidth() / 2), 0, topMargin - (currView.getHeight() / 2), 0);
            Animation translateAnimation = new TranslateAnimation(leftMargin,0,topMargin,0);
            translateAnimation.setDuration(300);
            translateAnimation.setInterpolator(new AccelerateInterpolator());
            currView.startAnimation(translateAnimation);
            currView.setVisibility(View.VISIBLE);
            nextView.setVisibility(INVISIBLE);
        }

    }

    private View getViewFromId(long id)
	{
		int relativePosition = getFirstVisiblePosition();

		for(int i = 0; i < getChildCount(); i++)
		{
			View v = getChildAt(i);
			int position = relativePosition + i;
			long itemId = mAdapter.getItemId(position);
			if(itemId == id)
				return v;
		}
		return null;
	}


	private int getPositionFromId(long id)
	{
		View v = getViewFromId(id);
		if(v == null)
		    return -1;
		else
		    return getPositionForView(v);
	}



}