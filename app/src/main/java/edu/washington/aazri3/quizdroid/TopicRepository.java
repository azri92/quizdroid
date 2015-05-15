package edu.washington.aazri3.quizdroid;

import java.util.List;

/**
 *
 */

public interface TopicRepository  {

    List<String> getTopicList();

    void setTopic(String topic);

    String getTopic();

    String getIcon();

    String getShortDesc();

    String getLongDesc();

    String getQuestion();

    int getAnswer();

    String[] getOptions();

    int currentQuestion();

    void nextQuestion();

    boolean isCorrect(int choosenAnswer);

    void answeredCorrectly();

    int getCorrectAnswers();

    boolean isLastQuestion();

    void reset();
}
