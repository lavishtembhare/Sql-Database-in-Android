package com.example.sqlite_database_integration;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        MySqLHelper db=new MySqLHelper(this);
//        db.addContact("Lavish","8554019974");
//        db.addContact("Lash","8554019975");
//        db.addContact("Lavh","8554019976");
//        db.addContact("Lavish","8554019977");
        ArrayList<ContactModel> arrContact=db.fetchContact();
        for(int i=0;i<arrContact.size();i++){
            Log.d("Contact_Info","Name"+arrContact.get(i).name+", Phone No: "+arrContact.get(i).phone_no);
        }
    }
}