package edu.washington.aazri3.quizdroid;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class AnswerFragment extends Fragment {

    private QuizFragmentInterface mCallback;
    private Button btnSubmit;

    public AnswerFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Quiz quiz = mCallback.getQuiz();
        int chosenAnswer = mCallback.getChosenAnswer();

        TextView txtQuestion = (TextView) getActivity().findViewById(R.id.txtQuestion);
        int currentQuestion = quiz.currentQuestion();
        getActivity().setTitle("Answer " + (currentQuestion + 1));
        txtQuestion.setText(quiz.getQuestion(currentQuestion));

        String[] options = quiz.getOptions(currentQuestion);

        for (int i = 0; i < options.length; i++) {
            String id = "radio" + i;
            int radioID = getResources().getIdentifier(id, "id", getActivity().getPackageName());
            RadioButton radioButton = (RadioButton) getActivity().findViewById(radioID);
            radioButton.setText(options[i]);
            if (i == chosenAnswer) {
                radioButton.setChecked(true);
                if (i == quiz.getAnswer(currentQuestion)) {
                    radioButton.setBackgroundColor(Color.GREEN);
                }
            } else {
                radioButton.setEnabled(false);
                if (i == quiz.getAnswer(currentQuestion)) {
                    radioButton.setBackgroundColor(Color.RED);
                }
            }
        }

        TextView score = (TextView) getActivity().findViewById(R.id.score);
        score.setText("You have " + quiz.getCorrectAnswers() + " out of " + (currentQuestion + 1) +
                " correct.");
        score.setVisibility(View.VISIBLE);

        btnSubmit = (Button) getActivity().findViewById(R.id.btnSubmit);
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
                    mCallback.updateQuiz(quiz);
                    mCallback.onButtonClicked("next");
                } else {
                    mCallback.onButtonClicked("finish");
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mCallback = (QuizFragmentInterface) activity;
    }
}
