package edu.washington.aazri3.quizdroid;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */
public class PreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "PreferenceFragment";
    private static final String DEFAULT_URL = "tednewardsandbox.site44.com/questions.json";

    private Context context;

//    private PendingIntent pendingIntent;

    public PreferencesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        context = getActivity();

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
                    stopAlarmIfRunning();

                } else if (Integer.parseInt(interval) < 1) {
                    stopAlarmIfRunning();

                } else if (downloadID > 0) {

                    DownloadManager.Query dq = new DownloadManager.Query();
                    DownloadManager dm = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                    // set the query filter to our previously Enqueued download
                    dq.setFilterById(downloadID);

                    // Query the download manager about downloads that have been requested.
                    Cursor cursor = dm.query(dq);
                    if (cursor.moveToFirst()) {
                        // column for status
                        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int status = cursor.getInt(columnIndex);

                        switch (status) {
                            case DownloadManager.STATUS_PENDING:
                            case DownloadManager.STATUS_RUNNING:
                                // TODO: Delay until next download
                                break;
                            default:
                                Log.d(TAG, "new alarm started");
                                startAlarm(url, Integer.parseInt(interval));
                                break;
                        }
                    }

                } else {

                    Log.d(TAG, "new alarm started");
                    startAlarm(url, Integer.parseInt(interval));

                }
            }
        }

        if (pref instanceof SwitchPreference) {
            SwitchPreference switchPref = (SwitchPreference) pref;
            if (switchPref.isChecked()) {
                Log.d(TAG, "new alarm started");
                String url = sharedPreferences.getString("url_pref", "");
                int interval = Integer.parseInt(sharedPreferences.getString("interval_pref", "0"));
                startAlarm(url, interval);

            } else {
                stopAlarmIfRunning();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener for key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    private void startAlarm(String url, int interval) {

        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.putExtra("url", "http://" + url);
        intent.setAction("edu.washington.aazri3.quizdroid.download");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        int intervalInMillis = interval * 1000 * 60;

        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
//        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + intervalInMillis, intervalInMillis, pendingIntent);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000, 3000, pendingIntent); // for testing

    }

    private void stopAlarmIfRunning() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("edu.washington.aazri3.quizdroid.download");
        if ((PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE) != null))
            stopAlarm();
    }

    private void stopAlarm() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("edu.washington.aazri3.quizdroid.download");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_NO_CREATE);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        pendingIntent.cancel();

        SwitchPreference downloadSwitch = (SwitchPreference) findPreference("download_switch");
        if (downloadSwitch.isChecked()) {
            downloadSwitch.setChecked(false);
        }
    }

}
