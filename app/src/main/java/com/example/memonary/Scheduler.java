package com.example.memonary;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.memonary.dictionary.WordModel;
import com.example.memonary.dictionary.WordState;
import com.google.type.DateTime;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    private static Scheduler instance = new Scheduler();
    private static final TimeUnit timeUnit = TimeUnit.SECONDS;

    private Scheduler() {
    }

    public static Scheduler getInstance() {
        return instance;
    }

    public void updateSchedule(WordModel word) {
        word.setState(word.getState().next());
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, 30);
        word.setDueTime(c.getTimeInMillis());
    }

    public void scheduleWorker(WordModel word) {
        long delay = word.getDueTime() - new Date().getTime();
        if (delay < 0)
            return;
        Data data = new Data.Builder().putString("wordId", word.getId()).putString("word", word.getWord()).build();
        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWorker.class)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag(word.getId())
                .setInputData(data)
                .build();
        WorkManager.getInstance(MyApplication.getContext()).enqueueUniqueWork(word.getId(), ExistingWorkPolicy.KEEP, notificationWork);
    }
}
