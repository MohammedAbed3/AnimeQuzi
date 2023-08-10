package com.tegogames.animequzi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tegogames.animequzi.databinding.ActivityScoreBinding;

public class ScoreActivity extends AppCompatActivity {


    ActivityScoreBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int correct = getIntent().getIntExtra("correctAnswer",0);
        int totalQuestion = getIntent().getIntExtra("totalQuestion",0);

        int wrong = totalQuestion - correct;
        binding.totalRight.setText(String.valueOf(correct));
        binding.totalWrong.setText(String.valueOf(wrong));
        binding.totalQuestion.setText(String.valueOf(totalQuestion));

        binding.progresBar.setProgress(totalQuestion);
        binding.progresBar.setProgress(correct);
        binding.progresBar.setProgressMax(totalQuestion);

        binding.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        binding.btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}