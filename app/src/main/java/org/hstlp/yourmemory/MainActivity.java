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
import android.os.Build;
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

public class MainActivity extends AppCompatActivity implements MainCallBack, View.OnClickListener {
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
        shareConfig = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        edit = shareConfig.edit();
        isDark = shareConfig.getBoolean("darkmode", false);

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        edit.putBoolean("darkmode", isDark);
        edit.commit();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FileInPaths.clear();

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.placehoder)
                .error(R.drawable.error_image);
        Glide.init(this, new GlideBuilder().setDefaultRequestOptions(requestOptions));


        mainActivity = this;


        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET
                }, 1);
        if (savedInstanceState == null) {
            mainImageDisplay = new ImageDisplay();
        }
        else{
            mainImageDisplay =(ImageDisplay) getSupportFragmentManager().getFragment(savedInstanceState, "f0");
        }
        AlbumsFragment.getInstance();
        arrFrag[0] = mainImageDisplay;
        arrFrag[1] = AlbumHostingFragment.getInstance();
        arrFrag[2] = SearchHostingFragment.getInstance();

        DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        Picture = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();

        initView();
        viewPagerAdapter viewPagerAdapter = new viewPagerAdapter(this, Arrays.asList(arrFrag));
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateButtonStatus(position);
            }
        });
        deleteBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        selectAll.setOnClickListener(this);
        createSliderBtn.setOnClickListener(this);
        shareMultipleBtn.setOnClickListener(this);
        addToAlbumBtn.setOnClickListener(this);
        addToFavoriteBtn.setOnClickListener(this);

        arrNavLinearLayouts[0].setOnClickListener(new NavLinearLayouts(0));
        arrNavLinearLayouts[1].setOnClickListener(new NavLinearLayouts(1));
        arrNavLinearLayouts[2].setOnClickListener(new NavLinearLayouts(2));

        setCurrentDirectory(Picture);
        setSupportActionBar(toolbar);
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
    public void shareImages(ArrayList<String> paths) {
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

    @Override
    public boolean getIsDark(){
        return isDark;
    }

    @Override
    public void setIsDark(boolean status) {
        isDark = status;
        edit.putBoolean("darkmode", isDark);
        edit.commit();
        this.recreate();
    }

    @SuppressLint("UseCompatLoadingForDrawable")
    private void showSliderDialogBox() {
        final Dialog customDialog = new Dialog(mainActivity);
        customDialog.setContentView(R.layout.slider_dialog_notify); // Có thể fix lại layout này
        Objects.requireNonNull(customDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        // Kiểm tra và làm nền cho Dialog
        customDialog.findViewById(R.id.cancelSlider)
                .setOnClickListener(view -> customDialog.dismiss());

        customDialog.findViewById(R.id.confirmSlider)
                .setOnClickListener(view -> {

                    RadioGroup radio = customDialog.findViewById(R.id.musicGroup);
                    // Cài đặt nhạc cho slideShow
                    int id = radio.getCheckedRadioButtonId();
                    RadioButton selectedRadionBtn = customDialog.findViewById(id);
                    String name = selectedRadionBtn.getText().toString();

                    customDialog.dismiss();

                    String[] select = chooseToDeleteInList.toArray
                            (new String[0]);

                    Intent intent = new Intent(mainActivity, SlideShow.class)
                            .putExtra("images", select)
                            .putExtra("music", name);

                    startActivity(intent);
                });
        customDialog.show();
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void showCustomDialogBox() {
        final Dialog customDialog = new Dialog(mainActivity);
        customDialog.setTitle("Delete confirm");

        customDialog.setContentView(R.layout.delete_image_confirm_dialog);
        Objects.requireNonNull(customDialog.getWindow()).
                setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));

        ((TextView) customDialog.findViewById(R.id.deleteNotify))
                .setText("Do you want to delete " + deleteNotify + " image(s) permanently in your device ?");
                //Thay đổi thông báo xoá bao nhiêu ảnh
        customDialog.findViewById(R.id.cancelDelete)
                .setOnClickListener(view -> customDialog.dismiss());

        customDialog.findViewById(R.id.confirmDelete)
                .setOnClickListener(view -> {
                    ImageDisplay ic = mainImageDisplay;
                    String[] select = chooseToDeleteInList.toArray
                            (new String[0]);


                    removeImageUpdate(select);
                    ImageDelete.DeleteImage(select);
                    clearChooseToDeleteInList(); //Xoá các ảnh đã chọn
                    ic.deleteClicked();
                    customDialog.dismiss();
                });

        customDialog.show();
    }


    @Override
    public void clearChooseToDeleteInList() {
        chooseToDeleteInList.clear();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { //Trường hợp permission đã được chấp nhận
                askForPermissions();

                ReadFolderTask readPicture = new ReadFolderTask();
                ReadFolderTask readDCIM = new ReadFolderTask();
                readPicture.execute(Picture);
                readDCIM.execute(DCIM);

                viewPager2.setCurrentItem(0);
            }
        } else {
            finish();
        }
    }

    @Override
    public void setCurrentDirectory(String Dir) {
        currentDirectory = Dir;
        folderPaths.add(Dir);
    }

    @Override
    public  void removeImageUpdate(String[] imageList) {    //Xóa nhiều ảnh
        for (String image : imageList) {
            FileInPaths.remove(image);
            mainImageDisplay.removeImage(image);
        }
    }

    @Override
    public  void removeImageUpdate(String image) {  //Xóa 1 ảnh
        FileInPaths.remove(image);
    }

    @Override
    public void renameImageUpdate(String oldName, String newName) {
        changeFileInFolder(Picture, oldName, newName);
        changeFileInFolder(DCIM, oldName, newName);
    }

    @Override
    public void addImageUpdate(String[] imageList) {
        Collections.addAll(FileInPaths, imageList);
    }

    @Override
    public void Holding(boolean isHolding) {    //Hàm xử lý khi giữ 1 ảnh hoặc nhiều ảnh (Sử dụng cho album)
        ImageDisplay instance = onScreenImageDisplay;

        if (isHolding) {
            chooseNavbar.setVisibility(View.VISIBLE);
            navbar.setVisibility(View.INVISIBLE);
            status.setVisibility(View.VISIBLE);
            if (instance.callback != null) {
                instance.callback.onLongClick();
            }
        } else {
            chooseNavbar.setVisibility(View.INVISIBLE);
            navbar.setVisibility(View.VISIBLE);
            status.setVisibility(View.INVISIBLE);

            if (instance.callback != null) {
                instance.callback.afterLongClick();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void SelectedTextChange() {
        deleteNotify = String.valueOf(chooseToDeleteInList.size());
        informationSelected.setText(chooseToDeleteInList.size() + " image selected");
    }

    @Override
    public ArrayList<String> chooseToDeleteInList() {
        return chooseToDeleteInList;
    }

    @Override
    public ArrayList<String> adjustChooseToDeleteInList(String ListInp, String type) {
        switch (type) {
            case "choose":
                if(!chooseToDeleteInList.contains(ListInp)) {
                    chooseToDeleteInList.add(ListInp); // Kiểm tra xem có chứa List delete không nếu không thì thêm ListInp vào để xoá
                }
                break;

            case "unchoose":
                chooseToDeleteInList.remove(ListInp);
                break;
        }
        return chooseToDeleteInList;
    }


    public class ReadFolderTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) { //Sử lý kết quả sau mỗi lần gọi đệ qui
            super.onPostExecute(aVoid);
            mainImageDisplay.notifyChangeGridLayout();
        }
        @Override
        protected Void doInBackground(String... params) { //Đọc các tẹp trong một luồng mà không ảnh hưởng đến UI
            String dir = params[0];
            readFolderAsync(dir);
            return null;
        }

        private void readFolderAsync(String dir) {
            File sdFile = new File(dir);
            File[] foldersSD = sdFile.listFiles();

            try {
                assert foldersSD != null; //Kiểm tra điều kiện nếu false sẽ có AssertionError được ném ra
                for(File file : foldersSD) {
                    if(file.isDirectory()) {
                        if(file.getName().equals("Albums")) {
                            continue;
                        }
                        //Sử dụng AsyncTask để thực hiện gọi đệ qui ở đây
                        ReadFolderTask readFolderTask = new ReadFolderTask();
                        readFolderTask.execute(file.getAbsolutePath());
                    }
                    else {
                        for(String extension : ImageExtensions) {
                            if(file.getAbsolutePath().toLowerCase().endsWith(extension)) {
                                if(!FileInPaths.contains(file.getAbsolutePath()))
                                    FileInPaths.add(file.getAbsolutePath()); //Kiểm tra có đúng là hình ảnh không bằng cách so sánh đuôi mở rộng với tẹp hình ảnh trước đó
                                break;
                            }
                        }
                    }
                }
            }
            catch(Exception ignored){

            }
        }
    }

    private void changeFileInFolder(String Dir, String oldName, String newName) { // Đổi tên tệp hoặc thư mục con
        File sdFile = new File(Dir);
        File[] foldersSD = sdFile.listFiles();
        try{
            assert foldersSD != null;
            for(File file : foldersSD){
                if(file.isDirectory()) {
                    if(file.getName().equals("Albums")){
                        continue;
                    }
                    //Gọi hàm đệ qui tại đây để đổi tên tệp trong thư mục con
                    changeFileInFolder(file.getAbsolutePath(), oldName, newName);
                }
                else{
                    for(String extension :ImageExtensions) {
                        if(file.getAbsolutePath().toLowerCase().endsWith(extension)){ //Kiểm tra có phải ảnh bằng phần đuôi mở rộng
                            if(oldName.equals(file.getName())) { //Nếu trùng với oldName thì tiến hành đổi tên
                                File fromFile = new File(Dir + "/" + oldName);
                                File toFile = new File(Dir + "/" + newName);
                                fromFile.renameTo(toFile);
                                FileInPaths.remove(Dir + "/" + oldName);
                                FileInPaths.add(Dir + "/" + newName);
                                Toast.makeText(this, "doi thanh cong", Toast.LENGTH_LONG).show();
                                //Hiển thị thông báo "doi thanh cong"
                                ImageDisplay ic = mainImageDisplay;
                                ic.notifyChangeGridLayout();
                                break;

                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex){

        }
    }

    @Override
    public String getSDDirectory() {
        return SD;
    }

    @Override
    public String getCurrentDirectory() {
        return currentDirectory;
    }

    @Override
    public void pushFolderPath(String inp) {
        folderPaths.add(inp);
    }

    @Override
    public void popFolderPath() {
        folderPaths.remove(folderPaths.size() - 1);
        currentDirectory = folderPaths.get(folderPaths.size() - 1);
    }

    @Override
    public ArrayList<String> getFolderPath() {
        return folderPaths;
    }

    @Override
    public String getDCIMDirectory() {
        return DCIM;
    }

    @Override
    public String getPictureDirectory() {
        return Picture;
    }

    @Override
    public ArrayList<String> getFileinDir() {
        return FileInPaths;
    }


    private static final int PERMISSION_REQUEST_CODE = 1001;
    public void askForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
    }


    void initView() {
        arrRoundLayout[0] = R.drawable.round_photos;
        arrRoundLayout[1] = R.drawable.round_albums;
        arrRoundLayout[2] = R.drawable.round_settings;

        navbar = findViewById(R.id.navbar);
        chooseNavbar = findViewById(R.id.selectNavbar);
        status = findViewById(R.id.status);

        deleteBtn = findViewById(R.id.deleteImageButton);
        cancelBtn = findViewById(R.id.clear);
        selectAll = findViewById(R.id.selectAll);
        createSliderBtn = findViewById(R.id.createSliderBtn);
        shareMultipleBtn = findViewById(R.id.shareMultipleBtn);
        addToAlbumBtn = findViewById(R.id.addToAlbumBtn);
        addToFavoriteBtn = findViewById(R.id.addToFavoriteBtn);
        informationSelected = findViewById(R.id.infomationText);


        arrIcon[0] = R.drawable.ic_baseline_photo;
        arrIcon[1] = R.drawable.ic_baseline_photo_library;
        arrIcon[2] = R.drawable.magnify;


        arrSelectedIcon[0] = R.drawable.ic_baseline_photo_selected;
        arrSelectedIcon[1] = R.drawable.ic_baseline_photo_library_selected;
        arrSelectedIcon[2] = R.drawable.magnify;

        arrNavLinearLayouts[0] = findViewById(R.id.photosLayout);
        arrNavLinearLayouts[1] = findViewById(R.id.albumsLayout);
        arrNavLinearLayouts[2] = findViewById(R.id.searchLayout);

        arrNavImageViews[0] = findViewById(R.id.photos_img);
        arrNavImageViews[1] = findViewById(R.id.albums_img);
        arrNavImageViews[2] = findViewById(R.id.search_img);

        arrNavTextViews[0] = findViewById(R.id.photos_txt);
        arrNavTextViews[1] = findViewById(R.id.albums_txt);
        arrNavTextViews[2] = findViewById(R.id.search_txt);

        viewPager2 = findViewById(R.id.viewPager);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.deleteImageButton) { //Trường hợp bấm nút xóa
            showCustomDialogBox();
        } else if (id == R.id.clear) { //Trường hợp bấm nút clear
            ImageDisplay ic1 = onScreenImageDisplay;
            clearChooseToDeleteInList();
            ic1.clearClicked();
        } else if (id == R.id.selectAll) { //Trường hợp bấm nút chọn tất cả
            ImageDisplay ic2 = onScreenImageDisplay;
            if (chooseToDeleteInList.size() == ic2.listAdapter.filteredList.size()) { //Kiểm tra các số phần tử trong danh sách xóa có bằng số phần từ đã được filter hay không
                chooseToDeleteInList.clear();
            } else {
                chooseToDeleteInList = new ArrayList<>(ic2.listAdapter.filteredList);
            }
            ic2.selectAllClicked();
        } else if (id == R.id.createSliderBtn) { //Trường hợp bấm vào nút tạo slider
            Toast.makeText(mainActivity, "Create slideshow", Toast.LENGTH_SHORT).show();
            showSliderDialogBox();
        } else if (id == R.id.shareMultipleBtn) { //Trường hợp bấm vào nút share
            Toast.makeText(mainActivity, "Share multiple", Toast.LENGTH_SHORT).show();
            String[] select = chooseToDeleteInList.toArray(new String[0]);
            ArrayList<String> paths = new ArrayList<>();
            Collections.addAll(paths, select);
            shareImages(paths);
        } else if (id == R.id.addToAlbumBtn) {//Trường hợp thêm vào album
            AlbumChoosingDialog albumChoosingDialog = new AlbumChoosingDialog(mainActivity);
            albumChoosingDialog.show();
        } else if (id == R.id.addToFavoriteBtn) {//Trường hợp thêm vào yêu thích
            MoveOrCopyForDialog moveOrCopyForDialogDialog = new MoveOrCopyForDialog(mainActivity, new MoveOrCopyForDialog.MoveOrCopyCallBack() {
                @Override
                public void dismissCallback(String method) {
                    if (method.equals("remove")) {
                        ImageDisplay ic = mainImageDisplay;
                        clearChooseToDeleteInList();
                        ic.clearClicked();
                    }
                }

                @Override
                public void copiedCallback(String newImagePath) {
                    assert AlbumsFragment.favoriteAlbum() != null;
                    AlbumsFragment.favoriteAlbum().imagePaths.add(newImagePath);
                }

                @Override
                public void removedCallback(String oldImagePath, String newImagePath) {
                    mainImageDisplay.removeImage(oldImagePath);
                    assert AlbumsFragment.favoriteAlbum() != null;
                    AlbumsFragment.favoriteAlbum().imagePaths.add(newImagePath);
                }
            }, AlbumsFragment.favoriteAlbum(), chooseToDeleteInList());
            moveOrCopyForDialogDialog.show();
        }
    }

    protected class NavLinearLayouts implements View.OnClickListener {
        public int thisIndex;

        NavLinearLayouts(int index) {
            thisIndex = index;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View view) {
            if (selectedTab != thisIndex) {
                selectedTab = thisIndex;

                viewPager2.setCurrentItem(thisIndex);
                updateButtonStatus(thisIndex);
            }
        }
    }

    private void updateButtonStatus(int thisIndex) {
        if (thisIndex>=3)
            return;
        for (int i = 0; i < 3; i++) {
            if (i != thisIndex) {
                arrNavTextViews[i].setVisibility(View.GONE);

            }
            arrNavTextViews[thisIndex].setVisibility(View.VISIBLE);
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,
                    1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0.0f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            arrNavLinearLayouts[thisIndex].startAnimation(scaleAnimation);
        }
    }

    private class AlbumChoosingDialog extends Dialog {
        @SuppressLint("UseCompatLoadingForDrawables")
        public AlbumChoosingDialog(@NonNull Context context) {
            super(context);
            @SuppressLint("InflateParams") View layout =
                    getLayoutInflater().inflate(R.layout.album_choosing, null);

            ImageButton backBtn = layout.findViewById(R.id.album_choosing_back);
            backBtn.setOnClickListener(view -> dismiss());

            GridView imageList = layout.findViewById(R.id.album_choosing_list);
            imageList.setAdapter(new AlbumChoosingAdapter());

            imageList.setOnItemClickListener((adapterView, view, i, l) -> {
                view.setBackgroundResource(R.drawable.custom_row_album);

                Drawable buttonDrawable = view.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, context.getResources().getColor(R.color.fullScreenBtn));
                view.setBackground(buttonDrawable);
                MoveOrCopyForDialog dialog = new MoveOrCopyForDialog(context, new MoveOrCopyForDialog.MoveOrCopyCallBack() {
                    @Override
                    public void dismissCallback(String method) {
                        view.setBackgroundResource(android.R.color.transparent);

                        view.setBackgroundTintList(null);
                        TextView imgCount = view.findViewById(R.id.album_images_count);
                        imgCount.setText(String.format(context.getString(R.string.album_image_count), AlbumsFragment.albumList.get(i).imagePaths.size()));
                        if (method.equals("remove")) {
                            ImageDisplay ic = mainImageDisplay;
                            clearChooseToDeleteInList();
                            ic.clearClicked();
                            dismiss();
                        }
                    }

                    @Override
                    public void copiedCallback(String newImagePath) {
                        AlbumsFragment.albumList.get(i).imagePaths.add(newImagePath);
                    }

                    @Override
                    public void removedCallback(String oldImagePath, String newImagePath) {
                        mainImageDisplay.removeImage(oldImagePath);
                        AlbumsFragment.albumList.get(i).imagePaths.add(newImagePath);
                    }


                }, AlbumsFragment.albumList.get(i), chooseToDeleteInList());
                dialog.show();
            });

            setContentView(layout);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(Objects.requireNonNull(getWindow()).getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
            getWindow().setAttributes(layoutParams);
        }

        private class AlbumChoosingAdapter extends BaseAdapter {
            ArrayList<Album> albumList;

            public AlbumChoosingAdapter() {
                this.albumList = AlbumsFragment.albumList;
            }

            @Override
            public int getCount() {
                return albumList.size();
            }

            @Override
            public Object getItem(int i) {
                return albumList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                ViewHolder viewHolder;
                if (view == null) {
                    view = getLayoutInflater().inflate(R.layout.row_album, viewGroup, false);
                    viewHolder = new ViewHolder();
                    viewHolder.albumName = view.findViewById(R.id.album_name);
                    viewHolder.albumImageCount = view.findViewById(R.id.album_images_count);
                    viewHolder.imageView = view.findViewById(R.id.album_image);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                viewHolder.albumName.setText(albumList.get(i).name);
                viewHolder.albumImageCount.setText(String.format(mainActivity.getString(R.string.album_image_count), albumList.get(i).imagePaths.size()));
                view.setBackgroundTintList(null);
                if (albumList.get(i).name.equals(AlbumsFragment.favourite)) {
                    viewHolder.imageView.setImageResource(R.drawable.ic_baseline_favorite_24);
                } else {
                    viewHolder.imageView.setImageResource(R.drawable.ic_baseline_folder_24);
                }
                return view;
            }

            private class ViewHolder {
                TextView albumName;
                TextView albumImageCount;
                ImageView imageView;
            }
        }
    }

    public void analyse() {
        ImageLabelWrapper imageLabelWrapper = ImageLabelWrapper.getInstance();
        for (String path : FileInPaths) {
            imageLabelWrapper.getLabels(path, null);
        }
    }

    public void purgeDatabase() {
        DatabaseManager dbManager = new DatabaseManager(this);
        dbManager.purgeDatabase();
    }
}