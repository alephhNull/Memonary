package com.example.memonary;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Arrays;
import java.util.Random;

public class NotifyWorker extends Worker {

    private Context context;

    public NotifyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        triggerNotification();
        return Result.success();
    }

    public void triggerNotification() {
        String word = getInputData().getString("word");
        Intent[] intents = {new Intent(context, MainActivity.class),
                new Intent(context, ForgetBroadcast.class), new Intent(context, RememberBroadcast.class)};
        for (Intent intent : intents) {
            intent.putExtra("word", word);
        }
        PendingIntent showWord = PendingIntent.getActivity(context, word.hashCode(), intents[0], PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent forgetWord = PendingIntent.getBroadcast(context, word.hashCode(), intents[1], PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent rememberWord = PendingIntent.getBroadcast(context, word.hashCode(), intents[2], PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                .setContentTitle(word)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_search_black_24dp)
                .setContentIntent(showWord)
                .addAction(0, "Forgot", forgetWord)
                .addAction(0, "Remember", rememberWord);
        NotificationManagerCompat.from(context).notify(word.hashCode(), builder.build());
    }
}