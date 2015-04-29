package edu.washington.aazri3.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class QuestionActivity extends ActionBarActivity {
    Button btnSubmit;
    private int chosenAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        final Quiz quiz = (Quiz) getIntent().getSerializableExtra("quiz");

        TextView txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        int currentQuestion = quiz.currentQuestion();
        setTitle("Question " + (currentQuestion + 1));
        txtQuestion.setText(quiz.getQuestion(currentQuestion));

        String[] options = quiz.getOptions(currentQuestion);

        for (int i = 0; i < options.length; i++) {
            String id = "radio" + i;
            int radioID = getResources().getIdentifier(id, "id", getPackageName());
            RadioButton radioButton = (RadioButton) findViewById(radioID);
            radioButton.setText(options[i]);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickRadio(view);
                }
            });
        }

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setVisibility(View.GONE);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quiz.isCorrect(chosenAnswer)) {
                    quiz.answeredCorrectly();
                }

                Intent next = new Intent(getApplicationContext(), AnswerActivity.class);
                next.putExtra("quiz", quiz);
                next.putExtra("chosenAnswer", chosenAnswer);
                startActivity(next);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {

        return;
    }

    public void onClickRadio(View view) {
        btnSubmit.setVisibility(View.VISIBLE);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.grpRadio);
        chosenAnswer = radioGroup.indexOfChild(view);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
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
