package com.example.astori.tinyscrobbler;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.LoginFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class ScrollingActivity extends AppCompatActivity implements AsyncResponse {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startService(new Intent(this, MusicCatcher.class));
        onNewIntent(getIntent());
    }
    @Override
    public void onNewIntent(Intent intent) {
        Task asyncTask = new Task();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("track") && extras.containsKey("artist")) {
                String track = extras.getString("track");
                String artist = extras.getString("artist");
                track = track.replaceAll("\\([^()]*\\)", "");
                track = track.replaceAll("\\p{Punct}", "");
                String url = "http://www.azlyrics.com/lyrics/" + artist.toLowerCase().replace("and", "").replace(" ", "") + "/" + track.replace(" ", "").toLowerCase() + ".html";
                String url2 = "http://www.songlyrics.com/" + artist.toLowerCase().replace("and", "").replace(" ", "-") + "/" + track.replace(" ", "-").toLowerCase() + "-lyrics/";
                asyncTask.delegate = this;
                asyncTask.execute(url, url2, artist, track);
            }
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putAll(savedInstanceState);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    public void processFinish(String output){
        TextView textView = (TextView)findViewById(R.id.textView);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf");
        textView.setText(output);
        textView.setTypeface(type);
        textView.setTextSize(13);
        textView.setGravity(Gravity.CENTER);
    }

}
