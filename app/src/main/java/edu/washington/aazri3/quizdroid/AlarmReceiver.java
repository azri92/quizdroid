package edu.washington.aazri3.quizdroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AlarmReceiver", "Broadcast received.");

//        Intent downloadServiceIntent = new Intent(context, DownloadService.class);
//        context.startService(downloadServiceIntent);

        if (intent.getAction().equals("edu.washington.aazri3.quizdroid.download")) {
            String msg = intent.getStringExtra("url");
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
