package com.example.straycareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class RequestList extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Requests");

    private EditText searchBar;
    private ImageView searchBtn;
    private ImageView noRecordFound;
    private ArrayList<DetailModel> list;
    private RecyclerView recyclerView;
    private RequestListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        Objects.requireNonNull(getSupportActionBar()).hide();

        searchBar = findViewById(R.id.regiListSearch);
        searchBtn = findViewById(R.id.searchImageBtn);
        noRecordFound=findViewById(R.id.Image_no_record_found);
        noRecordFound.setVisibility(View.GONE);


        list = new ArrayList<>();


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /** getting all the students from database and adding them to list*/
        collectionReference.get().addOnCompleteListener(task -> {
            noRecordFound.setVisibility(View.GONE);
            if (task.getResult() != null) {
                for (QueryDocumentSnapshot obj : task.getResult()) {
                    DetailModel model = obj.toObject(DetailModel.class);
                    list.add(model);
                }
                adapter = new RequestListAdapter(list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Failed to get List", Toast.LENGTH_LONG).show();
        });

        /** method which search student by their name */
        searchBtn.setOnClickListener(v -> {
            noRecordFound.setVisibility(View.GONE);
            ArrayList<DetailModel> filteredList = new ArrayList<>();
            String value = searchBar.getText().toString().trim();
            collectionReference.whereEqualTo("city", value).get().addOnCompleteListener(task -> {
                if (task.getResult() != null) {
                    for (QueryDocumentSnapshot obj : task.getResult()) {
                        DetailModel model = obj.toObject(DetailModel.class);
                        filteredList.add(model);
                    }
                    adapter = new RequestListAdapter(filteredList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                if(filteredList.size()==0){
                    Toast.makeText(getApplicationContext(), "No Record Found", Toast.LENGTH_LONG).show();
                    noRecordFound.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show());
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent(getApplicationContext(),AdminLogin.class);
        startActivity(intent);
        super.onBackPressed();
    }
}