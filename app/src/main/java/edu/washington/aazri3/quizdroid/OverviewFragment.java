package edu.washington.aazri3.quizdroid;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Display quiz overview.
 */
public class OverviewFragment extends Fragment {
    private static final String TAG = "OverviewFragment";

    QuizFragmentInterface mCallback;

    public OverviewFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String topic = QuizApp.getInstance().getRepository().getTopic();

        ImageView imgIcon = (ImageView) getActivity().findViewById(R.id.topicIcon);
        String id = QuizApp.getInstance().getRepository().getIcon(topic);
        int imgID = getResources().getIdentifier(id, "mipmap", getActivity().getPackageName());
        imgIcon.setImageResource(imgID);

        TextView txtTopic = (TextView) getActivity().findViewById(R.id.txtTopic);
        txtTopic.setText(topic);

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
