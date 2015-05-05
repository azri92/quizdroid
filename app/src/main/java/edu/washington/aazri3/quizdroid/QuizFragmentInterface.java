package edu.washington.aazri3.quizdroid;

/**
 * Interface between activity (QuizActivity) & fragments
 */
public interface QuizFragmentInterface {

    void onButtonClicked(String type);

    Quiz getQuiz();

    void updateQuiz(Quiz quiz);

    void updateChosenAnswer(int answer);

    int getChosenAnswer();

}
