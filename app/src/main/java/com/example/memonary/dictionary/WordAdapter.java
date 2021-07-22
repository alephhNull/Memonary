package com.example.memonary.dictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memonary.R;

import java.util.ArrayList;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {

    private ArrayList<WordModel> searchedWords;
    private Context context;


    public WordAdapter(ArrayList<WordModel> searchedWords, Context context) {
        this.searchedWords = searchedWords;
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
        holder.wordTitle.setText(wordModel.getWord());
        PronunciationAdapter adapter = new PronunciationAdapter((ArrayList<Phonetics>) wordModel.getPhonetics());
        holder.pronunciations.setAdapter(adapter);
        holder.pronunciations.setLayoutManager(new LinearLayoutManager(context));
        StringBuilder meaningStr = new StringBuilder();
        for (Meaning meaning : wordModel.getMeanings()) {
            meaningStr.append("part of speech:").append(meaning.getPartOfSpeech()).append("\n");
            for (Definition definition : meaning.getDefinitions()) {
                meaningStr.append("definition:\n").append(definition.getDefinition()).append("\n");
                meaningStr.append("example:\n").append(definition.getExample()).append("\n");
                if (definition.getSynonyms() != null) {
                    meaningStr.append("synonyms:\n");
                    for (String synonym : definition.getSynonyms()) {
                        meaningStr.append(synonym).append("\n");
                    }
                }
            }
        }
        holder.meaning.setText(meaningStr);
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
        private RecyclerView pronunciations;
        private TextView meaning;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.wordTitle = itemView.findViewById(R.id.wordTitle);
            this.pronunciations = itemView.findViewById(R.id.recyclerPronunciations);
            this.meaning = itemView.findViewById(R.id.textView_meaning);
        }

    }

    public ArrayList<WordModel> getSearchedWords() {
        return searchedWords;
    }

    public void setSearchedWords(ArrayList<WordModel> searchedWords) {
        this.searchedWords = searchedWords;
    }
}
