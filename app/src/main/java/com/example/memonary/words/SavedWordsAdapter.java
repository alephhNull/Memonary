package com.example.memonary.words;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memonary.R;
import com.example.memonary.broadcasts.ForgetBroadcast;
import com.example.memonary.broadcasts.RememberBroadcast;
import com.example.memonary.dictionary.WordWrapper;

import java.util.ArrayList;

public class SavedWordsAdapter extends RecyclerView.Adapter<SavedWordsAdapter.ViewHolder> {

    ArrayList<WordWrapper> savedWords;
    Context context;
    int state;

    public SavedWordsAdapter(Context context) {
        this.context = context;
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
        WordWrapper wordWrapper = savedWords.get(position);
        holder.wordTextView.setText(wordWrapper.getTitle());
        int visibility = wordWrapper.getIsDue() ? View.VISIBLE : View.INVISIBLE;
        holder.forgetButton.setVisibility(visibility);
        holder.rememberButton.setVisibility(visibility);
        holder.forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ForgetBroadcast.class);
                i.putExtra("word", wordWrapper.getTitle());
                context.sendBroadcast(i);
                holder.forgetButton.setVisibility(View.INVISIBLE);
                holder.rememberButton.setVisibility(View.INVISIBLE);
                if (state != 1 && state != 7) {
                    savedWords.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });
        holder.rememberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, RememberBroadcast.class);
                i.putExtra("word", wordWrapper.getTitle());
                context.sendBroadcast(i);
                holder.forgetButton.setVisibility(View.INVISIBLE);
                holder.rememberButton.setVisibility(View.INVISIBLE);
                if (state != 7) {
                    savedWords.remove(position);
                    notifyItemRemoved(position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (savedWords == null)
            return 0;
        return savedWords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView wordTextView;
        private ImageButton rememberButton;
        private ImageButton forgetButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.savedWord);
            rememberButton = itemView.findViewById(R.id.rememberButton);
            forgetButton = itemView.findViewById(R.id.forgetButton);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    public void setSavedWords(ArrayList<WordWrapper> savedWords, int state) {
        this.savedWords = savedWords;
        this.state = state;
        notifyDataSetChanged();
    }
}
