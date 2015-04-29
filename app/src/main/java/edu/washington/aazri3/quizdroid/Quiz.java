package edu.washington.aazri3.quizdroid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AzriABA.
 */
public class Quiz implements Serializable {
    private final String topic;
    private String description;
    private List<String> questions;
    private List<Integer> answers;
    private Map<String, String[]> options;
    private int currentQuestion;
    private int correctAnswers;

    public Quiz(String topic) {
        this.topic = topic;
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        options = new HashMap<>();
        switch (topic) {
            case "Math":
                mathInit();
                break;
            case "Physics":
                physicsInit();
                break;
            case "Marvel Super Heroes":
                marvelInit();
                break;
            case "Dota 2":
                dotaInit();
                break;
        }
        endText();
        currentQuestion = 0;
        correctAnswers = 0;
    }

    private void mathInit() {
        description = "Questions involve simple Math that you should be able to calculate " +
                "in your head.";
        addQuestion("123 + 456 =", 2, new String[]{"123456", "569", "579", "654321"});
        addQuestion("100 - -10 =", 0, new String[]{"110", "101", "100", "001"});
        addQuestion("908 + 890 = ", 2, new String[]{"1234", "1900", "1798", "1978"});
    }

    private void physicsInit() {
        description = "Questions involve simple high school Physics concepts, save for some " +
                "special picks ;)";
        addQuestion("Newton's Laws of Motion are all these except..", 3, new String[]{"Law of Inertia",
                        "F = ma", "For every action there is an equal and opposite reaction.",
                        "A student will remain in bed unless acted upon by a large enough panic"});
    }

    private void marvelInit() {
        description = "Questions involve general knowledge on Marvel Super Heroes. Get yourself " +
                "prepared before watching Avengers (if you haven't).";
        addQuestion("What made Stephen Rogers the Captain America?", 2,
                new String[]{"Training", "Talent", "Drugs", "Patriotism"});
    }

    private void dotaInit() {
        description = "If you've played more than 200 hours of Dota 2, you should at least know " +
                "these.";
        addQuestion("What item should you get when you're a carry against a Phantom Assassin?", 1,
                new String[]{"BKB", "MKB", "Branch", "Dagon"});
    }

    private void endText() {
        description += "\n\nNumber of Questions: " + questions.size() +
                "\n\nPress \"BEGIN\" to start. Good luck!";
    }

    private void addQuestion(String question, int answer, String[] option) {
        questions.add(question);
        answers.add(answer);
        options.put(question, option);
    }

    public String getDescription() {
        return description;
    }

    public String getQuestion(int i) {
        return questions.get(i);
    }

    public int getAnswer(int i) {
        return answers.get(i);
    }

    public String[] getOptions(int i) {
        return options.get(questions.get(i));
    }

    public int numberOfQuestions() {
        return questions.size();
    }

    public int currentQuestion() {
        return currentQuestion;
    }

    public void nextQuestion() {
        currentQuestion++;
    }

    public boolean isCorrect(int chosenAnswer) {
        return answers.get(currentQuestion) == chosenAnswer;
    }

    public void answeredCorrectly() {
        correctAnswers++;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public boolean isLastQuestion() {
        return currentQuestion == (questions.size() - 1);
    }
}
