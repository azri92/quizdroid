package edu.washington.aazri3.quizdroid;

/**
 * Interface between activity (QuizActivity) & fragments
 */
public interface QuizFragmentInterface {

    void onButtonClicked(String type);

    void updateChosenAnswer(int answer);

    int getChosenAnswer();

}
