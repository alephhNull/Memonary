package com.example.memonary.dictionary;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

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

    public String getWord() {
        return word;
    }

    public List<Phonetics> getPhonetics() {
        return phonetics;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordModel wordModel = (WordModel) o;
        return word.equals(wordModel.word) &&
                Objects.equals(phonetics, wordModel.phonetics) &&
                meanings.equals(wordModel.meanings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, phonetics, meanings);
    }

    public List<Meaning> getMeanings() {
        return meanings;
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
}

class Definition {

    @SerializedName("definition")
    private String definition;

    @SerializedName("synonyms")
    private List<String> synonyms;

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
}