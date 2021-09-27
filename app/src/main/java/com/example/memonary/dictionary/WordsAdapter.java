package com.example.memonary.dictionary;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import com.example.memonary.DatabaseManager;
import com.example.memonary.R;
import com.example.memonary.Scheduler;

import java.util.ArrayList;
import java.util.UUID;

import kotlin.Unit;
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder> {

    private ArrayList<WordModel> searchedWords;
    private Context context;
    private DatabaseManager dbManager;
    private Scheduler scheduler;


    public WordsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);
        View wordItem = inflater.inflate(R.layout.item_word, parent, false);
        dbManager = DatabaseManager.getInstance();
        scheduler = Scheduler.getInstance();
        return new ViewHolder(wordItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WordModel wordModel = searchedWords.get(position);
        holder.saveButton.setChecked(wordModel.getState() != WordState.NOT_SAVED);
        holder.saveButton.setOnClickListener((view) -> toggleSave(wordModel, view));
        holder.wordTitle.setText(wordModel.getWord());
        PronunciationAdapter pronunciationAdapter = new PronunciationAdapter((ArrayList<Phonetics>) wordModel.getPhonetics());
        holder.pronunciations.setAdapter(pronunciationAdapter);
        holder.pronunciations.setLayoutManager(new LinearLayoutManager(context));
        MeaningAdapter meaningAdapter = new MeaningAdapter(wordModel.getMeanings(), context);
        holder.meanings.setAdapter(meaningAdapter);
        holder.meanings.setLayoutManager(new LinearLayoutManager(context));
    }

    private void toggleSave(WordModel word, View view) {
        SwitchCompat btn = (SwitchCompat) view;
        if (word.getState() == WordState.NOT_SAVED) saveWord(word);
        else {
            btn.setChecked(true);
            new AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to remove this word? \n" +
                            "all progress will be lost")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        removeWord(word);
                        btn.setChecked(false);
                    })
                    .create()
                    .show();
        }
    }

    private void saveWord(WordModel word) {
        scheduler.updateSchedule(word);
        dbManager.addWord(word);
    }

    private void removeWord(WordModel word) {
        dbManager.removeWord(word);
        WorkManager.getInstance(context).cancelAllWorkByTag(word.getId());
        NotificationManagerCompat.from(context).cancel(Integer.parseInt(word.getId()));
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
        private SwitchCompat saveButton;
        private RecyclerView pronunciations;
        private RecyclerView meanings;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.wordTitle = itemView.findViewById(R.id.wordTitle);
            this.pronunciations = itemView.findViewById(R.id.recyclerPronunciations);
            this.meanings = itemView.findViewById(R.id.recyclerMeanings);
            this.saveButton = itemView.findViewById(R.id.saveButton);
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
