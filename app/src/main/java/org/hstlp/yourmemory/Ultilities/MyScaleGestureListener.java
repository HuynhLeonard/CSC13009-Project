package org.hstlp.yourmemory.Ultilities;

import android.view.ScaleGestureDetector;

public class MyScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    private static final float MIN_SCALE_FACTOR = 0.5f;
    private static final float MAX_SCALE_FACTOR = 2.0f;
    private float scaleFactor = 1.0f;

    public float getScaleFactor() {return scaleFactor;}
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();
        scaleFactor = Math.max(MIN_SCALE_FACTOR, Math.min(scaleFactor, MAX_SCALE_FACTOR));
        return true;
    }
}
