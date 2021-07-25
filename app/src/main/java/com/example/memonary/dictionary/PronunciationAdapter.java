package com.example.memonary.dictionary;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memonary.R;

import java.io.IOException;
import java.util.ArrayList;

public class PronunciationAdapter extends RecyclerView.Adapter<PronunciationAdapter.ViewHolder> {

    ArrayList<Phonetics> pronunciations;

    public PronunciationAdapter(ArrayList<Phonetics> pronunciations) {
        this.pronunciations = pronunciations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View pronunciationItem =  inflater.inflate(R.layout.item_pronunciation, parent, false);
        return new ViewHolder(pronunciationItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.phonetic.setText(pronunciations.get(position).getText());
        holder.audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudioFromUrl(pronunciations.get(position).getAudioUrl());
            }
        });
    }

    public void playAudioFromUrl(String url){
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if (pronunciations == null)
            return 0;
        return pronunciations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView phonetic;
        private ImageButton audioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            phonetic = itemView.findViewById(R.id.phoneticText);
            audioButton = itemView.findViewById(R.id.audioButton);
        }
    }
}
