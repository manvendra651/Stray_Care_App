package com.example.straycareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class AdminLogin extends AppCompatActivity {
    private static final String LOGIN_INFO = "username";
    ConnectivityManager connectivityManager;

    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference imageStorageReference;
    FirebaseAuth auth;

    private EditText userName;
    private EditText pass;
    private Button logInBtn;
    private TextView forgotLink;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        db = FirebaseFirestore.getInstance(); // connection to Firestore

        storage = FirebaseStorage.getInstance();
        imageStorageReference = storage.getReference();
        auth = FirebaseAuth.getInstance();

        userName=findViewById(R.id.loginUser);
        pass=findViewById(R.id.loginPass);
        logInBtn=findViewById(R.id.loginLoginBtn);
        forgotLink=findViewById(R.id.loginForgot);
        error=findViewById(R.id.loginError);

        SharedPreferences getSharedEmail=getSharedPreferences(LOGIN_INFO,MODE_PRIVATE);
        String userEmail=getSharedEmail.getString("username","Enter Email");
//        String loginPass=getSharedEmail.getString("password","");
        userName.setText(userEmail);
//        pass.setText(loginPass);


        logInBtn.setOnClickListener(v -> {
            if (activeNetworkInfo == null) {
                Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_LONG).show();
            }
            String email=userName.getText().toString().trim();
            String password=pass.getText().toString().trim();
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    error.setText("");

                    Toast.makeText(getApplicationContext(),"Logged",Toast.LENGTH_LONG)
                            .show();

                    // Shared Preferences used to save id after login
                    SharedPreferences sharedPreferences=getSharedPreferences(LOGIN_INFO,MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("username",email);
                    editor.putString("password",password);
                    editor.apply();// saving to disk
                    Intent intent=new Intent(getApplicationContext(), RequestList.class);
                    startActivity(intent);
                }else{
                    error.setText("Invalid Username or Password");
                }

            });

        });
    }

    public void resetPass(View v) {
        String user = userName.getText().toString().trim();
        if (user.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Email first", Toast.LENGTH_LONG).show();
        }
        if (!user.isEmpty()) {
            auth.sendPasswordResetEmail(user).addOnSuccessListener(unused ->
                    Toast.makeText(getApplicationContext(), "Password reset mail send to " + user, Toast.LENGTH_LONG)
                    .show()).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Some Error Occurred",Toast.LENGTH_LONG));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent(getApplicationContext(),Dashboard.class);
        startActivity(intent);
        super.onBackPressed();
    }
}