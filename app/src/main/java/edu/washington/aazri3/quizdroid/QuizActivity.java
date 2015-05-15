package edu.washington.aazri3.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


public class QuizActivity extends ActionBarActivity implements QuizFragmentInterface {

    private int chosenAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            OverviewFragment overviewFragment = new OverviewFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            overviewFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, overviewFragment).commit();
            setTitle("Overview");
        }
    }

    // Interface implementation
    public void onButtonClicked(String type) {
        switch (type){
            case "begin":
                QuestionFragment questionFragment = new QuestionFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.push_up_in, 0, 0, R.anim.push_up_out)
                        .replace(R.id.fragment_container, questionFragment).commit();
                break;
            case "submit":
                AnswerFragment answerFragment = new AnswerFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, 0, 0, R.anim.slide_out_left)
                        .replace(R.id.fragment_container, answerFragment).commit();
                break;
            case "next":
                questionFragment = new QuestionFragment();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.push_up_in, 0, 0, R.anim.push_up_out)
                        .replace(R.id.fragment_container, questionFragment).commit();
                break;
            case "finish":
                Intent next = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(next);
                finish();
                break;
        }
    }

    // Interface implementation
    public void updateChosenAnswer(int answer) {
        chosenAnswer = answer;
    }

    // Interface implementation
    public int getChosenAnswer() {
        return chosenAnswer;
    }

    @Override
    public void onBackPressed() {
        return;
    }


}