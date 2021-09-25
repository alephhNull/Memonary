package com.example.memonary.words;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memonary.DatabaseManager;
import com.example.memonary.R;
import com.example.memonary.broadcasts.ForgetBroadcast;
import com.example.memonary.broadcasts.RememberBroadcast;
import com.example.memonary.dictionary.WordModel;
import com.example.memonary.dictionary.WordState;
import com.example.memonary.dictionary.WordWrapper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SavedWordsAdapter extends RecyclerView.Adapter<SavedWordsAdapter.ViewHolder> {

    ArrayList<WordWrapper> savedWords;
    List<WordModel> filteredWords;
    DatabaseManager dbManager;
    Activity activity;
    OnWordSelectedListener onWordSelectedListener;
    int filter;

    public SavedWordsAdapter(Activity activity, OnWordSelectedListener onWordSelectedListener) {
        this.activity = activity;
        this.onWordSelectedListener = onWordSelectedListener;
        dbManager = DatabaseManager.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_saved_word, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WordModel wordModel = filteredWords.get(position);
        holder.wordTextView.setText(wordModel.getWord());
        int visibility = wordModel.isDue() ? View.VISIBLE : View.INVISIBLE;
        holder.forgetButton.setVisibility(visibility);
        holder.rememberButton.setVisibility(visibility);
        holder.forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("position clicked", String.valueOf(position));
                Intent i = new Intent(activity, ForgetBroadcast.class);
                i.putExtra("word", new Gson().toJson(wordModel));
                activity.sendBroadcast(i);
                holder.forgetButton.setVisibility(View.INVISIBLE);
                holder.rememberButton.setVisibility(View.INVISIBLE);
                if (filter != 1 && filter != 7) {
                    int position = filteredWords.indexOf(wordModel);
                    filteredWords.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });
        holder.rememberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, RememberBroadcast.class);
                i.putExtra("word", new Gson().toJson(wordModel));
                activity.sendBroadcast(i);
                holder.forgetButton.setVisibility(View.INVISIBLE);
                holder.rememberButton.setVisibility(View.INVISIBLE);
                if (filter != 7) {
                    int position = filteredWords.indexOf(wordModel);
                    filteredWords.remove(position);
                    notifyItemRemoved(position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (filteredWords == null)
            return 0;
        return filteredWords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView wordTextView;
        private final ImageButton rememberButton;
        private final ImageButton forgetButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.savedWord);
            rememberButton = itemView.findViewById(R.id.rememberButton);
            forgetButton = itemView.findViewById(R.id.forgetButton);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            WordWrapper wordWrapper = savedWords.get(getAdapterPosition());
//            onWordSelectedListener.onWordSelected(wordWrapper);
        }
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filterWords() {
     if (filter == 0) {
         filteredWords = dbManager.getSavedWords().stream().filter(WordModel::isDue)
                 .collect(Collectors.toList());
     } else if(filter < 7) {
         filteredWords = dbManager.getSavedWords().stream().filter(word ->
                 word.getState()== WordState.values()[this.filter]).collect(Collectors.toList());
     } else if (filter == 7){
         filteredWords = dbManager.getSavedWords();
     }
     activity.runOnUiThread(this::notifyDataSetChanged);
    }
}
