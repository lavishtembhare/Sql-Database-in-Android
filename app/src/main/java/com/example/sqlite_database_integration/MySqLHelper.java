package com.example.sqlite_database_integration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MySqLHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ContactDb";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CONTACT = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE_NO = "phone_no";

    public MySqLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_CONTACT + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " TEXT, "
                + KEY_PHONE_NO + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        onCreate(db);
    }

    public void addContact(String name, String phone_no) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_PHONE_NO, phone_no);

        db.insert(TABLE_CONTACT, null, values);
        db.close(); // ✅ Close DB after write operation
    }
    public ArrayList<ContactModel> fetchContact(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_CONTACT, null);
        ArrayList<ContactModel> arrContact=new ArrayList<>();
        while (cursor.moveToNext()){
            ContactModel model=new ContactModel();
            model.id=cursor.getInt(0);
            model.name=cursor.getString(1);
            model.phone_no=cursor.getString(2);
            arrContact.add(model);
        }
        return arrContact;
    }
}
