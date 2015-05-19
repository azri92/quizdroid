package edu.washington.aazri3.quizdroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import java.util.List;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);
        List<String> topics = QuizApp.getInstance().getRepository().getTopicList();
        ArrayAdapter<String> adapter = new CustomListAdapter(topics);
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
}
