package edu.washington.aazri3.quizdroid;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    private DownloadManager dm;
    private final AlarmReceiver alarmReceiver = new AlarmReceiver();
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);

        registerReceiver(alarmReceiver, new IntentFilter("edu.washington.aazri3.quizdroid.download"));

        updateUI();
    }

    private void updateUI() {
        listView = (ListView) findViewById(R.id.listView);
        List<String> topics = QuizApp.getInstance().getRepository().getTopicList();
        adapter = new CustomListAdapter(topics);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String chosenTopic = (String) adapterView.getItemAtPosition(i);
                QuizApp.getInstance().getRepository().setTopic(chosenTopic);

                Intent next = new Intent(getApplicationContext(), QuizActivity.class);
                startActivity(next);
            }
        });
    }

    private final BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

            Log.i("MyApp BroadcastReceiver", "Download receiver received.");

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                Log.i(TAG, "Download-complete broadcast received.");
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                long downloadID = QuizApp.getInstance().getCurrentDownloadID();

                // if the downloadID is right
                if (id == downloadID) {

                    // Check status
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadID);
                    Cursor c = dm.query(query);
                    try {
                        if (c.moveToFirst()) {
                            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                            switch (status) {
                                case DownloadManager.STATUS_PAUSED:
                                    Log.d(TAG, "Download Status - Paused");
                                case DownloadManager.STATUS_PENDING:
                                    Log.d(TAG, "Download Status - Pending");
                                case DownloadManager.STATUS_RUNNING:
                                    Log.d(TAG, "Download Status - Running");
                                    break;
                                case DownloadManager.STATUS_SUCCESSFUL:
                                    // The download-complete message said the download was "successfu" then run this code
                                    ParcelFileDescriptor downloadedFile;
                                    try {
                                        // Get file from Download Manager
                                        downloadedFile = dm.openDownloadedFile(downloadID);
                                        FileInputStream fis = new FileInputStream(downloadedFile.getFileDescriptor());

                                        // Convert file to string
                                        String jsonStr = null;
                                        try {
                                            FileChannel fc = fis.getChannel();
                                            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                                            jsonStr = Charset.defaultCharset().decode(bb).toString();
                                        } finally {
                                            fis.close();
                                        }
                                        Log.d(TAG, "JSON String downloaded: " + jsonStr);
                                        Boolean JSONisValid = isJSONValid(jsonStr);

                                        // Write string to data/questions.json if it's valid JSON
                                        if (JSONisValid) {
                                            try {
                                                Log.i(TAG, "writing downloaded json string to file");

                                                File file = new File(getFilesDir().getAbsolutePath(), "questions.json");
                                                FileOutputStream fos = new FileOutputStream(file, false);
                                                fos.write(jsonStr.getBytes());
                                                fos.close();
                                            } catch (IOException e) {
                                                Log.e("Exception", "File write failed: " + e.toString());
                                            }

                                            JSONArray jsonArray = new JSONArray(jsonStr);
                                            QuizApp.getInstance().getRepository().processJSON(jsonArray);
                                        } else {
                                            Log.d(TAG, "Invalid JSON");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case DownloadManager.STATUS_FAILED:
                                    Log.d(TAG, "Download Status - Failed");
                                    Intent dialogIntent = new Intent(getApplicationContext(), DialogActivity.class);
                                    dialogIntent.putExtra("reason", "download failed");
                                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getApplicationContext().startActivity(dialogIntent);
                                    break;
                            }
                        }
                    } finally {
                        c.close();
                    }

                } else {
                    Log.d(TAG + " Download", "Received ID " + id + " but download ID is " + downloadID);
                }

            }
        }
    };

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        return;
    }

    class CustomListAdapter extends ArrayAdapter<String> {
        private List<String> topicList;

        public CustomListAdapter(List<String> topicList) {
            super(getApplicationContext(), R.layout.custom_listview, topicList);

            this.topicList = topicList;
        }

        public View getView(int position,View view,ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.custom_listview, parent, false);

            ImageView imgIcon = (ImageView) rowView.findViewById(R.id.imgIcon);
            TextView txtTopic = (TextView) rowView.findViewById(R.id.txtTopic);

            txtTopic.setText(topicList.get(position));

            String id = QuizApp.getInstance().getRepository().getIcon(topicList.get(position));
            int imgID = getResources().getIdentifier(id, "mipmap", getPackageName());
            imgIcon.setImageResource(imgID);

            return rowView;

        }

        public void replaceData(List<String> newTopics ) {
            topicList.clear();
            topicList = newTopics;
            this.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.preferences) {
            Intent next = new Intent(getApplicationContext(), PreferencesActivity.class);
            startActivity(next);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(downloadReceiver);
        unregisterReceiver(alarmReceiver);
    }
}
