package edu.washington.aazri3.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;


public class AnswerActivity extends ActionBarActivity {
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        final Quiz quiz = (Quiz) getIntent().getSerializableExtra("quiz");
        int chosenAnswer = getIntent().getIntExtra("chosenAnswer", 0);

        TextView txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        int currentQuestion = quiz.currentQuestion();
        setTitle("Answer " + (currentQuestion + 1));
        txtQuestion.setText(quiz.getQuestion(currentQuestion));

        String[] options = quiz.getOptions(currentQuestion);

        for (int i = 0; i < options.length; i++) {
            String id = "radio" + i;
            int radioID = getResources().getIdentifier(id, "id", getPackageName());
            RadioButton radioButton = (RadioButton) findViewById(radioID);
            radioButton.setText(options[i]);
            if (i == chosenAnswer) {
                radioButton.setChecked(true);
            } else {
                radioButton.setEnabled(false);
            }
        }

        TextView score = (TextView) findViewById(R.id.score);
        score.setText("You have " + quiz.getCorrectAnswers() + " out of " + quiz.numberOfQuestions() +
            " correct.");
        score.setVisibility(View.VISIBLE);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        if (quiz.isLastQuestion()) {
            btnSubmit.setText("FINISH");
        } else {
            btnSubmit.setText("NEXT");
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!quiz.isLastQuestion()) {
                    quiz.nextQuestion();
                    Intent next = new Intent(getApplicationContext(), QuestionActivity.class);
                    next.putExtra("quiz", quiz);
                    startActivity(next);
                } else {
                    Intent next = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(next);
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_answer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
