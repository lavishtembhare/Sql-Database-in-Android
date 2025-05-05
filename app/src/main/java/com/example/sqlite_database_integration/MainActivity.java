package com.example.sqlite_database_integration;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    FloatingActionButton fab;
    DatabaseHelper db;
    ArrayList<Contact> contactList;
    ArrayAdapter<String> adapter;
    ArrayList<String> contactStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        db = new DatabaseHelper(this);
        contactList = new ArrayList<>();
        contactStrings = new ArrayList<>();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditContactActivity.class);
            startActivity(intent);
        });


        listView.setOnItemClickListener((parent, view, position, id) -> {
            Contact contact = contactList.get(position);
            Intent intent = new Intent(MainActivity.this, AddEditContactActivity.class);
            intent.putExtra("id", contact.getId());
            intent.putExtra("name", contact.getName());
            intent.putExtra("phone", contact.getPhone());
            startActivity(intent);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Contact contact = contactList.get(position);
            db.deleteContact(contact.getId());
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            loadContacts();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContacts();
    }

    private void loadContacts() {
        contactList.clear();
        contactStrings.clear();

        Cursor cursor = db.getAllContacts();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String phone = cursor.getString(2);

                Contact contact = new Contact(id, name, phone);
                contactList.add(contact);
                contactStrings.add(name + " - " + phone);
            } while (cursor.moveToNext());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactStrings);
        listView.setAdapter(adapter);
    }
}
