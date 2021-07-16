package com.example.memonary;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                .setContentTitle(word)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_search_black_24dp);
        NotificationManagerCompat.from(context).notify(word.hashCode(), builder.build());
    }
}