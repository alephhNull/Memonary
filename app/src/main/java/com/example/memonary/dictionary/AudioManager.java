package com.example.memonary.dictionary;

import android.media.MediaPlayer;

import java.io.IOException;

public class AudioManager implements Runnable{

    private String url;
    private static AudioManager manager;

    private AudioManager(String url) {
        this.url = url;
    }

    public static AudioManager getInstance(String url) {
        if (manager == null) {
            manager = new AudioManager(url);
        } else {
            manager.url = url;
        }
        return manager;
    }

    @Override
    public void run() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(this.url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
