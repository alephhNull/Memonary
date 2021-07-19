package com.example.memonary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.TimeUnit;

public class ForgetBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String word = bundle.getString("word");
        if (MainActivity.savedWords.get(word) != null) {
            DatabaseReference reference = MainActivity.mDatabase;
            reference.child("users").child(MainActivity.mAuth.getUid()).child("words").child(word).child("state")
                    .setValue("1");
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
