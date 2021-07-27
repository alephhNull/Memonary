package com.example.memonary.stats;

import android.text.format.DateUtils;

import androidx.core.util.TimeUtils;

import com.example.memonary.dictionary.WordModel;
import com.example.memonary.dictionary.WordWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Amirmehdi on 7/26/2021.
 */

public abstract class DummyStatsProvider {
    private DummyStatsProvider() {

    }

    public static List<WordWrapper> createDummyStats() {
        Random rand = new Random(System.currentTimeMillis());
        ArrayList<WordWrapper> words = new ArrayList<>();

        int monthsCount = 24;
        int startYear = 121;
        //Iterate through months
        for (int i = 0; i < monthsCount; i++) {
            Date date = new Date(startYear + i / 12, i % 12, rand.nextInt(30) + 1);
            int wordCount = rand.nextInt(30) + 20;
            for (int j = 0; j < wordCount; j++) {
                WordWrapper word = new WordWrapper("dummy", new ArrayList<>());
                word.setDateStart(date);
                word.setDateEnd(date);
                words.add(word);
            }
        }
        return words;
    }
}
