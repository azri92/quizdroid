package edu.washington.aazri3.quizdroid;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * Display QuizRepo overview.
 */
public class OverviewFragment extends Fragment {

    QuizFragmentInterface mCallback;

    public OverviewFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView txtTopic = (TextView) getActivity().findViewById(R.id.txtTopic);
        txtTopic.setText(QuizApp.getInstance().getRepository().getTopic());

        TextView txtDescription = (TextView) getActivity().findViewById(R.id.txtDescription);
        txtDescription.setText(QuizApp.getInstance().getRepository().getLongDesc());

        Button btnBegin = (Button) getActivity().findViewById(R.id.btnBegin);
        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onButtonClicked("begin");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mCallback = (QuizFragmentInterface) activity;
    }
}
