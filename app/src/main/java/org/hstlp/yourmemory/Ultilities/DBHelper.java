package org.hstlp.yourmemory.Ultilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "your_memory.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create labels table
        db.execSQL("CREATE TABLE IF NOT EXISTS labels ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "label TEXT UNIQUE)");

        // Create the images table
        db.execSQL("CREATE TABLE IF NOT EXISTS images ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "image_path TEXT UNIQUE)");

        // Create the label_image table
        db.execSQL("CREATE TABLE IF NOT EXISTS label_image ("
                + "label_id INTEGER, "
                + "image_id INTEGER, "
                + "PRIMARY KEY (label_id, image_id), "
                + "FOREIGN KEY(label_id) REFERENCES labels(id), "
                + "FOREIGN KEY(image_id) REFERENCES images(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // use when schema change
    }
}
