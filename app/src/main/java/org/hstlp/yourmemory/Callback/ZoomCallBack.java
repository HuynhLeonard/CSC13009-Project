package org.hstlp.yourmemory.Callback;

import android.graphics.Bitmap;

public interface ZoomCallBack {
    void BackToInit();
    Bitmap RotateDegree(String currentImg, float degree, int pos);
    void setImageView(String currentImg,int pos);
}
