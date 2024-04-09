package org.hstlp.yourmemory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.request.RequestOptions;
import org.hstlp.yourmemory.Callback.MainCallBack;
import org.hstlp.yourmemory.Ultilities.DatabaseManager;
import org.hstlp.yourmemory.Ultilities.ImageDelete;
import org.hstlp.yourmemory.Ultilities.SlideShow;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public ArrayList<String> FileInPaths = new ArrayList<>();
    String currentDirectory = null;
    String SD;
    String DCIM;
    String Picture;
    ArrayList<String> folderPaths = new ArrayList<>();
    LinearLayout navbar;
    RelativeLayout chooseNavbar;
    RelativeLayout status;
    static MainActivity mainActivity;
    static ImageDisplay onScreenImageDisplay;
    FloatingActionButton deleteBtn;
    FloatingActionButton cancelBtn;
    FloatingActionButton selectAll;
    FloatingActionButton createSliderBtn;
    FloatingActionButton shareMultipleBtn;
    FloatingActionButton addToAlbumBtn;
    FloatingActionButton addToFavoriteBtn;
    TextView informationSelected;
    String deleteNotify = "";
    boolean isDark;
    SharedPreferences shareConfig;
    SharedPreferences.Editor edit;
    public ArrayList<String> chooseToDeleteInList = new ArrayList<>();
    LinearLayout[] arrNavLinearLayouts = new LinearLayout[3];
    ImageView[] arrNavImageViews = new ImageView[3];
    TextView[] arrNavTextViews = new TextView[3];
    private int selectedTab = 0;
    int[] arrRoundLayout = new int[3];
    int[] arrIcon = new int[3];
    int[] arrSelectedIcon = new int[3];
    Fragment[] arrFrag = new Fragment[3];
    static ImageDisplay mainImageDisplay;

    ViewPager2 viewPager2;
    Toolbar toolbar;
    public static String[] ImageExtensions = new String[]{
            ".jpg",
            ".png",
            ".gif",
            ".jpeg"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        getSupportFragmentManager().putFragment(savedInstanceState, "f0", arrFrag[0]);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @SuppressLint("SetWorldReadable")
    @Override
    public void shareImage(ArrayList<String> paths) {
        Intent intent;
        ArrayList<Bitmap> bitmapList = new ArrayList<>();

        for (int i = 0; i < paths.size(); i++) {
            bitmapList.add(BitmapFactory.decodeFile(paths.get(i))); //Decode file thành các bitmap và lưu lại vào 1 list các bitmap
        }

        try {
            ArrayList<Uri> uriList = new ArrayList<>();

            for (int i = 0; i < paths.size(); i++) {
                File file = new File(paths.get(i));
                FileOutputStream fout = new FileOutputStream(file); //Mở đường dẫn để ghi vào file

                bitmapList.get(i).compress(Bitmap.CompressFormat.JPEG, 100, fout);  //Ghi vào file vừa tạo các bitmap với format là ảnh jpeg và chất lượng 100

                fout.flush();
                fout.close();

                file.setReadable(true, false);
                Uri uri = FileProvider.getUriForFile(this, "com.example.YourMemory.provider", file); //Lấy đường dẫn uri của file
                uriList.add(uri);
            }

            if (paths.size() == 1) { //Chuyển data đi
                intent = new Intent(Intent.ACTION_SEND);
            } else {
                intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (paths.size() == 1) {
                intent.putExtra(Intent.EXTRA_STREAM, uriList.get(0));
            } else {
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.setType("image/*");
            startActivity(Intent.createChooser(intent, "Share file via"));

        } catch (Exception ignored) {}
    }

}