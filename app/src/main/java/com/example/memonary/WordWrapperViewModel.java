package com.example.memonary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.memonary.dictionary.WordModel;
import com.example.memonary.dictionary.WordWrapper;

import java.util.List;

public class WordWrapperViewModel extends ViewModel {
    private final MutableLiveData<WordWrapper> selectedWord = new MutableLiveData<>();
    private final MutableLiveData<List<WordModel>> savedWords = new MutableLiveData<>();

    public void selectWord(WordWrapper wordWrapper) {
        selectedWord.setValue(wordWrapper);
    }

    public void setSavedWords(List<WordModel> savedWords) {
        this.savedWords.setValue(savedWords);
    }

    public MutableLiveData<WordWrapper> getSelectedWord() {
        return selectedWord;
    }
}
