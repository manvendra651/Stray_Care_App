package com.example.straycareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class FullDetails extends AppCompatActivity {
    private static final String LOGIN_INFO = "username";
    // getting fireStore firebase database instances
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Requests");

    DetailModel detailModel;

    private TextView type;
    private TextView gender;
    private TextView condition;
    private TextView Description;

    private TextView address;
    private TextView city;
    private TextView name;
    private TextView phone;

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_details);
        Objects.requireNonNull(getSupportActionBar()).hide();

        type = findViewById(R.id.fulDetailstype);
        name = findViewById(R.id.fullDetaillsName);
        Description = findViewById(R.id.fullDetailsDescri);

        address = findViewById(R.id.fullDetailsAdd);
        city = findViewById(R.id.fullDetailsCity);
        name = findViewById(R.id.fullDetaillsName);

        phone = findViewById(R.id.fullDetailsMobile);
        condition=findViewById(R.id.fullDetailsCondition);
        gender=findViewById(R.id.fullDetailsGender);

        image = findViewById(R.id.fullDetailsImage);
//        Button clearDues = findViewById(R.id.fullDetailsClearDues);

        // retrieving ID from list cardView adapter and setting value to the full list page
        String url = getIntent().getStringExtra("imageurl");

        // getting user email from shared preferences
        SharedPreferences getSharedEmail=getSharedPreferences(LOGIN_INFO,MODE_PRIVATE);
        String userEmail=getSharedEmail.getString("username","Enter Email");



        /** setting all fields of a students using his ID */
        collectionReference.whereEqualTo("imageUri", url).get().addOnCompleteListener(task -> {
            if (task.getResult() != null) {
                for (QueryDocumentSnapshot student : task.getResult()) {
                    if(url.equals(student.get("imageUri"))){
                        detailModel = student.toObject(DetailModel.class);
                    }
                }
                //setting values to full details editTexts
                type.setText(detailModel.getAnimalType());
                name.setText(detailModel.getSenderName());
                phone.setText(detailModel.getPhoneNumber());
                Description.setText(detailModel.getDescription());
                condition.setText(detailModel.getCondition());
                gender.setText(detailModel.getGender());
                address.setText(detailModel.getAddress());
                city.setText(detailModel.getCity());
//                if (phone.getText().toString().equals("1700")) {
//                    clearDues.setVisibility(View.INVISIBLE);
//                }
                Picasso.get().load(detailModel.getImageUri()).fit()
                        .placeholder(R.drawable.ic_baseline_image_24).into(image);
            }
        }).addOnFailureListener(e ->
                Toast.makeText(getApplicationContext(), "Failed To Fetch value at Moment", Toast.LENGTH_LONG)
                        .show());
    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent(getApplicationContext(),RequestList.class);
        startActivity(intent);
//        super.onBackPressed();
    }
}