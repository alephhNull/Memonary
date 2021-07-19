package com.example.memonary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.TimeUnit;

public class RememberBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String word = bundle.getString("word");
        DatabaseReference reference = MainActivity.mDatabase;
        WordWrapper wordWrapper = MainActivity.savedWords.get(bundle.getString("word"));
        if (wordWrapper != null && !wordWrapper.getState().equals("LEARNED")) {
            reference.child("users").child(MainActivity.mAuth.getUid()).child("words").child(word).child("state")
                    .setValue(WordWrapper.states.get(WordWrapper.states.indexOf(wordWrapper.getState()) + 1));
            scheduleWorker(context, wordWrapper.getTitle(), Integer.parseInt(wordWrapper.getState()));
        }
        NotificationManagerCompat.from(context).cancel(bundle.getString("word").hashCode());
    }

    public void scheduleWorker(Context context, String word, int initialDelay) {
        Data data = new Data.Builder().putString("word", word).build();
        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWorker.class)
                .setInitialDelay(initialDelay, TimeUnit.SECONDS)
                .addTag(word)
                .setInputData(data)
                .build();
        WorkManager.getInstance(context).enqueue(notificationWork);
    }
}
