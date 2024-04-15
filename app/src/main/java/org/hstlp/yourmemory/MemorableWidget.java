package org.hstlp.yourmemory;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class MemorableWidget extends AppWidgetProvider {
    public ArrayList<String> FileInPaths = new ArrayList<>();
    String DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    String Picture = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();

    public static String[] ImageExtensions = new String[]{
            ".jpg",
            ".png",
            ".gif",
            ".jpeg"
    };

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.memorable_widget);

        getImageFiles(Picture);
        getImageFiles(DCIM);

        String randomImage = FileInPaths.get((int)Math.floor(Math.random()*FileInPaths.size()));
        Bitmap bitmap = BitmapFactory.decodeFile(randomImage);
        views.setImageViewBitmap(R.id.memoryImage, bitmap);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public void getImageFiles (String dir) {
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
                    getImageFiles(file.getAbsolutePath());
                }
                else {
                    for(String extension : ImageExtensions) {
                        if(file.getAbsolutePath().toLowerCase().endsWith(extension)) {
                            if(!FileInPaths.contains(file.getAbsolutePath())) {
                                FileInPaths.add(file.getAbsolutePath()); //Kiểm tra có đúng là hình ảnh không bằng cách so sánh đuôi mở rộng với tẹp hình ảnh trước đó
                            }
                            break;
                        }
                    }
                }
            }
        }
        catch(Exception ignored) { }
    }
}