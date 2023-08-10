package com.tegogames.animequzi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tegogames.animequzi.Modles.QuestionModel;
import com.tegogames.animequzi.databinding.ActivityQuestionBinding;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {


    ActivityQuestionBinding binding;

    private  ArrayList<QuestionModel> list;
    private int count = 0;
    private int postion = 0;
    private int score = 0;
    CountDownTimer timer;
    FirebaseDatabase database;
    String categoryName;
    private int set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        categoryName = getIntent().getStringExtra("categoryName");
        set = getIntent().getIntExtra("setNum",1);

        list = new ArrayList<>();

        resetTimer();
        timer.start();


        database.getReference().child("Sets").child(categoryName).child("questions")
                .orderByChild("setNum")
                .equalTo(set)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {


                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                QuestionModel model = dataSnapshot.getValue(QuestionModel.class);
                                list.add(model);
                            }

                            if (list.size()>0){

                                for (int i =0;i<4;i++){
                                    binding.optionContiner.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            checkAnswer((Button)view);
                                        }
                                    });
                                }

                                playAnimation(binding.question,0,list.get(postion).getQuestion());

                                binding.btnNext.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        binding.btnNext.setEnabled(false);
                                        binding.btnNext.setAlpha(0.3f);

                                        enableOption(true);
                                        postion++;

                                        if (postion ==list.size()){

                                            Intent intent =new Intent(QuestionActivity.this,ScoreActivity.class);
                                            intent.putExtra("correctAnswer",score);
                                            intent.putExtra("totalQuestion",list.size());
                                            startActivity(intent );
                                            finish();
                                            return;
                                        }

                                        count = 0;
                                        playAnimation(binding.question,0,list.get(postion).getQuestion());


                                    }
                                });
                            }else {

                                Toast.makeText(QuestionActivity.this, "Question Not exist", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(QuestionActivity.this, "Question Not exist", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(QuestionActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void resetTimer() {
    timer = new CountDownTimer(30000,1000) {
        @Override
        public void onTick(long l) {

            binding.timer.setText(String.valueOf(l/1000));
        }

        @Override
        public void onFinish() {
            Toast.makeText(QuestionActivity.this, "Time out", Toast.LENGTH_SHORT).show();
        }
    };

    }

    private void enableOption(boolean enable) {

        for (int i = 0 ; i <4;i++){
            binding.optionContiner.getChildAt(i).setEnabled(enable);

        if (enable){
            binding.optionContiner.getChildAt(i).setBackgroundResource(R.drawable.btn_option_back);
        }



        }
    }

    private void playAnimation(View view, int value, String data) {
        if (view != null) {
            view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                    .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(@NonNull Animator animator) {
                            if (value == 0 && score < 4) {
                                String option = "";

                                if (count == 0) {
                                    option = list.get(postion).getOption1();
                                } else if (count == 1) {
                                    option = list.get(postion).getOption2();
                                } else if (count == 2) {
                                    option = list.get(postion).getOption3();
                                } else if (count == 3) {
                                    option = list.get(postion).getOption4();
                                }

                                playAnimation(binding.optionContiner.getChildAt(count), 0, option);
                                count++;
                            }
                        }

                        @Override
                        public void onAnimationEnd(@NonNull Animator animator) {
                            if (value == 0) {
                                try {
                                    if (view instanceof TextView) {
                                        ((TextView) view).setText(data);
                                    }
                                    binding.numIndictor.setText(postion + 1 + "/" + list.size());
                                } catch (Exception e) {
                                    if (view instanceof Button) {
                                        ((Button) view).setText(data);
                                    }
                                }

                                view.setTag(data);
                                playAnimation(view, 1, data);
                            }
                        }

                        @Override
                        public void onAnimationCancel(@NonNull Animator animator) {
                        }

                        @Override
                        public void onAnimationRepeat(@NonNull Animator animator) {
                        }
                    });
        }
    }

    private void checkAnswer(Button selected) {


        enableOption(false);
        binding.btnNext.setEnabled(true);
        binding.btnNext.setAlpha(1f);

        if (selected.getText().toString().equals(list.get(postion).getCorrectAnswer())){

            score++;
            selected.setBackgroundResource(R.drawable.right_answer);
        }else {
            selected.setBackgroundResource(R.drawable.wrong_answer);

            Button correctOption =(Button)  binding.optionContiner.findViewWithTag(list.get(postion).getCorrectAnswer());
            correctOption.setBackgroundResource(R.drawable.right_answer);

        }

    }
}