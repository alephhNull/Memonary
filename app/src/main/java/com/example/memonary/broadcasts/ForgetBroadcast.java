package com.example.memonary.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

import com.example.memonary.DatabaseManager;
import com.example.memonary.Scheduler;
import com.example.memonary.dictionary.WordModel;
import com.google.gson.Gson;

public class ForgetBroadcast extends BroadcastReceiver {
    private Scheduler scheduler;
    private DatabaseManager dbManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        scheduler = Scheduler.getInstance();
        dbManager = DatabaseManager.getInstance();
        Gson gson = new Gson();
        WordModel word = gson.fromJson(intent.getExtras().getString("word"), WordModel.class);
        onForgot(word);
        NotificationManagerCompat.from(context).cancel(Integer.parseInt(word.getId()));
    }

    public void onForgot(WordModel wordModel) {
        scheduler.resetSchedule(wordModel);
        dbManager.updateWord(wordModel);
    }
}
