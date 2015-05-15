package edu.washington.aazri3.quizdroid;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Repo that contains a list of topics.get(currTopic)s to choose from and Topic object based
 * on chosen topics.get(currTopic).
 * Also tracks current question and number of correct answers.
 */
public class JSONRepo implements TopicRepository {
    private static final String TAG = "JSONRepo";

    private static JSONRepo instance = null;

    private List<String> topicList = new ArrayList<>();
    private Map<String, Topic> topics = new HashMap<>();
    private String currTopic;
    private int currentQuestion = 0;
    private int correctAnswers = 0;

    protected JSONRepo() {
        // Exists only to defeat instantiation.
    }

    public static JSONRepo getInstance() {
        if(instance == null) {
            instance = new JSONRepo();
        }
        return instance;
    }

    public void processJSON(JSONArray json) {
        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject jsonObject = json.getJSONObject(i);
                String title = jsonObject.getString("title");
                Topic topic = new Topic(title);
                topic.setIcon("ic_launcher");
                topic.setShortDesc("not set");
                JSONArray questions = jsonObject.getJSONArray("questions");

                for (int j = 0; j < questions.length(); j++) {
                    JSONObject q = questions.getJSONObject(j);
                    String text = q.getString("text");
                    int answer = q.getInt("answer") - 1;
                    String[] options = new String[4];
                    JSONArray answers = q.getJSONArray("answers");
                    Log.d(TAG, "Number of answers " + answers.length());
                    for (int k = 0; k < answers.length(); k++) {
                        options[k] = answers.getString(k);
                    }

                    topic.addQuestion(text, answer, options);
                }

                topic.setLongDesr(jsonObject.getString("desc") + endText(questions.length()));

                topics.put(title, topic);
                topicList.add(title);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setTopic(String topic) {
        currTopic = topic;
    }

    public List<String> getTopicList() {
        return topicList;
    }

    public String getTopic() {
        return currTopic;
    }

    public String getIcon() {
        return topics.get(currTopic).getIcon();
    }

    public String getShortDesc() {
        return topics.get(currTopic).getLongDesr();
    }

    public String getLongDesc() {
        return topics.get(currTopic).getLongDesr();
    }

    public String getQuestion() {
        return topics.get(currTopic).question(currentQuestion).getQuestion();
    }

    public int getAnswer() {
        return topics.get(currTopic).question(currentQuestion).getAnswer();
    }

    public String[] getOptions() {
        return topics.get(currTopic).question(currentQuestion).getOptions();
    }

    public int currentQuestion() {
        return currentQuestion;
    }

    public void nextQuestion() {
        currentQuestion++;
    }

    public boolean isCorrect(int chosenAnswer) {
        return topics.get(currTopic).question(currentQuestion).getAnswer() == chosenAnswer;
    }

    public void answeredCorrectly() {
        correctAnswers++;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public boolean isLastQuestion() {
        return currentQuestion == (topics.get(currTopic).getQuestions().size() - 1);
    }

    public void reset() {
        currentQuestion = 0;
        correctAnswers = 0;
    }

    private String endText(int num) {
        return "\n\nNumber of Questions: " + num +
                "\n\nPress \"BEGIN\" to start. Good luck!";
    }

}
