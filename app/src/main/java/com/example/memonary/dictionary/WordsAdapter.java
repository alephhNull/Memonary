package com.example.memonary.dictionary;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memonary.MainActivity;
import com.example.memonary.R;

import java.util.ArrayList;

import kotlin.Unit;
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder> {

    private ArrayList<WordModel> searchedWords;
    private Context context;
    private ArrayList<WordModel> testWords;


    public WordsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);
        View wordItem = inflater.inflate(R.layout.item_word, parent, false);
        return new ViewHolder(wordItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WordModel wordModel = searchedWords.get(position);
        holder.saveButton.setOnSelectListener((button) -> toggleSave(button, wordModel));
        holder.wordTitle.setText(wordModel.getWord());
        PronunciationAdapter pronunciationAdapter = new PronunciationAdapter((ArrayList<Phonetics>) wordModel.getPhonetics());
        holder.pronunciations.setAdapter(pronunciationAdapter);
        holder.pronunciations.setLayoutManager(new LinearLayoutManager(context));
        MeaningAdapter meaningAdapter = new MeaningAdapter(wordModel.getMeanings(), context);
        holder.meanings.setAdapter(meaningAdapter);
        holder.meanings.setLayoutManager(new LinearLayoutManager(context));
    }

    private Unit toggleSave(ThemedButton button, WordModel word) {
        if (!MainActivity.words.containsKey(word.getId())) {
            MainActivity.mDatabase.child("users").child(MainActivity.mAuth.getUid()).child("words").child(word.getId()).setValue(word);
        }
        return kotlin.Unit.INSTANCE;
    }

    @Override
    public int getItemCount() {
        if (searchedWords == null)
            return 0;
        return  searchedWords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //TODO
        private TextView wordTitle;
        private  ThemedToggleButtonGroup saveButton;
        private RecyclerView pronunciations;
        private RecyclerView meanings;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.wordTitle = itemView.findViewById(R.id.wordTitle);
            this.pronunciations = itemView.findViewById(R.id.recyclerPronunciations);
            this.meanings = itemView.findViewById(R.id.recyclerMeanings);
            this.saveButton = itemView.findViewById(R.id.save_button);
        }

    }

    public ArrayList<WordModel> getSearchedWords() {
        return searchedWords;
    }

    public void setSearchedWords(ArrayList<WordModel> searchedWords) {
        this.searchedWords = searchedWords;
        this.notifyDataSetChanged();
    }
}
