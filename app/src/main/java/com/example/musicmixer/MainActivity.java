package com.example.musicmixer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener{

    public static final String TAG = "MainActivityME";
    Button play;
    Spinner background, track1, track2, track3;
    String backgroundString, track1String, track2String, track3String;
    SeekBar trackTime1, trackTime2, trackTime3;
    int trackTime1Val, trackTime2Val, trackTime3Val;

    public static final String BACKGROUND_NAME = "BACKGROUND_MUSIC_NAME";
    public static final String TRACK_1_NAME = "TRACK_1_MUSIC_NAME";
    public static final String TRACK_2_NAME = "TRACK_2_MUSIC_NAME";
    public static final String TRACK_3_NAME = "TRACK_3_MUSIC_NAME";
    public static final String TRACK_1_VAL = "TRACK_1_MUSIC_VALUE";
    public static final String TRACK_2_VAL = "TRACK_2_MUSIC_VALUE";
    public static final String TRACK_3_VAL = "TRACK_3_MUSIC_VALUE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (Button) findViewById(R.id.playMain);
        play.setOnClickListener(this);

        background = (Spinner) findViewById(R.id.backgroundName);
        track1 = (Spinner) findViewById(R.id.trackName1);
        track2 = (Spinner) findViewById(R.id.trackName2);
        track3 = (Spinner) findViewById(R.id.trackName3);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.music_names, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        background.setAdapter(adapter);
        track1.setAdapter(adapter);
        track2.setAdapter(adapter);
        track3.setAdapter(adapter);

        background.setOnItemSelectedListener(this);
        track1.setOnItemSelectedListener(this);
        track2.setOnItemSelectedListener(this);
        track3.setOnItemSelectedListener(this);

        backgroundString = "";
        track1String = "";
        track2String = "";
        track3String = "";

        trackTime1 = (SeekBar) findViewById(R.id.trackTime1);
        trackTime2 = (SeekBar) findViewById(R.id.trackTime2);
        trackTime3 = (SeekBar) findViewById(R.id.trackTime3);

        trackTime1.setOnSeekBarChangeListener(this);
        trackTime2.setOnSeekBarChangeListener(this);
        trackTime3.setOnSeekBarChangeListener(this);

        trackTime1Val = 0;
        trackTime2Val = 0;
        trackTime3Val = 0;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == play.getId()) {
            Intent myIntent = new Intent(MainActivity.this, PlayActivity.class);
            myIntent.putExtra(BACKGROUND_NAME, backgroundString);
            myIntent.putExtra(TRACK_1_NAME, track1String);
            myIntent.putExtra(TRACK_2_NAME, track2String);
            myIntent.putExtra(TRACK_3_NAME, track3String);

            myIntent.putExtra(TRACK_1_VAL, trackTime1Val);
            myIntent.putExtra(TRACK_2_VAL, trackTime2Val);
            myIntent.putExtra(TRACK_3_VAL, trackTime3Val);
            startActivity(myIntent);
        }
        if (v == findViewById(R.id.playPause)) {
            Log.d("MainActivity","playpausebutton detected");
        }
        if (v == findViewById(R.id.restart)) {
            Log.d("MainActivity","restart detected");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] trackNames = getResources().getStringArray(R.array.music_names);
        if (parent == null || view ==null || background == null || track1 == null || track2 == null || track3 == null) {
            Log.d(TAG, "onItemSelected: spinners or input view is null");
        }
        else if (parent.getId() == R.id.backgroundName) {
            backgroundString = trackNames[position];
        }
        else if (parent.getId() == track1.getId()) {
            track1String = trackNames[position];
        }
        else if (parent.getId() == track2.getId()) {
            track2String = trackNames[position];
        }
        else if (parent.getId() == track3.getId()) {
            track3String = trackNames[position];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getApplicationContext(), "Nothing selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == null || trackTime1 == null || trackTime2 == null || trackTime3 == null) {
            Toast.makeText(getApplicationContext(), "ERROR: one of the spinners or the input view has a value null", Toast.LENGTH_LONG).show();
        }
        else if (seekBar.getId() == trackTime1.getId()) {
            trackTime1Val = progress;
        }
        else if (seekBar.getId() == trackTime2.getId()) {
            trackTime2Val = progress;
        }
        else if (seekBar.getId() == trackTime3.getId()) {
            trackTime3Val = progress;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}