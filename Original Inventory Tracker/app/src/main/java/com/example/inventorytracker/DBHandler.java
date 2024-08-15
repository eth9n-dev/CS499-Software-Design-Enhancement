package com.example.inventorytracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.SmsManager;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "accountsdb";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "accounts";
    private static final String ID_COL = "id";
    private static final String USER_COL = "username";
    private static final String PASS_COL = "password";

    //------------------------------------------------

    private static final String TABLE_TWO = "inventory";
    private static final String ITEM_NAME_COL = "item_name";
    private static final String QUANTITY_COL = "quantity";



    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_COL + " TEXT,"
                + PASS_COL + " TEXT)";

        String query2 = "CREATE TABLE " + TABLE_TWO + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ITEM_NAME_COL + " TEXT,"
                + QUANTITY_COL + " TEXT)";

        db.execSQL(query);
        db.execSQL(query2);
        Log.d("DBG", "Database created.");
    }

    public void addNewUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_COL, username);
        values.put(PASS_COL, password);

        db.insert(TABLE_NAME, null, values);

        db.close();
        Log.d("DBG", "User added.");
    }

    public void addNewItem(String itemName, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME_COL, itemName);
        values.put(QUANTITY_COL, quantity);

        db.insert(TABLE_TWO, null, values);

        db.close();
        Log.d("DBG", "Item added.");
    }

    public void updateQuantity(String itemName, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ITEM_NAME_COL, itemName);
        values.put(QUANTITY_COL, quantity);

        db.execSQL("UPDATE " + TABLE_TWO + " SET quantity = '" + quantity + "' WHERE item_name = '" + itemName + "';");

        if (quantity < 2) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("5556", null, itemName + " stock is getting low.", null, null);
        }
    }

    public void deleteEntry(String itemName) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_TWO, "item_name = ?", new String[]{itemName});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TWO);
        onCreate(db);
    }

    public String validateUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT username, password FROM accounts WHERE username = '" + username + "';", null);
        if (c.moveToFirst()) {
            return c.getString(1);
        }
        c.close();
        db.close();
        return "";
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM inventory", null);
    }
}
