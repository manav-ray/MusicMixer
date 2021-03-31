package com.example.musicmixer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {

    MusicPlayer musicPlayer;
    PlayActivity playActivity;

    private final IBinder binder = new MyBinder();
    public static final String TAG = "MusicServiceME";

    @Override
    public void onCreate() {
        super.onCreate();
        musicPlayer = new MusicPlayer(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (musicPlayer != null) {
            String[] trackNames = getResources().getStringArray(R.array.music_names);

//            int backgroundTrackLength = 1;
//            track1Time = intent.getIntExtra(MainActivity.TRACK_1_VAL, 0);

            musicPlayer.setValues(
                    findIndexFromName(trackNames, intent.getStringExtra(MainActivity.BACKGROUND_NAME)),
                    findIndexFromName(trackNames, intent.getStringExtra(MainActivity.TRACK_1_NAME)),
                    findIndexFromName(trackNames, intent.getStringExtra(MainActivity.TRACK_2_NAME)),
                    findIndexFromName(trackNames, intent.getStringExtra(MainActivity.TRACK_3_NAME)),
                    intent.getIntExtra(MainActivity.TRACK_1_VAL, 0),
                    intent.getIntExtra(MainActivity.TRACK_2_VAL, 0),
                    intent.getIntExtra(MainActivity.TRACK_3_VAL, 0));
        }
        else {
            Log.d(TAG, "onStartCommand: but musicplayer is null");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private int findIndexFromName(String[] trackNames, String inName) {
        int idOut = 0;
        for (int i = 0; i < trackNames.length; ++i) {
            if (trackNames[i].equals(inName)) {
                return i;
            }
        }
        return idOut;
    }

    public void playMusic() {
        musicPlayer.playMusic();
    }

    public void pauseMusic() {
        musicPlayer.pauseMusic();
    }

    public void resumeMusic() {
        musicPlayer.resumeMusic();
    }

    public void restartMusic() {
        musicPlayer.restartMusic();
    }

    public int getMusicStatus() {
        return musicPlayer.getMusicStatus();
    }

    public static final String COMPLETE_INTENT = "complete intent";
    public static final String MUSIC_NAME = "music name";

    public void onUpdateMusicName(String musicName) {
        Intent intent = new Intent(COMPLETE_INTENT);
        intent.putExtra(MUSIC_NAME, musicName);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void setPlayActivity(PlayActivity inPlayActivity) {
        playActivity = inPlayActivity;
        Log.d(TAG, "setPlayActivity: reference set");
    }

    public void setImage(int inRID, String inName) {
        playActivity.setDisplayImage(inRID, inName);
    }
}
