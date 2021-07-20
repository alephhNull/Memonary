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

public class ForgetBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String word = bundle.getString("word");
        if (MainActivity.savedWords.get(word) != null) {
            DatabaseReference reference = MainActivity.mDatabase;
            Map<String, Object> children = new HashMap<>();
            children.put("state", "1");
            children.put("isDue", false);
            reference.child("users").child(MainActivity.mAuth.getUid()).child("words").child(word).updateChildren(children);
            Data data = new Data.Builder().putString("word", word).build();
            OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWorker.class)
                    .setInitialDelay(1, TimeUnit.SECONDS)
                    .addTag(word)
                    .setInputData(data)
                    .build();
            WorkManager.getInstance(context).enqueue(notificationWork);
        }
        NotificationManagerCompat.from(context).cancel(word.hashCode());
    }

}
