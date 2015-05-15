package edu.washington.aazri3.quizdroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class QuestionFragment extends Fragment {

    private Button btnSubmit;
    private int chosenAnswer;
    private QuizFragmentInterface mCallback;

    public QuestionFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView txtQuestion = (TextView) getActivity().findViewById(R.id.txtQuestion);
        int currentQuestion = QuizApp.getInstance().getRepository().currentQuestion();
        getActivity().setTitle("Question " + (currentQuestion + 1));
        txtQuestion.setText(QuizApp.getInstance().getRepository().getQuestion());

        String[] options = QuizApp.getInstance().getRepository().getOptions();

        for (int i = 0; i < options.length; i++) {
            String id = "radio" + i;
            int radioID = getResources().getIdentifier(id, "id", getActivity().getPackageName());
            RadioButton radioButton = (RadioButton) getActivity().findViewById(radioID);
            radioButton.setText(options[i]);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickRadio(view);
                }
            });
        }

        btnSubmit = (Button) getActivity().findViewById(R.id.btnSubmit);
        btnSubmit.setVisibility(View.GONE);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (QuizApp.getInstance().getRepository().isCorrect(chosenAnswer)) {
                    QuizApp.getInstance().getRepository().answeredCorrectly();
                }

                mCallback.updateChosenAnswer(chosenAnswer);
                mCallback.onButtonClicked("submit");
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

    public void onClickRadio(View view) {
        btnSubmit.setVisibility(View.VISIBLE);
        RadioGroup radioGroup = (RadioGroup) getActivity().findViewById(R.id.grpRadio);
        chosenAnswer = radioGroup.indexOfChild(view);
    }
}
