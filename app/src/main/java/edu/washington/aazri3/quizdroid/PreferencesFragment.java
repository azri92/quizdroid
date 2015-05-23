package edu.washington.aazri3.quizdroid;

import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;


/**
 * A placeholder fragment containing a simple view.
 */
public class PreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "PreferenceFragment";
    private static final String DEFAULT_URL = "tednewardsandbox.site44.com/questions.json";

    public PreferencesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        EditTextPreference urlPref = (EditTextPreference) findPreference("url_pref");
        if (urlPref != null && urlPref.getText() != null && !urlPref.getText().equals("")) {
            urlPref.setSummary(urlPref.getText());
        }

        EditTextPreference intervalPref = (EditTextPreference) findPreference("interval_pref");
        if (intervalPref != null && intervalPref.getText() != null && !intervalPref.getText().equals("")) {
            String text = intervalPref.getText() + " minute";
            if (Integer.parseInt(intervalPref.getText()) > 1) {
                text += "s";
            }
            intervalPref.setSummary(text);
        }

        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.setAction("edu.washington.aazri3.quizdroid.download");
        if ((PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_NO_CREATE) == null)) {
            turnOffSwitch();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);

        if (pref instanceof EditTextPreference) {
            EditTextPreference txtPref = (EditTextPreference) pref;
            String text = txtPref.getText();
            if (text.length() > 0) {
                if (key.equals("interval_pref")) {
                    text += " minute";
                    if (Integer.parseInt(txtPref.getText()) > 1) {
                        text += "s";
                    }
                }
                txtPref.setSummary(text);
            } else {
                String s = "";
                if (key.equals("url_pref")) {
                     s = getResources().getString(R.string.summary_url_pref);
                } else if (key.equals("interval_pref")) {
                    s = getResources().getString(R.string.summary_interval_pref);
                }
                txtPref.setSummary(s);
            }

            // Start new alarm only if switch is turned on & no pending/running downloads exist
            //  & interval is more than 0
            SwitchPreference downloadSwitch = (SwitchPreference) findPreference("download_switch");
            long downloadID = QuizApp.getInstance().getCurrentDownloadID();
            Log.d(TAG, "Download switch is " + Boolean.toString(downloadSwitch.isChecked()));
            String url = sharedPreferences.getString("url_pref", "");
            String interval = sharedPreferences.getString("interval_pref", "0");
            if (downloadSwitch.isChecked() && url != null && interval != null) {
                if (url.length() == 0 || interval.length() == 0) {
                    QuizApp.getInstance().stopAlarmIfRunning();
                    turnOffSwitch();

                } else if (Integer.parseInt(interval) < 1) {
                    QuizApp.getInstance().stopAlarmIfRunning();
                    turnOffSwitch();

                } else if (downloadID > 0) {

                    DownloadManager.Query dq = new DownloadManager.Query();
                    DownloadManager dm = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                    // set the query filter to our previously Enqueued download
                    dq.setFilterById(downloadID);

                    // Query the download manager about downloads that have been requested.
                    Cursor cursor = dm.query(dq);
                    try {
                        if (cursor.moveToFirst()) {
                            // column for status
                            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                            int status = cursor.getInt(columnIndex);

                            switch (status) {
                                case DownloadManager.STATUS_PENDING:
                                case DownloadManager.STATUS_RUNNING:
                                    QuizApp.getInstance().startAlarm(url, Integer.parseInt(interval), true);
                                    break;
                                default:
                                    Log.d(TAG, "new alarm started");
                                    QuizApp.getInstance().startAlarm(url, Integer.parseInt(interval), false);
                                    break;
                            }
                        }
                    } finally {
                        cursor.close();
                    }

                } else {

                    Log.d(TAG, "new alarm started");
                    QuizApp.getInstance().startAlarm(url, Integer.parseInt(interval), false);

                }
            }
        }

        if (pref instanceof SwitchPreference) {
            SwitchPreference switchPref = (SwitchPreference) pref;
            if (switchPref.isChecked()) {
                Log.d(TAG, "new alarm started");
                String url = sharedPreferences.getString("url_pref", "");
                int interval = Integer.parseInt(sharedPreferences.getString("interval_pref", "0"));
                QuizApp.getInstance().startAlarm(url, interval, false);

            } else {
                QuizApp.getInstance().stopAlarmIfRunning();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener for key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.setAction("edu.washington.aazri3.quizdroid.download");
        if ((PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_NO_CREATE) == null)) {
            turnOffSwitch();
        }
    }

    private void turnOffSwitch() {
        SwitchPreference downloadSwitch = (SwitchPreference) findPreference("download_switch");
        if (downloadSwitch.isChecked()) {
            downloadSwitch.setChecked(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
