package com.example.memonary;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class WordWrapper {

    public static final  List<String> states = Arrays.asList("1", "2", "4", "8", "15", "LEARNED");

    private String title;

    private List<WordModel> words;

    private String state = states.get(0);

    public WordWrapper(String title, List<WordModel> words) {
        this.title = title;
        this.words = words;
    }

    public WordWrapper() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWords(List<WordModel> words) {
        this.words = words;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<WordModel> getWords() {
        return words;
    }

}
