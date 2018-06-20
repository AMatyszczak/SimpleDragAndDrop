package com.example.adria.SimpleDragAndDrop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.ViewUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;

import static android.content.ContentValues.TAG;

public class ListViewCustom extends GridView {

    private final static long ANIMATION_DURATION = 500;
    private final static int LINE_THICKNESS = 10;

    private ActionMode mActionMode;

    private AdapterCustom mAdapter;

    private CompositeListener compositeListener;

    private int emptySpacePositionX;
    private int emptySpacePositionY;
    private int emptySpacePosition;



    private int mLastEventY = -1;
    private int mLastEventX = -1;

    private int mDownX = -1;
    private int mDownY = -1;
    private View mDownView;

    private boolean isDragging = false;

    private final int INVALID_ID = -1;
    private long mDraggedItemId = INVALID_ID;

    private BitmapDrawable mHoverCell;
    private Rect mHoverCellCurrentBounds;
    private Rect mHoverCellOriginalBounds;

    private final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;


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

    void init() {
        mAdapter = (AdapterCustom) getAdapter();
        //compositeListener = new CompositeListener();
        setOnTouchListener(mOnTouchListener);
        setOnItemLongClickListener(mOnLongClickListener);
    }

    private AdapterView.OnItemLongClickListener mOnLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, final View viewClick, final int index, final long l) {

            int position = pointToPosition(mDownX, mDownY);
            int itemId = position - getFirstVisiblePosition();
            mDownView = getChildAt(itemId);
            mDraggedItemId = getAdapter().getItemId(position);
            mHoverCell = getAndHoverView(mDownView);

            mDownView.setVisibility(INVISIBLE);

            isDragging = true;

            return true;
        }
    };

    private BitmapDrawable getAndHoverView(View view)
    {
        int w = view.getWidth();
        int h = view.getHeight();
        int top = view.getTop();
        int left = view.getLeft();

        Bitmap b = getBitmapWithBorder(view);

        BitmapDrawable drawable = new BitmapDrawable(getResources(),b);

        mHoverCellOriginalBounds = new Rect(left, top, left+w,top+h);
        mHoverCellCurrentBounds = new Rect(mHoverCellOriginalBounds);
        drawable.setBounds(mHoverCellCurrentBounds);

        return drawable;

    }

    private Bitmap getBitmapWithBorder(View view)
    {
        Bitmap bitmap = getBitmapFromView(view);
        Canvas canvas = new Canvas(bitmap);

        Rect rect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(LINE_THICKNESS);
        paint.setColor(Color.BLACK);

        canvas.drawBitmap(bitmap,0,0,null);
        canvas.drawRect(rect,paint);

        return bitmap;
    }

    private Bitmap getBitmapFromView(View view)
    {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;

    }


    private AdapterView.OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            mAdapter = (AdapterCustom)getAdapter();
            switch(motionEvent.getAction() & MotionEvent.ACTION_MASK)
            {
                case MotionEvent.ACTION_DOWN:
                    mDownX = (int)motionEvent.getX();
                    mDownY = (int)motionEvent.getY();
                    mActivePointerId = motionEvent.getPointerId(0);

                    break;
                case MotionEvent.ACTION_MOVE:
                    if(mActivePointerId == INVALID_POINTER_ID)
                        break;

                    int pointerIndex = motionEvent.findPointerIndex(mActivePointerId);
                    mLastEventX = (int)motionEvent.getX(pointerIndex);
                    mLastEventY = (int)motionEvent.getY(pointerIndex);


                    int deltaY = mLastEventY - mDownY;
                    int deltaX = mLastEventX - mDownX;

                    if(isDragging)
                    {
                        int position = pointToPosition(mLastEventX,mLastEventY);
                        final long viewID = mAdapter.getItemId(position);

                        mHoverCellCurrentBounds.offsetTo(mHoverCellOriginalBounds.left + deltaX , mHoverCellOriginalBounds.top + deltaY);
                        mHoverCell.setBounds(mHoverCellCurrentBounds);
                        invalidate();

                        if(viewID != -1)
                        {
                            final View viewUnder = getViewFromId(viewID);
                            //mHoverCellOriginalBounds.offsetTo(viewUnder.getLeft(), viewUnder.getTop());
                            mAdapter.swapItems((int)mDraggedItemId, (int)viewID);
                            mDraggedItemId = viewID;

                            viewUnder.setVisibility(INVISIBLE);
                            mDownView.setVisibility(VISIBLE);
                            mDownView = viewUnder;
                            mAdapter.notifyDataSetChanged();

//                            final ViewTreeObserver observer = getViewTreeObserver();
//                            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                                @Override
//                                public boolean onPreDraw()
//                                {
//                                    observer.removeOnPreDrawListener(this);
//
//                                    int switchViewNewTop = viewUnder.getTop();
//                                    int delta = viewUnder.getTop() - switchViewNewTop;
//
//                                    viewUnder.setTranslationY(delta);
//
//                                    ObjectAnimator animator = ObjectAnimator.ofFloat(viewUnder, View.TRANSLATION_Y, 0);
//                                    animator.setDuration(ANIMATION_DURATION);
//                                    animator.start();
//
//                                    return true;
//                                }
//                            });
                        }
                        return false;
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    if(isDragging) {
                        getViewFromId(mDraggedItemId).setVisibility(VISIBLE);
                        mDraggedItemId = INVALID_ID;
                        mHoverCell = null;

                        invalidate();
                        isDragging = false;
                    }

                        break;
                case MotionEvent.ACTION_CANCEL:
                    if(isDragging) {
                        getViewFromId(mDraggedItemId).setVisibility(VISIBLE);
                        mDraggedItemId = INVALID_ID;
                        mHoverCell = null;

                        invalidate();
                        isDragging = false;
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    pointerIndex = (motionEvent.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    final int pointerId = motionEvent.getPointerId(pointerIndex);
                    if(pointerId == mActivePointerId)
                        if(isDragging) {
                            getViewFromId(mDraggedItemId).setVisibility(VISIBLE);
                            mDraggedItemId = INVALID_ID;
                            mHoverCell = null;

                            invalidate();
                        }
                    break;
                default:
                    break;
            }
            return false;
        }
    };

//    private void animateDragToStart(View currView) {
//        if(currView!= null )
//        {
//
//            float topMargin =  emptySpacePositionY - currView.getTop() ;
//            float leftMargin = emptySpacePositionX - currView.getLeft() ;
//
//            Animation translateAnimation = new TranslateAnimation(0,leftMargin,0,topMargin);
//            translateAnimation.setDuration(0);
//            translateAnimation.setInterpolator(new AccelerateInterpolator());
//            currView.startAnimation(translateAnimation);
//
//        }
//    }

    public View getViewFromId(long id)
	{
		int relativePosition = getFirstVisiblePosition();
        AdapterCustom adapter = (AdapterCustom)getAdapter();
		for(int i = 0; i < getChildCount(); i++)
		{
			View v = getChildAt(i);
			int position = relativePosition + i;
			long itemId = adapter.getItemId(position);
			if(itemId == id)
				return v;
		}
		return null;
	}

	public int getPositionFromID(long itemID)
    {
        View v = getViewFromId(itemID);
        if(v == null) {
            return -1;
        }
        else
            return getPositionForView(v);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(mHoverCell != null)
        {
            mHoverCell.draw(canvas);
        }
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