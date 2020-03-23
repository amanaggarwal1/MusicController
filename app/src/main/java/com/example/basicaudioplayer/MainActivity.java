package com.example.basicaudioplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.voice.VoiceInteractionSession;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button playPauseButton;
    private SeekBar volumeSeekBar,  scrubSeekBar;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    private boolean isMusicPlaying = false;

    public void playMusic(View view){

        if(isMusicPlaying) mediaPlayer.pause();
        else mediaPlayer.start();

        isMusicPlaying ^= true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        volumeSeekBar = findViewById(R.id.volumeseekBar);
        scrubSeekBar = findViewById(R.id.scrubSeekBar);
        playPauseButton = findViewById(R.id.playPauseButton);

        mediaPlayer = MediaPlayer.create(this, R.raw.rainforest_music);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        scrubSeekBar.setMax(mediaPlayer.getDuration());

        scrubSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                playMusic(seekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playMusic(seekBar);
            }
        });

        if (!isMusicPlaying) {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    scrubSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            }, 0, 100);

        }
    }
}
