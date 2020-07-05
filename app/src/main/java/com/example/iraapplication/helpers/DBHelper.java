package com.example.iraapplication.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.iraapplication.domain.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String dbName = "history.db";
    private static final String schemaName = "history_new";
    private static final int version = 2;

    private String COL_ID = "_id";
    private String COL_TIME = "time";
    private String COL_FROM = "val_from";
    private String COL_TO = "val_to";

    public DBHelper(Context ctx) {
        this(ctx, dbName, null, version);
    }

    private DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "
                + schemaName
                + " ("
                + COL_ID + " TEXT PRIMARY KEY, "
                + COL_TIME + " TEXT, "
                + COL_FROM + " TEXT, "
                + COL_TO + " TEXT "
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + schemaName);
        onCreate(sqLiteDatabase);
    }

    public List<HistoryItem> getItems() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + schemaName + " ;", null);
        if (cursor.getCount() > 0) {
            List<HistoryItem> result = new ArrayList<HistoryItem>();
            cursor.moveToFirst();
            while (true) {
                if (cursor.isLast()) {
                    extractToHistoryItem(cursor, result);
                    cursor.close();
                    return result;
                } else {
                    extractToHistoryItem(cursor, result);
                    cursor.moveToNext();
                }
            }
        }
        return new ArrayList<HistoryItem>();
    }

    public void addItem(HistoryItem item) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ID, item.getId());
        cv.put(COL_TIME, item.getTime());
        cv.put(COL_FROM, item.getFrom());
        cv.put(COL_TO, item.getTo());
        List<HistoryItem> historyPrev = getItems();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (historyPrev.size() >= 10) {
            writableDatabase.delete(schemaName, "_id = ?", new String[]{historyPrev.get(historyPrev.size() - 1).getId()});
        }
        writableDatabase.insert(schemaName, null, cv);
    }

    private static void extractToHistoryItem(Cursor cursor, List<HistoryItem> result) {
        String id = cursor.getString(0);
        String time = cursor.getString(1);
        String from = cursor.getString(2);
        String to = cursor.getString(3);
        result.add(0, new HistoryItem(id, time, from, to));
    }
}
