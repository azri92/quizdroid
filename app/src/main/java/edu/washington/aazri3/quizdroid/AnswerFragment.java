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
 * Handles the answer view of quiz.
 */
public class AnswerFragment extends Fragment {

    private QuizFragmentInterface mCallback;

    public AnswerFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int chosenAnswer = mCallback.getChosenAnswer();

        TextView txtQuestion = (TextView) getActivity().findViewById(R.id.txtQuestion);
        int currentQuestion = QuizApp.getInstance().getRepository().currentQuestion();
        getActivity().setTitle("Answer " + (currentQuestion + 1));
        txtQuestion.setText(QuizApp.getInstance().getRepository().getQuestion());

        String[] options = QuizApp.getInstance().getRepository().getOptions();

        for (int i = 0; i < options.length; i++) {
            String id = "radio" + i;
            int radioID = getResources().getIdentifier(id, "id", getActivity().getPackageName());
            RadioButton radioButton = (RadioButton) getActivity().findViewById(radioID);
            radioButton.setText(options[i]);
            if (i == chosenAnswer) {
                radioButton.setChecked(true);
                if (i == QuizApp.getInstance().getRepository().getAnswer()) {
                    radioButton.setBackgroundColor(Color.GREEN);
                }
            } else {
                radioButton.setEnabled(false);
                if (i == QuizApp.getInstance().getRepository().getAnswer()) {
                    radioButton.setBackgroundColor(Color.RED);
                }
            }
        }

        TextView score = (TextView) getActivity().findViewById(R.id.score);
        score.setText("You have " + QuizApp.getInstance().getRepository().getCorrectAnswers() +
                " out of " + (currentQuestion + 1) + " correct.");
        score.setVisibility(View.VISIBLE);

        Button btnSubmit = (Button) getActivity().findViewById(R.id.btnSubmit);
        if (QuizApp.getInstance().getRepository().isLastQuestion()) {
            btnSubmit.setText("FINISH");
        } else {
            btnSubmit.setText("NEXT");
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!QuizApp.getInstance().getRepository().isLastQuestion()) {
                    QuizApp.getInstance().getRepository().nextQuestion();
                    mCallback.onButtonClicked("next");
                } else {
                    QuizApp.getInstance().getRepository().reset();
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
