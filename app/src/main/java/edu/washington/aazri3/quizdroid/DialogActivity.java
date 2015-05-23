package edu.washington.aazri3.quizdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

/**
 * To be used in any screen.
 */
public class DialogActivity extends Activity {
    private static final String TAG = "DialogActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // I think it's most sensible to stop the alarm when we have to
        // show a dialog to the user.
        QuizApp.getInstance().stopAlarmIfRunning();

        String reason = getIntent().getStringExtra("reason");
        String title = "Unable to Connect to Internet";

        if (reason.equals("airplane")) {
            Log.d(TAG, "Notifying user about airplane mode.");
            AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage("It seems you have Airplane Mode on. Would you like to " +
                            "turn it off?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "Sending user to System Settings");
                            if (android.os.Build.VERSION.SDK_INT < 17) {
                                Intent intentAirplaneMode = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                                intentAirplaneMode.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intentAirplaneMode);
                            } else {
                                Intent intentAirplaneMode = new Intent("android.settings.WIRELESS_SETTINGS");
                                intentAirplaneMode.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intentAirplaneMode);
                            }
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
            AlertDialog dialogue = dialogueBuilder.create();
            dialogue.show();

        } else if (reason.equals("download failed")) {
            Log.d(TAG, "Notifying user about failed download.");
            AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(this)
                    .setTitle("Download Failed")
                    .setMessage("Looks like the latest download failed. Would you like to " +
                            "retry?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            String url = sharedPreferences.getString("url_pref", "");
                            int interval = Integer.parseInt(sharedPreferences.getString("interval_pref", "1"));
                            Log.d(TAG, "interval is " + interval);
                            QuizApp.getInstance().startAlarm(url, interval, false);
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
            AlertDialog dialogue = dialogueBuilder.create();
            dialogue.show();

        } else {
            Log.d(TAG, "Notifying user connectivity issue.");
            AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage("Looks we lost contact with the outside world. " +
                            "Try checking your data/wifi.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert);
            AlertDialog dialogue = dialogueBuilder.create();
            dialogue.show();
        }
    }
}
