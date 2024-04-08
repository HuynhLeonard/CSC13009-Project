package org.hstlp.yourmemory.Ultilities;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PinchZoomItemTouchListener implements RecyclerView.OnItemTouchListener {
    private final GestureDetector gestureDetector;
    private final ScaleGestureDetector scaleGestureDetector;
    private final GridLayoutManager gridLayoutManager;
    private int currentSpanCount;

    public PinchZoomItemTouchListener(Context context, final RecyclerView recyclerView, GridLayoutManager layoutManager) {
        this.gridLayoutManager = layoutManager;
        this.currentSpanCount = layoutManager.getSpanCount();
        this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });

        MyScaleGestureListener scaleGestureListener = new MyScaleGestureListener();
        this.scaleGestureDetector = new ScaleGestureDetector(context, scaleGestureListener);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        scaleGestureDetector.onTouchEvent(e);

        if (scaleGestureDetector.isInProgress()) {
            float scaleFactor = ( scaleGestureDetector).getScaleFactor();
            int newSpanCount = Math.round(currentSpanCount * scaleFactor);
            newSpanCount = Math.max(1, Math.min(newSpanCount, 8));

            if (newSpanCount != currentSpanCount) {
                animateSpanCountChange(newSpanCount);
            }
        }

        return false;
    }

    private void animateSpanCountChange(final int newSpanCount) {
        ValueAnimator animator = ValueAnimator.ofInt(currentSpanCount, newSpanCount);
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            gridLayoutManager.setSpanCount(value);
            gridLayoutManager.requestLayout();
        });

        animator.setDuration(300);
        animator.start();

        currentSpanCount = newSpanCount;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        // Handle touch events if needed
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // Handle request to disallow touch intercept if needed
    }
}
