package edu.washington.aazri3.quizdroid;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private long currentDownloadID;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Locked and loaded.");

        if (instance == null) {
            instance = this;
//            repository = QuizRepo.getInstance();
            repository = JSONRepo.getInstance();

            File myFile = new File(getFilesDir().getAbsolutePath(), "/questions.json");
            String json = null;

            if (myFile.exists()) {
                Log.i("MyApp", "questions.json DOES exist");

                try {
                    FileInputStream fis = openFileInput("questions.json");
                    json = readJSONFile(fis);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Log.i("MyApp", "downloaded questions.json DOESN'T exist. Fetching from assets");

                try {
                    InputStream inputStream = getAssets().open("questions.json");
                    json = readJSONFile(inputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            repository.processJSON(jsonArray);

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

    public void setCurrentDownloadID(long downloadID) {
        currentDownloadID = downloadID;
    }

    public long getCurrentDownloadID() {
        return currentDownloadID;
    }

    public void startAlarm(String url, int interval, Boolean delay) {

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("url", "http://" + url);
        intent.setAction("edu.washington.aazri3.quizdroid.download");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        int intervalInMillis = interval * 1000 * 60;

        AlarmManager manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        long startTime = System.currentTimeMillis();
        if (delay) {
            startTime += intervalInMillis;
        }

//        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, startTime, intervalInMillis, pendingIntent);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5000, pendingIntent); // for testing

    }

    public void stopAlarmIfRunning() {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.setAction("edu.washington.aazri3.quizdroid.download");
        if ((PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE) != null)) {
            stopAlarm();
        } else {
            Log.d(TAG, "Alarm not running, so nothing to stop.");
        }
    }

    private void stopAlarm() {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.setAction("edu.washington.aazri3.quizdroid.download");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                0, intent, PendingIntent.FLAG_NO_CREATE);

        AlarmManager manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        pendingIntent.cancel();
        Log.d(TAG, "Alarm stopped.");
    }

}
