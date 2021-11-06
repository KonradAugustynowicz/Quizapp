package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String QUIZ_TAG = "MainActiviti";
    private static final String KET_CURRENT_INDEX="currentIndex";
    public static final String KET_EXTRA_ANSWER="com.company.app.correctAnswer";
    private static final int REQUEST_CODE_PROMPT = 0;

    private int currentIndex =0;
    public boolean answerWasShown;

    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button promptButton;
    private TextView questionTextView;
    private Question[] questions = new Question[]
            {
                    new Question(R.string.q_previous, false),
                    new Question(R.string.q_next, true),
                    new Question(R.string.q_dog, false),
                    new Question(R.string.q_false, false),
                    new Question(R.string.q_true, true),
            };
    private void setNextQuestion()
    {
        questionTextView.setText(questions[currentIndex].getQuestionId());
    }

    private void checkAnswerCorrectness(boolean userAnswer)
    {
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessageId = 0;
        if (answerWasShown)
        {
            resultMessageId = R.string.answer_was_shown;
        }else{
            if (userAnswer == correctAnswer)
            {
                resultMessageId = R.string.correct_answer;
            }else if (userAnswer == !correctAnswer)
            {
                resultMessageId = R.string.incorrect_answer;
            }
        }

        Toast.makeText(this,resultMessageId,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(QUIZ_TAG,"Zostało wykonane onCreate");

        setContentView(R.layout.activity_main);

        if (savedInstanceState != null)
        {
            currentIndex = savedInstanceState.getInt(KET_CURRENT_INDEX);
        }

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        promptButton = findViewById(R.id.prompt_button);
        questionTextView = findViewById(R.id.question_text_view);

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswerCorrectness(true);
            }
        });
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswerCorrectness(false);
            }
        });
        promptButton.setOnClickListener((v) ->{
            Intent intent = new Intent(MainActivity.this,PromptActivity.class);
            boolean correctAnswer=questions[currentIndex].isTrueAnswer();
            intent.putExtra(KET_EXTRA_ANSWER,correctAnswer);
            startActivityForResult(intent,REQUEST_CODE_PROMPT);
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIndex = (currentIndex+1)%questions.length;
                answerWasShown = false;
                setNextQuestion();
            }
        });
        setNextQuestion();
    }


    @Override
    protected void onResume()
    {
        Log.d(QUIZ_TAG,"Zostało wykonane onResume");
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(QUIZ_TAG,"Zostało wykonane onStart");
    }

    @Override
    protected void onStop()
    {
        Log.d(QUIZ_TAG,"Zostało wykonane onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        Log.d(QUIZ_TAG,"Zostało wykonane onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        Log.d(QUIZ_TAG,"Wywołąna została metoda: onSaveInstanceState");
        outState.putInt(KET_CURRENT_INDEX,currentIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_CODE_PROMPT){
            if (data == null) { return;}
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN,false);
        }
    }
}