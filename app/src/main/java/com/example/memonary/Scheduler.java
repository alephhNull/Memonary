package com.example.memonary;

import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.memonary.dictionary.WordModel;
import com.example.memonary.dictionary.WordState;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    private static Scheduler instance = new Scheduler();

    private Scheduler() {
    }

    public static Scheduler getInstance() {
        return instance;
    }

    public void updateSchedule(WordModel word) {
        if (word.getState() == WordState.LEARNED)
            return;
        word.setState(word.getState().next());
        if (word.getState() != WordState.LEARNED) {
            setDueTime(word);
        }
    }

    public void resetSchedule(WordModel word) {
        word.setState(WordState.DAY1);
        setDueTime(word);
    }

    public void setDueTime(WordModel word) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, 10 + word.getState().value);
        word.setDueTime(c.getTimeInMillis());
    }

    public void scheduleWorker(WordModel word) {
        long delay = word.getDueTime() - new Date().getTime();
        if (delay < 0)
            return;
        Gson gson = new Gson();
        Data data = new Data.Builder().putString("word", gson.toJson(word)).build();
        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWorker.class)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag(word.getId())
                .setInputData(data)
                .build();
        WorkManager.getInstance(MyApplication.getContext()).enqueueUniqueWork(word.getId(), ExistingWorkPolicy.KEEP, notificationWork);
    }
}
