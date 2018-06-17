package com.example.adria.SimpleDragAndDrop;

import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ListViewCustom extends GridView implements AdapterView.OnItemLongClickListener
{
    private final static long ANIMATION_DURATION = 225;

    private long mCurrViewId;
    private long mNextItemId;
    private View mCurrView;
    private View mNextView;

    private ActionMode mActionMode;

    private AdapterCustom mAdapter;

    private CompositeListener compositeListener;
    private long emptySpaceId;
    private int emptySpacePositionX;
    private int emptySpacePositionY;


    private boolean isDragging = false;
    private View mDraggedView;
    private long mDraggedViewId;


    public ListViewCustom(Context context) {
        super(context);
        init();
    }

    public ListViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListViewCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init()
    {
        mAdapter = (AdapterCustom)getAdapter();
        compositeListener = new CompositeListener();
        setOnTouchListener(mOnTouchListener);

        setOnItemLongClickListener(mOnLongClickListener);
    }

    private AdapterView.OnItemLongClickListener mOnLongClickListener = new AdapterView.OnItemLongClickListener()
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, final View viewClick, final int index, final long l)
        {

            //isDragging = true;

            return true;
        }
    };



    private AdapterView.OnTouchListener mOnTouchListener = new AdapterView.OnTouchListener()
    {
        @Override
        public boolean onTouch(final View view, MotionEvent event) {

                mAdapter = (AdapterCustom)getAdapter();
                final int X = (int) event.getX();
                final int Y = (int) event.getY();
                int position = pointToPosition(X,Y);

                if(position > AdapterView.INVALID_POSITION)
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if(isDragging == false)
                        {
                            mDraggedViewId = mAdapter.getItemId(position);
                            emptySpaceId = mDraggedViewId;
                            mDraggedView = getViewFromId(mDraggedViewId);
                            mDraggedView.setBackgroundColor(getResources().getColor(R.color.selected));

                            emptySpacePositionX = (int)mDraggedView.getX();
                            emptySpacePositionY = (int)mDraggedView.getY();
                            isDragging = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    {
                        mDraggedView.setBackgroundColor(getResources().getColor(R.color.unselected));
                        mDraggedView.setX(emptySpacePositionX);
                        mDraggedView.setY(emptySpacePositionY);
                        isDragging = false;
                    }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(isDragging)
                        {
                            mDraggedView.setX(X-mDraggedView.getMeasuredWidth()/2);
                            mDraggedView.setY(Y-mDraggedView.getMeasuredHeight()/2);
                            mAdapter.notifyDataSetChanged();
                            mAdapter.notifyDataSetInvalidated();
                            //int pos = pointToPosition((int)event.getX(),(int)event.getY());
                            View viewU = getViewFromId(mAdapter.getItemId(position));
                            if(viewU != mDraggedView)
                            {
                                viewU.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            }

                        }

                        break;
                }
                return true;
        }

    };



    private void animateDragToStart(View currView, View nextView) {
        if(currView!= null && nextView!= null)
        {
            float topMargin = nextView.getY() - currView.getTop();
            float leftMargin = nextView.getX() - currView.getLeft();

            Animation translateAnimation = new TranslateAnimation(leftMargin,0,topMargin,0);
            translateAnimation.setDuration(ANIMATION_DURATION);
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

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long l) {

        return true;
    }

    private class MultiChoiceModeWrapper implements MultiChoiceModeListener {
        MultiChoiceModeListener mWrapped;
        boolean isDraggable;
        boolean selectOnly;

        public MultiChoiceModeWrapper(MultiChoiceModeListener listener) {
            this.mWrapped = listener;
            this.selectOnly = false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            if (mWrapped.onCreateActionMode(actionMode, menu)) {
                isDraggable = true;
                return true;
            }
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
            return mWrapped.onActionItemClicked(actionMode, item);
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            mActionMode = actionMode;
            return mWrapped.onPrepareActionMode(actionMode, menu);
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            isDraggable = true;
            selectOnly = false;
            mActionMode = null;

        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            if( isDraggable)
            {
                isDraggable = false;

            }
            mWrapped.onItemCheckedStateChanged(mode, position, id, checked);
        }
    }

}