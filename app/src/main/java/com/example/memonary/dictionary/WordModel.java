package com.example.memonary.dictionary;

import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@IgnoreExtraProperties
public class WordModel {

    @SerializedName("word")
    private String word;

    @SerializedName("phonetics")
    private List<Phonetics> phonetics;

    @SerializedName("meanings")
    private List<Meaning> meanings;


    private WordState state = WordState.NOT_SAVED;

    private long dueTime;

    public String getWord() {
        return word;
    }

    public List<Phonetics> getPhonetics() {
        return phonetics;
    }

    private String json;

    public String getId() {
        return String.valueOf(json.hashCode());
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getJson() {
        return json;
    }

    public WordState getState() {
        return state;
    }

    @Override
    public String toString() {
        return "WordModel{" +
                "word='" + word + '\'' +
                ", phonetics=" + phonetics +
                ", meanings=" + meanings +
                '}';
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public void setState(WordState state) {
        this.state = state;
    }

    public long getDueTime() {
        return dueTime;
    }

    public void setDueTime(long dueTime) {
        this.dueTime = dueTime;
    }
}

class Phonetics {

    @SerializedName("text")
    private String text;

    @SerializedName("audio")
    private String audioUrl;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;

    }

    @Override
    public String toString() {
        return "Phonetics{" +
                "text='" + text + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                '}';
    }
}


class Meaning {

    @SerializedName("partOfSpeech")
    private String partOfSpeech;

    @SerializedName("definitions")
    private List<Definition> definitions;

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    @Override
    public String toString() {
        return "Meaning{" +
                "partOfSpeech='" + partOfSpeech + '\'' +
                ", definitions=" + definitions +
                '}';
    }
}

class Definition {

    @SerializedName("definition")
    private String definition;

    @SerializedName("synonyms")
    private List<String> synonyms;

    @SerializedName("antonyms")
    private List<String> antonyms;

    @SerializedName("example")
    private String example;

    public String getDefinition() {
        return definition;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public String getExample() {
        return example;
    }

    public List<String> getAntonyms() {
        return antonyms;
    }

    @Override
    public String toString() {
        return "Definition{" +
                "definition='" + definition + '\'' +
                ", synonyms=" + synonyms +
                ", antonyms=" + antonyms +
                ", example='" + example + '\'' +
                '}';
    }
}