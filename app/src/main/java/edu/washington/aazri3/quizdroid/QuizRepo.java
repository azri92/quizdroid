package edu.washington.aazri3.quizdroid;

import java.util.ArrayList;
import java.util.List;

/**
 * Repo that contains a list of topics to choose from and Topic object based
 * on chosen topic.
 * Also tracks current question and number of correct answers.
 */
public class QuizRepo implements TopicRepository {

    private static QuizRepo instance = null;

    private List<String> topicList = new ArrayList<String>() {{
        add("Math");
        add("Physics");
        add("Marvel Super Heroes");
        add("Dota 2");
    }};
    private Topic topic;
    private int currentQuestion = 0;
    private int correctAnswers = 0;

    protected QuizRepo() {
        // Exists only to defeat instantiation.
    }

    public static QuizRepo getInstance() {
        if(instance == null) {
            instance = new QuizRepo();
        }
        return instance;
    }

    public void setTopic(String topic) {
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
    }

    public List<String> getTopicList() {
        return topicList;
    }

    public String getTopic() {
        return topic.getTitle();
    }

    public String getIcon() {
        return topic.getIcon();
    }

    public String getShortDesc() {
        return topic.getLongDesr();
    }

    public String getLongDesc() {
        return topic.getLongDesr();
    }

    public String getQuestion() {
        return topic.question(currentQuestion).getQuestion();
    }

    public int getAnswer() {
        return topic.question(currentQuestion).getAnswer();
    }

    public String[] getOptions() {
        return topic.question(currentQuestion).getOptions();
    }

    public int currentQuestion() {
        return currentQuestion;
    }

    public void nextQuestion() {
        currentQuestion++;
    }

    public boolean isCorrect(int chosenAnswer) {
        return topic.question(currentQuestion).getAnswer() == chosenAnswer;
    }

    public void answeredCorrectly() {
        correctAnswers++;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public boolean isLastQuestion() {
        return currentQuestion == (topic.getQuestions().size() - 1);
    }

    public void reset() {
        currentQuestion = 0;
        correctAnswers = 0;
    }

    private void mathInit() {
        topic = new Topic("Math");
        topic.setShortDesc("Put aside that calculator and test your skills.");
        topic.setLongDesr("Questions involve simple Math that you should be able to calculate " +
                "in your head.");
        topic.setIcon("ic_launcher");
        topic.addQuestion("123 + 456 =", 2, new String[]{"123456", "569", "579", "654321"});
        topic.addQuestion("100 - -10 =", 0, new String[]{"110", "101", "100", "001"});
        topic.addQuestion("908 + 890 = ", 2, new String[]{"1234", "1900", "1798", "1978"});
    }

    private void physicsInit() {
        topic = new Topic("Physics");
        topic.setIcon("ic_launcher");
        topic.setShortDesc("High School Physics..sort of.");
        topic.setLongDesr("Questions involve simple high school Physics concepts, save for some " +
                "special picks ;)");
        topic.addQuestion("Newton's Laws of Motion are all these except..", 3, new String[]{"Law of Inertia",
                "F = ma", "For every action there is an equal and opposite reaction.",
                "A student will remain in bed unless acted upon by a large enough panic"});
    }

    private void marvelInit() {
        topic = new Topic("Marvel Super Heroes");
        topic.setIcon("ic_launcher");
        topic.setShortDesc("Do you know your heroes? Test yourself.");
        topic.setLongDesr("Questions involve general knowledge on Marvel Super Heroes. Get yourself " +
                "prepared before watching Avengers (if you haven't).");
        topic.addQuestion("What made Stephen Rogers the Captain America?", 2,
                new String[]{"Training", "Talent", "Drugs", "Patriotism"});
        topic.addQuestion("How did Iron Man's mansion get attacked by the bad guys in Iron Man 2?", 3,
                new String[]{"They followed Iron Man home.", "They put a tracker on Iron Man.",
                "Iron Man gave his address on TV.", "Everyone knew Iron Man's address the whole time."});
    }

    private void dotaInit() {
        topic = new Topic("Dota 2");
        topic.setIcon("ic_launcher");
        topic.setShortDesc("Check if you're a noob or not.");
        topic.setLongDesr("If you've played more than 200 hours of Dota 2, you should at least know " +
                "these. Else, you need more practice.");
        topic.addQuestion("What item should you get when you're a carry against a Phantom Assassin?", 1,
                new String[]{"BKB", "MKB", "Branch", "Dagon"});
    }

    private void endText() {
        topic.setShortDesc(topic.getShortDesc() + "\n\nNumber of Questions: " + topic.getQuestions().size() +
                "\n\nPress \"BEGIN\" to start. Good luck!");
        topic.setLongDesr(topic.getLongDesr() + "\n\nNumber of Questions: " + topic.getQuestions().size() +
                "\n\nPress \"BEGIN\" to start. Good luck!");
    }

}
