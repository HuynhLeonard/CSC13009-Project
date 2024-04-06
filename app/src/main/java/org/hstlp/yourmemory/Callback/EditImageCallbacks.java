package org.hstlp.yourmemory.Callback;

import android.graphics.Bitmap;

public interface EditImageCallbacks {
    void TransformVertical();
    void TransformHorizontal();
    void BackFragment();
    Bitmap blurFast(int radius);
    void ConfirmBlur(Bitmap input);
    void BitmapFilterChoose(Bitmap input,String name);
    void recreateOnDarkMode();
}