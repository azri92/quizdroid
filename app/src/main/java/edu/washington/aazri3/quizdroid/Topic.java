package edu.washington.aazri3.quizdroid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain model that contains the title, icon, short description, long description,
 * and list of questions.
 */

public class Topic implements Serializable {
    private String title;
    private String icon;
    private String shortDesc;
    private String longDesr;
    private List<Question> questions;

    public Topic(String title) {
        this.title = title;
        questions = new ArrayList<>();
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public void setLongDesr(String longDesr) {
        this.longDesr = longDesr;
    }

    public void addQuestion(String question, int answer, String[] option) {
        this.questions.add(new Question(question, answer, option));
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getLongDesr() {
        return longDesr;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Question question(int i) {
        return questions.get(i);
    }
}
