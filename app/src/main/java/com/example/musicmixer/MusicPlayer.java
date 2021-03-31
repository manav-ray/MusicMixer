package com.example.musicmixer;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;


public class MusicPlayer implements MediaPlayer.OnCompletionListener {

    MusicService musicService;
    MediaPlayer player, playert1, playert2, playert3;
    int musicStatus;
    int currentPosition, track1Position, track2Position, track3Position;
    int timeCount;
    int backgroundMusicIndex, track1Index, track2Index, track3Index;
    int track1SeekVal, track2SeekVal, track3SeekVal;
    int track1StartTime, track2StartTime, track3StartTime;
    Timer timer;

    public static final String TAG = "MusicPlayerME";

    static final int[] MUSICPATH = new int[]{
            R.raw.gotechgo,
            R.raw.cheering,
            R.raw.clapping,
            R.raw.letsgohokies
    };

    static final String[] MUSICNAME = new String[]{
            "Go Tech Go!",
            "Cheering",
            "Clapping",
            "Let's Go Hokies"
    };

    public MusicPlayer(MusicService inService) {
        musicService = inService;
        //musicService.getMus
        //CHANGE THIS SHIT
        backgroundMusicIndex = 1;
        musicStatus = 0;
        timeCount = 0;
        currentPosition = 0;
        track1Position = -1;
        track2Position = -1;
        track3Position = -1;
        timer = new Timer();
    }

    public void setValues(int inB, int inT1, int inT2, int inT3, int inT1time, int inT2time, int inT3time) {
        backgroundMusicIndex = inB;
        track1Index = inT1;
        track2Index = inT2;
        track3Index = inT3;
        track1SeekVal = inT1time;
        track2SeekVal = inT2time;
        track3SeekVal = inT3time;
    }














    public void playMusic() {
        if (player == null) {
            musicService.onUpdateMusicName(MUSICNAME[backgroundMusicIndex]);
            player = MediaPlayer.create(musicService, MUSICPATH[backgroundMusicIndex]);
            player.start();
            player.setOnCompletionListener(this);

            int backgoundTrackLength = player.getDuration();
            track1StartTime = track1SeekVal * backgoundTrackLength / 100;
            track2StartTime = track2SeekVal * backgoundTrackLength / 100;
            track3StartTime = track3SeekVal * backgoundTrackLength / 100;
            timerPlay();
            musicStatus = 1;
        }
    }

    private void timerPlay() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timeCount == track1StartTime) {
                    playert1 = MediaPlayer.create(musicService, MUSICPATH[track1Index]);
                    playert1.start();
                    playert1.setOnCompletionListener(getReference());
                    int tempID = PlayActivity.getRIDfromName(MUSICNAME[track1Index]);
                    musicService.setImage(tempID, MUSICNAME[track1Index]);
                    track1Position = 0;
                }
                if (timeCount == track2StartTime) {
                    playert2 = MediaPlayer.create(musicService, MUSICPATH[track2Index]);
                    playert2.start();
                    playert2.setOnCompletionListener(getReference());
                    int tempID = PlayActivity.getRIDfromName(MUSICNAME[track2Index]);
                    musicService.setImage(tempID, MUSICNAME[track2Index]);
                    track2Position = 0;
                }
                if (timeCount == track3StartTime) {
                    playert3 = MediaPlayer.create(musicService, MUSICPATH[track3Index]);
                    playert3.start();
                    playert3.setOnCompletionListener(getReference());
                    int tempID = PlayActivity.getRIDfromName(MUSICNAME[track3Index]);
                    musicService.setImage(tempID, MUSICNAME[track3Index]);
                    track3Position = 0;
                }
                timeCount++;
            }
        }, 0, 1);
    }

















    public void pauseMusic() {
        timer.cancel();
        if (player != null && player.isPlaying()) {
            player.pause();
            currentPosition = player.getCurrentPosition();
            musicStatus = 2;
        }
        if (playert1 != null && playert1.isPlaying()) {
            if (track1Position == -1) {
                Log.d(TAG, "pauseMusic: ERROR, playert1 is playing but currPos == -1");
            }
            playert1.pause();
            track1Position = playert1.getCurrentPosition();
        }
        if (playert2 != null && playert2.isPlaying()) {
            if (track2Position == -1) {
                Log.d(TAG, "pauseMusic: ERROR, playert2 is playing but currPos == -1");
            }
            playert2.pause();
            track2Position = playert2.getCurrentPosition();
        }
        if (playert3 != null && playert3.isPlaying()) {
            if (track3Position == -1) {
                Log.d(TAG, "pauseMusic: ERROR, playert3 is playing but currPos == -1");
            }
            playert3.pause();
            track3Position = playert3.getCurrentPosition();
        }
    }

    public void resumeMusic() {
        if (player != null) {
            player.seekTo(currentPosition);
            player.start();
            timerPlay();
            musicStatus = 1;
        }
        if (playert1 != null ) {
            if (track1Position == -1) {
                Log.d(TAG, "resumeMusic: ERROR, playert1 is not null but currPos == -1");
            }
            playert1.seekTo(track1Position);
            playert1.start();
        }
        if (playert2 != null ) {
            if (track1Position == -1) {
                Log.d(TAG, "resumeMusic: ERROR, playert2 is not null but currPos == -1");
            }
            playert2.seekTo(track2Position);
            playert2.start();
        }
        if (playert3 != null ) {
            if (track1Position == -1) {
                Log.d(TAG, "resumeMusic: ERROR, playert3 is not null but currPos == -1");
            }
            playert3.seekTo(track1Position);
            playert3.start();
        }
    }

    public void restartMusic() {
        pauseMusic();
        player = null;
        musicStatus = 0;
        playert1 = null;
        playert2 = null;
        playert3 = null;
        currentPosition = 0;
        track1Position = -1;
        track2Position = -1;
        track3Position = -1;
        timeCount = 0;
    }

    public int getMusicStatus() {
        return musicStatus;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mp.getAudioSessionId() == player.getAudioSessionId()) {
            Log.d(TAG, "onCompletion: background track completed");
        }
        else {
            musicService.setImage(PlayActivity.getRIDfromName(MUSICNAME[backgroundMusicIndex]), MUSICNAME[backgroundMusicIndex]);
        }
        if (playert1 != null && mp.getAudioSessionId() == playert1.getAudioSessionId()) {
            Log.d(TAG, "onCompletion: track1 completed");
        }
        if (playert2 != null && mp.getAudioSessionId() == playert2.getAudioSessionId()) {
            Log.d(TAG, "onCompletion: track2 completed");
        }
        if (playert3 != null && mp.getAudioSessionId() == playert3.getAudioSessionId()) {
            Log.d(TAG, "onCompletion: track3 completed");
        }
    }






    public MusicPlayer getReference() {
        return MusicPlayer.this;
    }
}
