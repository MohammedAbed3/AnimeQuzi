package com.tegogames.animequzi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tegogames.animequzi.Adapters.CategoryAdapter;
import com.tegogames.animequzi.Modles.CategoryModel;
import com.tegogames.animequzi.databinding.ActivityMainBinding;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    ArrayList<CategoryModel> list;
    CategoryAdapter adapter;

    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        list = new ArrayList<>();

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);

        if (loadingDialog.getWindow()!= null){
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadingDialog.setCancelable(false);
        }
        loadingDialog.show();

        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        binding.recyclerCategory.setLayoutManager(layoutManager);

        adapter = new CategoryAdapter(this,list);

        binding.recyclerCategory.setAdapter(adapter);

        database.getReference().child("categories").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Object valueFromDatabase = snapshot.child("setNum").getValue();
                Object valueFromDatabas;

                if (snapshot.exists()){
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.child("categoryName").getValue() != null &&
                                dataSnapshot.child("categoryImage").getValue() != null &&
                                dataSnapshot.child("setNum").getValue() != null) {

                            String categoryName = dataSnapshot.child("categoryName").getValue().toString();
                            String categoryImage = dataSnapshot.child("categoryImage").getValue().toString();
                            String key = dataSnapshot.getKey();

                            int intValue = 0;  // القيمة الافتراضية
                            valueFromDatabase = dataSnapshot.child("setNum").getValue();

                            if (valueFromDatabase != null) {
                                try {
                                    intValue = Integer.parseInt(valueFromDatabase.toString());
                                } catch (NumberFormatException e) {
                                    // يمكنك تنفيذ إجراءات خاصة هنا إذا فشل التحويل
                                }
                            }

                            list.add(new CategoryModel(categoryName, categoryImage, key, intValue));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    loadingDialog.dismiss();

                } else {
                    Toast.makeText(MainActivity.this, "No categories", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}