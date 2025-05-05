package com.example.sqlite_database_integration;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class MainActivity extends AppCompatActivity implements ContactAdapter.OnContactItemClickListener {
    private static final String TAG = "MainActivity";

    private EditText edtName, edtPhone, edtEmail, edtContactId;
    private Button btnAdd, btnUpdate, btnDelete;
    private RecyclerView recyclerContacts;
    private ContactAdapter adapter;
    private ArrayList<ContactModel> contactList;
    private MySqLHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initViews();

        // Initialize database helper
        dbHelper = MySqLHelper.getInstance(this);
        if (dbHelper == null) {
            Log.e(TAG, "Database helper is not initialized.");
            return;
        }

        // Setup recycler view
        setupRecyclerView();

        // Load all contacts
        refreshContactList();

        // Setup button click listeners
        setupClickListeners();
    }

    private void initViews() {
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtContactId = findViewById(R.id.edtContactId);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        recyclerContacts = findViewById(R.id.recyclerContacts);
    }

    private void setupRecyclerView() {
        contactList = new ArrayList<>();
        adapter = new ContactAdapter(this, contactList, this);
        recyclerContacts.setLayoutManager(new LinearLayoutManager(this));
        recyclerContacts.setAdapter(adapter);
    }

    private void setupClickListeners() {
        // Add contact
        btnAdd.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String phoneNo = edtPhone.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();

            if (validateInput(name, phoneNo)) {
                ContactModel newContact = new ContactModel(name, phoneNo);
                if (!TextUtils.isEmpty(email)) {
                    newContact.setEmail(email);
                }

                long id = dbHelper.addContact(newContact);
                if (id != -1) {
                    Toast.makeText(MainActivity.this, "Contact added successfully", Toast.LENGTH_SHORT).show();
                    clearInputFields();
                    refreshContactList();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to add contact", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to add contact to the database");
                }
            } else {
                Toast.makeText(MainActivity.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
            }
        });

        // Update contact
        btnUpdate.setOnClickListener(v -> {
            String idStr = edtContactId.getText().toString().trim();
            String name = edtName.getText().toString().trim();
            String phoneNo = edtPhone.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();

            if (TextUtils.isEmpty(idStr)) {
                Toast.makeText(MainActivity.this, "Please enter a contact ID", Toast.LENGTH_SHORT).show();
                return;
            }

            int id = Integer.parseInt(idStr);

            if (validateInput(name, phoneNo)) {
                if (dbHelper.contactExists(id)) {
                    ContactModel model = new ContactModel(id, name, phoneNo);
                    if (!TextUtils.isEmpty(email)) {
                        model.setEmail(email);
                    }

                    int rowsUpdated = dbHelper.updateContact(model);
                    if (rowsUpdated > 0) {
                        Toast.makeText(MainActivity.this, "Contact updated successfully", Toast.LENGTH_SHORT).show();
                        clearInputFields();
                        refreshContactList();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to update contact", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Failed to update contact in the database");
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Contact with ID " + id + " does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Delete contact
        btnDelete.setOnClickListener(v -> {
            String idStr = edtContactId.getText().toString().trim();

            if (TextUtils.isEmpty(idStr)) {
                Toast.makeText(MainActivity.this, "Please enter a contact ID", Toast.LENGTH_SHORT).show();
                return;
            }

            int id = Integer.parseInt(idStr);

            if (dbHelper.contactExists(id)) {
                new AlertDialog.Builder(this)
                        .setTitle("Delete Contact")
                        .setMessage("Are you sure you want to delete this contact?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            int rowsDeleted = dbHelper.deleteContact(id);
                            if (rowsDeleted > 0) {
                                Toast.makeText(MainActivity.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                                clearInputFields();
                                refreshContactList();
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to delete contact", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else {
                Toast.makeText(MainActivity.this, "Contact with ID " + id + " does not exist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshContactList() {
        contactList.clear();
        contactList.addAll(dbHelper.getAllContacts());
        adapter.notifyDataSetChanged();
    }

    private void clearInputFields() {
        edtName.setText("");
        edtPhone.setText("");
        edtEmail.setText("");
        edtContactId.setText("");
    }

    private boolean validateInput(String name, String phoneNo) {
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(phoneNo);
    }

    @Override
    public void onContactClick(ContactModel contact) {
        edtContactId.setText(String.valueOf(contact.getId()));
        edtName.setText(contact.getName());
        edtPhone.setText(contact.getPhoneNo());
        edtEmail.setText(contact.getEmail());
    }
}
