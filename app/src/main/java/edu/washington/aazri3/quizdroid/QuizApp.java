package edu.washington.aazri3.quizdroid;

import android.app.Application;
import android.util.Log;

/**
 *
 */

public class QuizApp extends Application {
    private static final String TAG = "QuizApp";

    private static QuizApp instance;
    private static QuizRepo repository;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Locked and loaded.");

        if (instance == null) {
            instance = this;
            repository = QuizRepo.getInstance();
        } else {
            throw new RuntimeException("QuizApp already instantiated");
        }
    }

    public static QuizApp getInstance() {
        return instance;
    }

    public QuizRepo getRepository() {
        return repository;
    }

}
