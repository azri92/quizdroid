package edu.washington.aazri3.quizdroid;

import java.io.Serializable;

/**
 * Domain model that contains the question, answer, and options.
 */
public class Question implements Serializable {
    private String question;
    private int answer;
    private String[] options;

    public Question(String question, int answer, String[] options) {
        this.question = question;
        this.answer = answer;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public int getAnswer() {
        return answer;
    }


}
