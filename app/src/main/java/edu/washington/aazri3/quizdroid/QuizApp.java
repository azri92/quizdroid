package edu.washington.aazri3.quizdroid;

import android.app.Application;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Repository design.
 */

public class QuizApp extends Application {
    private static final String TAG = "QuizApp";

    private static QuizApp instance;
//    private static QuizRepo repository;
    private static JSONRepo repository;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Locked and loaded.");

        if (instance == null) {
            instance = this;
//            repository = QuizRepo.getInstance();
            repository = JSONRepo.getInstance();
            try {
                InputStream inputStream = getAssets().open("questions.json");
                String json = readJSONFile(inputStream);
                JSONArray jsonArray = new JSONArray(json);
                repository.processJSON(jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            throw new RuntimeException("QuizApp already instantiated");
        }
    }

    public static QuizApp getInstance() {
        return instance;
    }

    public JSONRepo getRepository() {
        return repository;
    }

    // reads InputStream of JSON file and returns the file in JSON String format
    public String readJSONFile(InputStream inputStream) throws IOException {

        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();

        return new String(buffer, "UTF-8");
    }
}
