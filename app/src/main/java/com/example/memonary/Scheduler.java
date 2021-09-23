package com.example.memonary;

import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.memonary.dictionary.WordModel;
import com.example.memonary.dictionary.WordState;

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
        if (word.getState() == WordState.LEARNED)
            return;
        setDueTime(word);
    }

    public void resetSchedule(WordModel word) {
        word.setState(WordState.DAY1);
        setDueTime(word);
        
    }

    public void setDueTime(WordModel word) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, 10 *  word.getState().value);
        word.setDueTime(c.getTimeInMillis());
    }

    public void scheduleWorker(WordModel word, String id) {
        long delay = word.getDueTime() - new Date().getTime();
        if (delay < 0)
            return;
        Data data = new Data.Builder().putString("wordId", id).putString("word", word.getWord()).build();
        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWorker.class)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag(id)
                .setInputData(data)
                .build();
        WorkManager.getInstance(MyApplication.getContext()).enqueueUniqueWork(id, ExistingWorkPolicy.KEEP, notificationWork);
    }
}
