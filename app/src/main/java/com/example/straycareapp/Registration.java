package com.example.straycareapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.firebase.FirebaseException;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class Registration extends AppCompatActivity {

    EditText enterOTPBox;
    ImageView animalImage;
    String[] genderArr = {"Male", "Female", "Unknown"};
    String[] conditionArr = {"Normal", "Mild", "Severe"};
    String[] animalTypeArr = {"Dog","Cat", "Cow", "Buffalo", "Bull", "Donkey", "Ox", "Horse", "Elephant", "Pig", "Sheep", "goat"};
    /***/
    FirebaseFirestore db;
    CollectionReference collectionReference;
    FirebaseStorage storage;
    StorageReference imageStorageReference;
    FirebaseAuth auth;

    /**
     * Views which hide and unhide on certain condition
     */
    LinearLayout senderNameLayout;
    LinearLayout phoneNumberLayout;
    LinearLayout enterLayout;
    Button validateOTP;
    Button sendOTP;

    /**
     * Private Useful fileds
     */
    private Uri imageUri;
    private String currentPhotoPath;
    private String OTPByFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //progress bar
        ProgressBar progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        /** connecting to firebase different services*/
        db = FirebaseFirestore.getInstance(); // connection to Firestore
        collectionReference = db.collection("Requests");
        storage = FirebaseStorage.getInstance();
        imageStorageReference = storage.getReference();
        auth = FirebaseAuth.getInstance();

        /**Buttons*/
        Button captureImage = findViewById(R.id.imageCaptureBtn);
        sendOTP = findViewById(R.id.sendOTPBtn);
        validateOTP = findViewById(R.id.validateOTPBtn);

        senderNameLayout = findViewById(R.id.layout1);
        phoneNumberLayout = findViewById(R.id.layout2);
        enterLayout = findViewById(R.id.layout3);

        /** EditTexts*/
        AutoCompleteTextView animalType = findViewById(R.id.animalType);
        EditText description = findViewById(R.id.descriptionText);
        EditText address = findViewById(R.id.address);
        EditText city = findViewById(R.id.city);
        EditText senderName = findViewById(R.id.senderName);
        EditText phoneNumber = findViewById(R.id.phoneNumber);
        enterOTPBox = findViewById(R.id.enterOTPBox);

        /** Imageview*/
        animalImage = findViewById(R.id.AnimalImage);

        /**Spinners*/
        Spinner gender = findViewById(R.id.genderSpinner);
        Spinner condition = findViewById(R.id.conditionSpinner);

        /** Initiating and Setting adapters to Spinners*/
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, genderArr);
        gender.setAdapter(genderAdapter);

        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, conditionArr);
        condition.setAdapter(conditionAdapter);

        ArrayAdapter<String> animalTypeAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, animalTypeArr);
        animalType.setAdapter(animalTypeAdapter);

        /** Hiding Views to unhide them during specific condition*/
        animalImage.setVisibility(View.GONE);
        sendOTP.setVisibility(View.GONE);
        senderNameLayout.setVisibility(View.INVISIBLE);
        phoneNumberLayout.setVisibility(View.INVISIBLE);
        enterLayout.setVisibility(View.INVISIBLE);
        validateOTP.setVisibility(View.GONE);



        /** Button to Capture Image*/
        captureImage.setOnClickListener(v -> {
            if (TextUtils.isEmpty(animalType.getText().toString().trim())) {
                animalType.setError("Field can't be Empty");
            } else if (TextUtils.isEmpty(description.getText().toString().trim())) {
                description.setError("Field can't be Empty");
            } else if (TextUtils.isEmpty(address.getText().toString().trim())) {
                address.setError("Field can't be Empty");
            } else if (TextUtils.isEmpty(city.getText().toString().trim())) {
                city.setError("Field can't be Empty");
            } else {
                senderNameLayout.setVisibility(View.VISIBLE);
                phoneNumberLayout.setVisibility(View.VISIBLE);
                sendOTP.setVisibility(View.VISIBLE);
                String fileName = "animalPhoto";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);

                    currentPhotoPath = imageFile.getAbsolutePath();

                    imageUri = FileProvider.getUriForFile(this, "com.example.straycareapp.fileprovider", imageFile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    animalImage.setVisibility(View.VISIBLE);
                    startActivityForResult(intent, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        /** Button to Send OTP */
        sendOTP.setOnClickListener(v -> {
            if (TextUtils.isEmpty(senderName.getText().toString())) {
                senderName.setError("Name Can't be Empty");
            } else if (phoneNumber.getText().toString().trim().length() != 10) {
                phoneNumber.setError("Input Valid Phone number");
            } else {
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber("+91" + phoneNumber.getText().toString().trim())       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(this)                 // Activity (for callback binding)
                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                                        Toast.makeText(getApplicationContext(), "Failed! Some error occurred" + e.getMessage(), Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        OTPByFirebase = s;
                                        super.onCodeSent(s, forceResendingToken);
                                        Toast.makeText(getApplicationContext(), "OTP Sent Successfully", Toast.LENGTH_LONG).show();
                                        enterLayout.setVisibility(View.VISIBLE);
                                        validateOTP.setVisibility(View.VISIBLE);
                                        sendOTP.setText("Resend OTP");
                                        sendOTP.setTextSize(13);
                                    }
                                })
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }

        });

        /** verify OTP Btn*/
        validateOTP.setOnClickListener(v -> {
            if(TextUtils.isEmpty(enterOTPBox.getText().toString())){
                enterOTPBox.setError("Enter OTP First");
            }else {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTPByFirebase, enterOTPBox.getText().toString());
                auth.signInWithCredential(credential)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                captureImage.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(getApplicationContext(), "OTP Verified", Toast.LENGTH_LONG).show();
                                // Setting data from fields to object
                                String AnimalType = animalType.getText().toString().trim();
                                String Gender = gender.getSelectedItem().toString();
                                String Condition = condition.getSelectedItem().toString();
                                String Description = description.getText().toString().trim();
                                String Address = address.getText().toString().trim();
                                String City = city.getText().toString().trim();
                                String SenderName = senderName.getText().toString().trim();
                                String Phone = phoneNumber.getText().toString().trim();

                                DetailModel obj = new DetailModel();

                                String filename = "image" + Timestamp.now().getSeconds();
                                imageStorageReference.child("Requests")
                                        .child(filename)
                                        .putFile(imageUri).addOnSuccessListener((task2 -> {
                                    imageStorageReference.child("Requests").child(filename).getDownloadUrl()
                                            .addOnSuccessListener(uri -> {
                                                        String imageUrl = uri.toString();
                                                        obj.setAnimalType(AnimalType);
                                                        obj.setGender(Gender);
                                                        obj.setCondition(Condition);
                                                        obj.setDescription(Description);
                                                        obj.setAddress(Address);
                                                        obj.setCity(City);
                                                        obj.setSenderName(SenderName);
                                                        obj.setPhoneNumber(Phone);
                                                        obj.setImageUri(imageUrl);

                                                        // sending data to database
                                                        db.collection("Requests").add(obj).addOnSuccessListener(s -> {
                                                            Toast.makeText(getApplicationContext(), "Request Sent", Toast.LENGTH_LONG).show();
                                                            Toast.makeText(getApplicationContext(), "Thank You For Helping", Toast.LENGTH_LONG).show();
                                                            progressBar.setVisibility(View.GONE);
                                                            finish();
                                                        }).addOnFailureListener(e -> {
                                                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                                                        }).addOnFailureListener(e -> {
                                                            Toast.makeText(getApplicationContext(), "Unable to get Url", Toast.LENGTH_LONG).show();
                                                        });
                                                    }
                                            ).addOnFailureListener(
                                            e -> Toast.makeText(getApplicationContext(), "Some Error Occurred", Toast.LENGTH_LONG).show());
                                }));
                                // Update UI
//                                validateOTP.setVisibility(View.GONE);
//                                enterLayout.setVisibility(View.GONE);
//                                sendOTP.setVisibility(View.GONE);

                            } else {
//                            Toast.makeText(getApplicationContext(), "Failed" + task.getException(), Toast.LENGTH_LONG).show();
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    // The verification code entered was invalid
                                    Toast.makeText(getApplicationContext(), "OTP Incorrect", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            animalImage.setImageURI(imageUri);
            senderNameLayout.setVisibility(View.VISIBLE);
            phoneNumberLayout.setVisibility(View.VISIBLE);
            phoneNumberLayout.setVisibility(View.VISIBLE);

        }
    }
}