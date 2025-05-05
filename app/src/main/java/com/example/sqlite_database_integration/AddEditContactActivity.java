package com.example.sqlite_database_integration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;



public class AddEditContactActivity extends AppCompatActivity {
    EditText etName, etPhone;
    Button btnSave;
    DatabaseHelper db;
    int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        btnSave = findViewById(R.id.btnSave);
        db = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1);
            etName.setText(intent.getStringExtra("name"));
            etPhone.setText(intent.getStringExtra("phone"));
            btnSave.setText("Update");
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String phone = etPhone.getText().toString();
            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (id == -1) {
                db.addContact(name, phone);
                Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
            } else {
                db.updateContact(id, name, phone);
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }
}
