package com.tegogames.animequzi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.tegogames.animequzi.Adapters.GrideAdapter;
import com.tegogames.animequzi.databinding.ActivitySetsBinding;

public class SetsActivity extends AppCompatActivity {


    ActivitySetsBinding binding;
    FirebaseDatabase database;

    GrideAdapter adapter;

    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        database = FirebaseDatabase.getInstance();
        key = getIntent().getStringExtra("key");

        adapter = new GrideAdapter(getIntent().getIntExtra("sets",0),
                getIntent().getStringExtra("category"));

        binding.gridview.setAdapter(adapter);
    }
}