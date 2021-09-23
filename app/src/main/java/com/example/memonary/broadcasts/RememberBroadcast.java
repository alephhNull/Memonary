package com.example.memonary.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.memonary.DatabaseManager;
import com.example.memonary.MainActivity;
import com.example.memonary.NotifyWorker;
import com.example.memonary.Scheduler;
import com.example.memonary.dictionary.WordModel;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.example.memonary.dictionary.WordWrapper;

public class RememberBroadcast extends BroadcastReceiver {
    private Scheduler scheduler;
    private DatabaseManager dbManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        scheduler = Scheduler.getInstance();
        dbManager = DatabaseManager.getInstance();
        String wordId = intent.getExtras().getString("wordId");
        WordModel word = dbManager.getWordById(wordId);
        onRemember(word, wordId);
        NotificationManagerCompat.from(context).cancel(Integer.parseInt(wordId));
    }

    public void onRemember(WordModel wordModel, String id) {
        scheduler.updateSchedule(wordModel);
        dbManager.updateWord(wordModel, id);
    }

}
