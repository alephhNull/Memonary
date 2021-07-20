package com.example.memonary.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.memonary.MainActivity;
import com.example.memonary.NotifyWorker;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.example.memonary.dictionary.WordWrapper;

public class RememberBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String word = bundle.getString("word");
        DatabaseReference reference = MainActivity.mDatabase;
        WordWrapper wordWrapper = MainActivity.savedWords.get(word);
        if (wordWrapper != null) {
            int currentState = WordWrapper.states.indexOf(wordWrapper.getState());
            Map<String, Object> children = new HashMap<>();
            children.put("state", WordWrapper.states.get(currentState + 1));
            children.put("isDue", false);
            reference.child("users").child(MainActivity.mAuth.getUid()).child("words").child(word)
                    .updateChildren(children);
            if (currentState != 4)
                scheduleWorker(context, word, Integer.parseInt(WordWrapper.states.get(currentState + 1)));
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
