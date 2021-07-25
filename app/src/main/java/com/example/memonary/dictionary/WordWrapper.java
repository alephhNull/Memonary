package com.example.memonary.dictionary;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
public class WordWrapper {

    public static final  List<String> states = Arrays.asList("1", "2", "4", "8", "15", "LEARNED");

    private Date dateStart;

    private Date dateEnd;

    private String title;

    private List<WordModel> words;

    private String state = states.get(0);

    private boolean isDue = false;

    public WordWrapper(String title, List<WordModel> words) {
        this.title = title;
        this.words = words;
        this.dateStart = new Date();
        this.dateEnd = new Date();
    }

    public WordWrapper() {

    }

    public boolean getIsDue() {
        return isDue;
    }

    public void setIsDue(boolean isDue) {
        this.isDue = isDue;
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

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart() {
        this.dateStart = Calendar.getInstance().getTime();
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd() {
        this.dateEnd = Calendar.getInstance().getTime();
    }
}
