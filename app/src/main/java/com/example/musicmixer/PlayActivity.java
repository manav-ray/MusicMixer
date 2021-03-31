package com.example.musicmixer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    String backgroundString, track1String, track2String, track3String;
    int trackTime1Val, trackTime2Val, trackTime3Val;
    Button playB, restartB;
    TextView trackTitle;
    ImageView pic;
    int imageRID;
    String currTrack;

    MusicService musicService;
    MusicCompletionListener musicCompletionListener;
    Intent startMusicServiceIntent;
    boolean isBound, isInitialized;
    public static final String TAG = "PlayActivityME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.d(TAG, "onCreate: extras are null");
        }
        else {
            backgroundString = extras.getString(MainActivity.BACKGROUND_NAME);
            track1String = extras.getString(MainActivity.TRACK_1_NAME);
            track2String = extras.getString(MainActivity.TRACK_2_NAME);
            track3String = extras.getString(MainActivity.TRACK_3_NAME);
            trackTime1Val = extras.getInt(MainActivity.TRACK_1_VAL);
            trackTime2Val = extras.getInt(MainActivity.TRACK_2_VAL);
            trackTime3Val = extras.getInt(MainActivity.TRACK_3_VAL);
        }

        if (backgroundString==null || track1String==null || track2String==null || track3String==null) {
            Log.d(TAG, "onCreate: One of the intent strings is null");
        }

        playB = (Button) findViewById(R.id.playPause);
        playB.setOnClickListener(this);
        restartB = (Button) findViewById(R.id.restart);
        restartB.setOnClickListener(this);
        trackTitle = (TextView) findViewById(R.id.trackTitle);
        pic = (ImageView) findViewById(R.id.pic);

        setDisplayImage(getRIDfromName(backgroundString), backgroundString);

        startMusicServiceIntent = new Intent(this, MusicService.class);
        startMusicServiceIntent.putExtra(MainActivity.BACKGROUND_NAME, backgroundString);
        startMusicServiceIntent.putExtra(MainActivity.TRACK_1_NAME, track1String);
        startMusicServiceIntent.putExtra(MainActivity.TRACK_2_NAME, track2String);
        startMusicServiceIntent.putExtra(MainActivity.TRACK_3_NAME, track3String);
        startMusicServiceIntent.putExtra(MainActivity.TRACK_1_VAL, trackTime1Val);
        startMusicServiceIntent.putExtra(MainActivity.TRACK_2_VAL, trackTime2Val);
        startMusicServiceIntent.putExtra(MainActivity.TRACK_3_VAL, trackTime3Val);

        if (backgroundString==null || track1String==null || track2String==null || track3String==null) {
            Log.d(TAG, "onCreate: One of the intent strings is null");
        }

        playB = (Button) findViewById(R.id.playPause);
        playB.setOnClickListener(this);
        restartB = (Button) findViewById(R.id.restart);
        restartB.setOnClickListener(this);
        trackTitle = (TextView) findViewById(R.id.trackTitle);
        pic = (ImageView) findViewById(R.id.pic);

        setDisplayImage(getRIDfromName(backgroundString), backgroundString);

        if (savedInstanceState != null) {
            isInitialized = savedInstanceState.getBoolean("INITIALIZE");
        }

        if (!isInitialized) {
            startService(startMusicServiceIntent);
            isInitialized = true;
        }
        musicCompletionListener = new MusicCompletionListener(this);
    }



    public static int getRIDfromName(String inName) {
        if (inName.equals(MusicPlayer.MUSICNAME[0]))
            return R.drawable.gotechgo;
        if (inName.equals(MusicPlayer.MUSICNAME[1]))
            return R.drawable.cheering;
        if (inName.equals(MusicPlayer.MUSICNAME[2]))
            return R.drawable.clapping;
        if (inName.equals(MusicPlayer.MUSICNAME[3]))
            return R.drawable.letsgohokies;
        return 0;
    }

    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder inBinder) {
            Log.d(TAG, "onServiceConnected: 1");
            MusicService.MyBinder binder = (MusicService.MyBinder) inBinder;
            musicService = binder.getService();
            musicService.setPlayActivity(getReference());
            isBound = true;
            switch(musicService.getMusicStatus()) {
                case 0:
                    //musicService.playMusic();
                    playB.setText("Play");
                    break;
                case 1:
                    musicService.playMusic();
                    playB.setText("Pause");
                    break;
                case 2:
                    musicService.pauseMusic();
                    playB.setText("Resume");
                    break;
                default:
                    break;
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: 2");
            musicService = null;
            isBound = false;
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("INITIALIZE", isInitialized);
        outState.putInt("IMAGERID", imageRID);
        outState.putString("TRACKTITLE", currTrack);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isInitialized = savedInstanceState.getBoolean("INITIALIZE");
        int tempRID = savedInstanceState.getInt("IMAGERID");
        String tempTitle = savedInstanceState.getString("TRACKTITLE");
        setDisplayImage(tempRID, tempTitle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isInitialized && !isBound) {
            bindService(startMusicServiceIntent,musicServiceConnection, Context.BIND_AUTO_CREATE);
        }
        registerReceiver(musicCompletionListener, new IntentFilter(MusicService.COMPLETE_INTENT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(musicCompletionListener);
    }




    public void setDisplayImage(int tempRID, String inName) {
        imageRID = tempRID;
        currTrack = inName;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pic.setImageResource(imageRID);
                trackTitle.setText(currTrack);
            }
        });
    }

    public PlayActivity getReference() {
        return PlayActivity.this;
    }

    @Override
    public void onClick(View v) {
        Log.d("PlayActivity", "onClick "+isBound);
        if (v.getId() == playB.getId()) {
            if (isBound) {
                switch (musicService.getMusicStatus()) {
                    case 0:
                        musicService.playMusic();
                        playB.setText("Pause");
                        Log.d("PlayActivity", "play button clicked");
                        break;
                    case 1:
                        musicService.pauseMusic();
                        playB.setText("Resume");
                        break;
                    case 2:
                        musicService.resumeMusic();
                        playB.setText("Pause");
                        break;
                    default:
                        break;
                }
            }
        }
        if (v.getId() == restartB.getId()) {
            if (isBound) {
                Log.d(TAG, "onClick: isBound=true");
                musicService.restartMusic();
                playB.setText("Play");
            }
        }
    }
}
