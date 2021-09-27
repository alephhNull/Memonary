package com.example.memonary;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.memonary.dictionary.WordModel;

public class WordViewModel extends ViewModel {
    private MutableLiveData<WordModel> selectedWord = new MutableLiveData<>();

    public void setSelectedWord(WordModel word) {
        this.selectedWord.setValue(word);
    }

    public MutableLiveData<WordModel> getSelectedWord() {
        return this.selectedWord;
    }
}
