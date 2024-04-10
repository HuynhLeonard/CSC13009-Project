package org.hstlp.yourmemory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import org.hstlp.yourmemory.Callback.EditImageCallbacks;
import org.hstlp.yourmemory.Ultilities.ImageDelete;
import org.hstlp.yourmemory.Ultilities.ImageUltility;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.model.AspectRatio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
public class EditImage extends AppCompatActivity implements EditImageCallbacks {

    ImageButton edit_cancel;
    ImageButton edit_confirm;
    ImageButton edit_reset;
    LinearLayout transform_btn, filter_btn, blur_btn;
    ImageView edit_img;
    FragmentTransaction ft;
    EditTransformFragment transformFragment;
    // Loc
    BlurEditorFragment blurFragment;
    //Tuan
    EditFilterFragment filterFragment;
    String imgName = null;
    Bitmap editedImage = null;
    Boolean VerticalFlip = false;
    Boolean HorizontalFlip = false;
    LinearLayout linearView;
    FrameLayout fragmentLayoutDisplay;
    String[] listName = {"No Effect", "Forest", "Cozy", "Evergreen", "Grayscale", "Vintage"};
    RelativeLayout edit_nav;

    // add all these layout first then fix
    void initView() {
        fragmentLayoutDisplay = findViewById(R.id.fragment_function_btns);
        linearView = findViewById(R.id.edit_central_btn);
        edit_cancel = findViewById(R.id.edit_cancel_btn);
        edit_confirm = findViewById(R.id.edit_confirm_btn);
        edit_nav = findViewById(R.id.edit_nav);
        edit_reset = findViewById(R.id.edit_reset_btn);
        filter_btn = findViewById(R.id.edit_filter_btn);
        transform_btn = findViewById(R.id.edit_transform_btn);
        blur_btn = findViewById(R.id.blur_btn);
        edit_img = findViewById(R.id.edit_image_object);
    }
    @Override
    public void TransformVertical() {
        Matrix matrixMirror = new Matrix();
        VerticalFlip = !VerticalFlip;
        // left to right and right to left => x change
        matrixMirror.preScale(-1.0f,1.0f);
        editedImage = Bitmap.createBitmap(editedImage,0,0,editedImage.getWidth(), editedImage.getHeight(), matrixMirror, false);

    }

    // the same but we change the x to y coor
    @Override
    public void TransformHorizontal() {

        Matrix matrixMirror = new Matrix();
        HorizontalFlip = !HorizontalFlip;

        matrixMirror.preScale(1.0f, -1.0f);

        editedImage = Bitmap.createBitmap(
                editedImage,
                0,
                0,
                editedImage.getWidth(),
                editedImage.getHeight(),
                matrixMirror,
                false);

        edit_img.setImageBitmap(editedImage);

        Toast.makeText(this, "Horizontal", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void BackFragment() {
        linearView.setVisibility(View.VISIBLE);
        fragmentLayoutDisplay.setVisibility(View.GONE);
        edit_nav.setVisibility(View.VISIBLE);
        edit_img.setImageBitmap(editedImage);
    }
}
