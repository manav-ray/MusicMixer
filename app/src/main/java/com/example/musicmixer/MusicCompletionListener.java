package com.example.musicmixer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class MusicCompletionListener extends BroadcastReceiver {
    PlayActivity playActivity;
    public static final String TAG = "MusicCompleteListenerME";


    @Override
    public void onReceive(Context context, Intent intent) {
        //String musicName = intent.getStringExtra(MusicService.MUSIC_NAME);
        //playActivity.updateName(musicName);
        Log.d(TAG, "onReceive: onRecieve, intent");
        playActivity.pic.setImageResource(R.drawable.letsgohokies);
    }

    public MusicCompletionListener(PlayActivity inPlayActivity) {
        playActivity = inPlayActivity;
    }
}
