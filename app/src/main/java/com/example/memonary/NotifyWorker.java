package com.example.memonary;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.memonary.broadcasts.ForgetBroadcast;
import com.example.memonary.broadcasts.RememberBroadcast;
import com.example.memonary.dictionary.WordModel;
import com.google.gson.Gson;

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
        Gson gson = new Gson();
        String jsonWord = getInputData().getString("word");
        WordModel word = gson.fromJson(jsonWord, WordModel.class);
        Intent[] intents = {new Intent(context, MainActivity.class),
                new Intent(context, ForgetBroadcast.class), new Intent(context, RememberBroadcast.class)};
        for (Intent intent : intents) {
            intent.putExtra("word", jsonWord);
        }

        PendingIntent showWord = PendingIntent.getActivity(context, Integer.parseInt(word.getId()),
                intents[0], PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent forgetWord = PendingIntent.getBroadcast(context, Integer.parseInt(word.getId()),
                intents[1], PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent rememberWord = PendingIntent.getBroadcast(context, Integer.parseInt(word.getId()),
                intents[2], PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                .setContentTitle(word.getWord())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_book_of_black_cover_closed)
                .setContentIntent(showWord)
                .addAction(0, "Forgot", forgetWord)
                .addAction(0, "Remember", rememberWord);
        NotificationManagerCompat.from(context).notify(Integer.parseInt(word.getId()), builder.build());
    }
}