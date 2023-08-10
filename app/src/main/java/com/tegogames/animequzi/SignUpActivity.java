package com.tegogames.animequzi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.tegogames.animequzi.databinding.ActivitySignUpBinding;

import java.util.HashMap;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog dialog;
    ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Creating your Account");
        dialog.setMessage("your Account is Creating!!!");


        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.userEmail.getText().toString();
                String password = binding.password.getText().toString();
                String name = binding.userName.getText().toString();
                dialog.show();
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        dialog.dismiss();
                        if (task.isSuccessful()){
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("userName",name);
                            map.put("userEmail",email);
                            map.put("password",password);
                            String id = task.getResult().getUser().getUid();

                            database.getReference().child("users").child(id).setValue(map);

                            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                            startActivity(intent);

                        }else {
                            Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.alredyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SingInActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}