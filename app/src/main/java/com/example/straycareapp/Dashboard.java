package com.example.straycareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class Dashboard extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Objects.requireNonNull(getSupportActionBar())
                .setBackgroundDrawable(getDrawable(R.color.taskbar));



        /** Assigning variables with input fields Ids*/
        Button registration =findViewById(R.id.registrationBtn);
        Button donate =findViewById(R.id.contact);

        donate.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://sanjanavarshney01.github.io/Stray_Care_Website/#pricing");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        /** registration button to open registration page*/
        registration.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),Registration.class);
            startActivity(intent);
        });

    }

    /**methods to show menu in action*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAdmin:
//                Toast.makeText(getApplicationContext(), "Admin pressed", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),AdminLogin.class);
                startActivity(intent);
                break;
            case R.id.menuSupport:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","strayCare@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            case R.id.aboutDev:
                String url = "https://sanjanavarshney01.github.io/Stray_Care_Website/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
        }
                return true;
    }
}