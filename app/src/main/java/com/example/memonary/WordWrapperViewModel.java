package com.example.memonary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.memonary.dictionary.WordWrapper;

public class WordWrapperViewModel extends ViewModel {
    private final MutableLiveData<WordWrapper> selectedWord = new MutableLiveData<>();

    public void selectWord(WordWrapper wordWrapper) {
        selectedWord.setValue(wordWrapper);
    }

    public MutableLiveData<WordWrapper> getSelectedWord() {
        return selectedWord;
    }
}
