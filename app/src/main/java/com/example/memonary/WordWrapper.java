package com.example.memonary;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class WordWrapper {

    private String title;

    private List<WordModel> words;


    public WordWrapper(String title, List<WordModel> words) {
        this.title = title;
        this.words = words;
    }

    private int state = 1;

    public String getTitle() {
        return title;
    }

    public List<WordModel> getWords() {
        return words;
    }

    public int getState() {
        return state;
    }
}
