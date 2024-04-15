package org.hstlp.yourmemory;

import android.app.Activity;
import android.app.AlarmManager;
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

    private static final String ACTION_UPDATE_WIDGET = "org.hstlp.yourmemory.ACTION_UPDATE_WIDGET";
    private static final int ALARM_INTERVAL = 10;

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

        String randomImage = FileInPaths.get((int)Math.floor(Math.random() * FileInPaths.size()));
        Bitmap bitmap = BitmapFactory.decodeFile(randomImage);
        views.setImageViewBitmap(R.id.memoryImage, bitmap);

        //final Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Gửi một broadcast để cập nhật tiện ích khi được kích hoạt bởi AlarmManager
        Intent intent = new Intent(context, MemorableWidget.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.memoryImage, pendingIntent);
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

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null && intent.getAction().equals(ACTION_UPDATE_WIDGET)) {
            // Nếu nhận được hành động cập nhật từ AlarmManager, thực hiện cập nhật tiện ích
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, MemorableWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        // Lập lịch cho việc cập nhật tiện ích định kỳ bằng AlarmManager khi tiện ích được kích hoạt
        scheduleWidgetUpdates(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        // Hủy lịch trình cập nhật tiện ích khi tiện ích được tắt
        cancelWidgetUpdates(context);
    }

    private void scheduleWidgetUpdates(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MemorableWidget.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), ALARM_INTERVAL, pendingIntent);
    }

    private void cancelWidgetUpdates(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MemorableWidget.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }
}