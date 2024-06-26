package org.hstlp.yourmemory.Ultilities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

public class ImageUltility {
    public static Bitmap rotateImage(Bitmap bmpSrc, float degrees) {
        int w = bmpSrc.getWidth();
        int h = bmpSrc.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(degrees);
        Bitmap bmpTrg = Bitmap.createBitmap(bmpSrc, 0, 0, w, h, mtx, true);
        return bmpTrg;
    }
    public static Bitmap setFilter(Bitmap bitmap,String name)
    {
        Bitmap editedBitmap= bitmap.copy(bitmap.getConfig(),true);

        ColorMatrix colorMatrix= new ColorMatrix();
        switch (name)
        {
            case "No Effect":
                colorMatrix.set(new float[]{
                        1,0,0,0,0,
                        0,1,0,0,0,
                        0,0,1,0,0,
                        0,0,0,1,0
                });
                break;
            case "Forest":
                colorMatrix.set(new float[]{
                        0.5f,0,0,0,0,
                        0,0.8f,0,0,0,
                        0,0,0.5f,0,0,
                        0,0,0,1,0
                });
                break;
            case "Cozy":
                colorMatrix.set(new float[]{
                        0.75f,0,0,0,0,
                        0,0.75f,0,0,0,
                        0,0,0,0,50,
                        0.5f,0,0,0,0
                });
                break;
            case "Evergreen":
                colorMatrix.set(new float[]{
                        0,0,0,0,100,
                        0,1,0,0,0,
                        0,0.8f,0,0,0,
                        0,0,0,0.5f,0
                });
                break;
            case "Grayscale":
                colorMatrix.set(new float[]{
                        0.33f,0.33f,0.33f,0,0,
                        0.33f,0.33f,0.33f,0,0,
                        0.33f,0.33f,0.33f,0,0,
                        0,0,0,1,0
                });
                break;
            case "Vintage":
                colorMatrix.set(new float[]{
                        1,0,0,0,0,
                        0,0.8f,0,0,0,
                        0,0,0.5f,0,0,
                        0,0,0,0.5f,0
                }); // Tạo hiệu ứng vintage
                break;
            case "Invert":
                colorMatrix.set(new float[]{
                        -1, 0, 0, 0, 255,
                        0, -1, 0, 0, 255,
                        0, 0, -1, 0, 255,
                        0, 0, 0, 1, 0
                }); //Đảo ngược màu
                break;
            case "Night Vision":
                colorMatrix.set(new float[]{
                        0, 1, 0, 0, 0,
                        1, 0, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 1, 0
                }); // Hiệu ứng tạo ra cảm giác như đang nhìn qua kính quang học của thiết bị tìm kiếm hạt nhân
                break;
            case "Warmth":
                colorMatrix.set(new float[]{
                        1, 0, 0, 0, 0,
                        0, 1, 0, 0, 0,
                        0, 0, 0.8f, 0, 0,
                        0, 0, 0, 1, 0
                }); // Thay đổi thành ánh sáng vàng
                break;
            case "Coolness":
                colorMatrix.set(new float[]{
                        0.8f, 0, 0, 0, 0,
                        0, 1, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 1, 0
                }); // Thay đổi thành ánh sáng xanh
                break;
            default:
                break;
        }

        Paint paint= new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        Canvas canvas=new Canvas(editedBitmap);
        canvas.drawBitmap(editedBitmap,0,0,paint);
        return editedBitmap;
    }
}
