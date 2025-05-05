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
    private static final String KEY_EMAIL = "email"; // New field
    private static final String KEY_DATE_CREATED = "date_created"; // New field

    private static final String TAG = "MySqLHelper"; // For logging

    // Singleton instance
    private static MySqLHelper instance;

    // Get singleton instance
    public static synchronized MySqLHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MySqLHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Private constructor to prevent direct instantiation
    private MySqLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_CONTACT + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " TEXT NOT NULL, "
                + KEY_PHONE_NO + " TEXT NOT NULL, "
                + KEY_EMAIL + " TEXT, "
                + KEY_DATE_CREATED + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_TABLE);

        // Create index on name for faster searches
        db.execSQL("CREATE INDEX idx_contact_name ON " + TABLE_CONTACT + "(" + KEY_NAME + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add email column if upgrading from version 1
            db.execSQL("ALTER TABLE " + TABLE_CONTACT + " ADD COLUMN " + KEY_EMAIL + " TEXT");
        }

        if (oldVersion < 3) {
            // Add date_created column if upgrading from version 2
            db.execSQL("ALTER TABLE " + TABLE_CONTACT + " ADD COLUMN " + KEY_DATE_CREATED +
                    " TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // Enable foreign key constraints if needed in the future
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    /**
     * Add a new contact to the database
     * @param contactModel The contact model containing data to insert
     * @return Row ID of the newly inserted contact, or -1 if an error occurred
     */
    public long addContact(ContactModel contactModel) {
        SQLiteDatabase db = null;
        long result = -1;
        try {
            db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_NAME, contactModel.getName());
            values.put(KEY_PHONE_NO, contactModel.getPhoneNo());
            if (contactModel.getEmail() != null) {
                values.put(KEY_EMAIL, contactModel.getEmail());
            }

            result = db.insert(TABLE_CONTACT, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Error adding contact: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return result;
    }

    /**
     * Add a new contact to the database using name and phone number
     * @param name The contact name
     * @param phone_no The contact phone number
     * @return Row ID of the newly inserted contact, or -1 if an error occurred
     */
    public long addContact(String name, String phone_no) {
        ContactModel model = new ContactModel(name, phone_no);
        return addContact(model);
    }

    /**
     * Add a new contact to the database with email
     * @param name The contact name
     * @param phone_no The contact phone number
     * @param email The contact email
     * @return Row ID of the newly inserted contact, or -1 if an error occurred
     */
    public long addContact(String name, String phone_no, String email) {
        ContactModel model = new ContactModel(name, phone_no);
        model.setEmail(email);
        return addContact(model);
    }

    /**
     * Fetch all contacts from the database
     * @return ArrayList of ContactModel objects
     */
    public ArrayList<ContactModel> fetchAllContacts() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<ContactModel> arrContact = new ArrayList<>();

        try {
            db = this.getReadableDatabase();
            cursor = db.query(TABLE_CONTACT, null, null, null, null, null, KEY_NAME + " ASC");

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(KEY_ID);
                int nameIndex = cursor.getColumnIndex(KEY_NAME);
                int phoneIndex = cursor.getColumnIndex(KEY_PHONE_NO);
                int emailIndex = cursor.getColumnIndex(KEY_EMAIL);

                do {
                    ContactModel model = new ContactModel();
                    model.setId(cursor.getInt(idIndex));
                    model.setName(cursor.getString(nameIndex));
                    model.setPhoneNo(cursor.getString(phoneIndex));

                    if (emailIndex != -1) {
                        model.setEmail(cursor.getString(emailIndex));
                    }

                    arrContact.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching contacts: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return arrContact;
    }

    /**
     * Fetch contacts with optional search criteria
     * @return ArrayList of ContactModel objects
     */
    public ArrayList<ContactModel> fetchContact(String searchQuery) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<ContactModel> arrContact = new ArrayList<>();

        try {
            db = this.getReadableDatabase();
            String selection = null;
            String[] selectionArgs = null;

            if (searchQuery != null && !searchQuery.isEmpty()) {
                selection = KEY_NAME + " LIKE ? OR " + KEY_PHONE_NO + " LIKE ?";
                selectionArgs = new String[]{"%" + searchQuery + "%", "%" + searchQuery + "%"};
            }

            cursor = db.query(TABLE_CONTACT, null, selection, selectionArgs,
                    null, null, KEY_NAME + " ASC");

            int idIndex = cursor.getColumnIndex(KEY_ID);
            int nameIndex = cursor.getColumnIndex(KEY_NAME);
            int phoneIndex = cursor.getColumnIndex(KEY_PHONE_NO);
            int emailIndex = cursor.getColumnIndex(KEY_EMAIL);

            while (cursor.moveToNext()) {
                ContactModel model = new ContactModel();
                model.setId(cursor.getInt(idIndex));
                model.setName(cursor.getString(nameIndex));
                model.setPhoneNo(cursor.getString(phoneIndex));

                if (emailIndex != -1) {
                    model.setEmail(cursor.getString(emailIndex));
                }

                arrContact.add(model);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching contacts: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return arrContact;
    }

    /**
     * Fetch all contacts method for backward compatibility
     */
    public ArrayList<ContactModel> fetchContact() {
        return fetchAllContacts();
    }

    /**
     * Get a single contact by ID
     * @param id The contact ID
     * @return ContactModel object if found, null otherwise
     */
    public ContactModel getContact(int id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ContactModel model = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(TABLE_CONTACT, null, KEY_ID + " = ?",
                    new String[]{String.valueOf(id)}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(KEY_ID);
                int nameIndex = cursor.getColumnIndex(KEY_NAME);
                int phoneIndex = cursor.getColumnIndex(KEY_PHONE_NO);
                int emailIndex = cursor.getColumnIndex(KEY_EMAIL);

                model = new ContactModel();
                model.setId(cursor.getInt(idIndex));
                model.setName(cursor.getString(nameIndex));
                model.setPhoneNo(cursor.getString(phoneIndex));

                if (emailIndex != -1) {
                    model.setEmail(cursor.getString(emailIndex));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting contact: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return model;
    }

    /**
     * Update an existing contact
     * @param contactModel The contact model containing updated data
     * @return Number of rows affected
     */
    public int updateContact(ContactModel contactModel) {
        SQLiteDatabase db = null;
        int rowsAffected = 0;

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, contactModel.getName());
            values.put(KEY_PHONE_NO, contactModel.getPhoneNo());

            if (contactModel.getEmail() != null) {
                values.put(KEY_EMAIL, contactModel.getEmail());
            }

            rowsAffected = db.update(TABLE_CONTACT, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(contactModel.getId())});
        } catch (Exception e) {
            Log.e(TAG, "Error updating contact: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return rowsAffected;
    }

    /**
     * Delete a contact by ID
     * @param id The contact ID to delete
     * @return Number of rows affected
     */
    public int deleteContact(int id) {
        SQLiteDatabase db = null;
        int rowsAffected = 0;

        try {
            db = this.getWritableDatabase();
            rowsAffected = db.delete(TABLE_CONTACT, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.e(TAG, "Error deleting contact: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return rowsAffected;
    }

    /**
     * Delete all contacts
     * @return Number of rows affected
     */
    public int deleteAllContacts() {
        SQLiteDatabase db = null;
        int rowsAffected = 0;

        try {
            db = this.getWritableDatabase();
            rowsAffected = db.delete(TABLE_CONTACT, null, null);
        } catch (Exception e) {
            Log.e(TAG, "Error deleting all contacts: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return rowsAffected;
    }

    /**
     * Check if a contact exists by ID
     * @param id The contact ID to check
     * @return true if contact exists, false otherwise
     */
    public boolean contactExists(int id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        boolean exists = false;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(TABLE_CONTACT, new String[]{KEY_ID},
                    KEY_ID + " = ?", new String[]{String.valueOf(id)},
                    null, null, null);
            exists = cursor != null && cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error checking if contact exists: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return exists;
    }

    /**
     * Get the count of all contacts in the database
     * @return Number of contacts
     */
    public int getContactsCount() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        int count = 0;

        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CONTACT, null);

            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting contacts count: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return count;
    }
    public ArrayList<ContactModel> getAllContacts() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<ContactModel> contactList = new ArrayList<>();

        try {
            db = this.getReadableDatabase();
            cursor = db.query(TABLE_CONTACT, null, null, null, null, null, KEY_NAME + " ASC"); // Retrieve all contacts, ordered by name

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(KEY_ID);
                int nameIndex = cursor.getColumnIndex(KEY_NAME);
                int phoneIndex = cursor.getColumnIndex(KEY_PHONE_NO);
                int emailIndex = cursor.getColumnIndex(KEY_EMAIL);

                do {
                    ContactModel contact = new ContactModel();
                    contact.setId(cursor.getInt(idIndex));
                    contact.setName(cursor.getString(nameIndex));
                    contact.setPhoneNo(cursor.getString(phoneIndex));

                    if (emailIndex != -1) {
                        contact.setEmail(cursor.getString(emailIndex));  // Set email if present
                    }

                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching all contacts: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return contactList; // Return the list of all contacts
    }

}