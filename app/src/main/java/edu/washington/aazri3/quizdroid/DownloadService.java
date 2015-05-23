package edu.washington.aazri3.quizdroid;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

public class DownloadService extends IntentService {
    private static final String TAG = "DownloadService";

    private DownloadManager dm;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onCreate() { super.onCreate(); }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        if (!isConnected) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (Settings.System.getInt(getApplicationContext().getContentResolver(),
                        Settings.Global.AIRPLANE_MODE_ON, 0) != 0) {
                    startDialogActivity("airplane");
                } else {
                    startDialogActivity("no connection");
                }
            } else {
                if (Settings.System.getInt(getApplicationContext().getContentResolver(),
                        Settings.System.AIRPLANE_MODE_ON, 0) != 0) {
                    startDialogActivity("airplane");
                } else {
                    startDialogActivity("no connection");
                }
            }
        } else {
            // Specify the url you want to download here
            String url = intent.getStringExtra("url");
            Log.d("DownloadService", "URL to contact is " + url);

            // Start the download
            dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            long downloadID = dm.enqueue(request);
            QuizApp.getInstance().setCurrentDownloadID(downloadID);
        }
    }

    private void startDialogActivity(String reason) {
        Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
        intent.putExtra("reason", reason);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
