package com.ife.shop.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IfeSQLite extends CordovaPlugin {
    private DatabaseHelper helper;

    @Override
    protected void pluginInitialize() {
        helper = new DatabaseHelper(cordova.getContext());
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("insert".equals(action)) {
            if (args.length() < 1) {
                callbackContext.error("Name is required");
                return true;
            }
            String name = args.getString(0).trim();
            if (name.length() == 0) {
                callbackContext.error("Name is empty");
                return true;
            }

            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);
            long id = db.insertOrThrow("items", null, values);
            callbackContext.success(String.valueOf(id));
            return true;
        }

        if ("getAll".equals(action)) {
            SQLiteDatabase db = helper.getReadableDatabase();
            JSONArray result = new JSONArray();
            Cursor cursor = db.query("items", new String[]{"id", "name"}, null, null,
                    null, null, "id ASC");
            try {
                while (cursor.moveToNext()) {
                    JSONObject row = new JSONObject();
                    row.put("id", cursor.getLong(0));
                    row.put("name", cursor.getString(1));
                    result.put(row);
                }
            } finally {
                cursor.close();
            }
            callbackContext.success(result);
            return true;
        }

        if ("clear".equals(action)) {
            SQLiteDatabase db = helper.getWritableDatabase();
            int count = db.delete("items", null, null);
            callbackContext.success(String.valueOf(count));
            return true;
        }

        return false;
    }

    @Override
    public void onDestroy() {
        if (helper != null) {
            helper.close();
            helper = null;
        }
        super.onDestroy();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "ife-shop.db";
        private static final int DATABASE_VERSION = 1;

        DatabaseHelper(android.content.Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL" +
                    ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Future schema upgrades will be added here.
        }
    }
}
