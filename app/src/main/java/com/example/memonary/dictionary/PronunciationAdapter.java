package com.example.memonary.dictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memonary.MainActivity;
import com.example.memonary.R;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class PronunciationAdapter extends RecyclerView.Adapter<PronunciationAdapter.ViewHolder> {

    ArrayList<Phonetics> pronunciations;
    Context context;
    public DictionaryFragment dictionaryFragment;

    public PronunciationAdapter(ArrayList<Phonetics> pronunciations, Context context, DictionaryFragment dictionaryFragment) {
        this.pronunciations = pronunciations;
        this.context = context;
        this.dictionaryFragment = dictionaryFragment;
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
        holder.url = pronunciations.get(position).getAudioUrl();
        //TODO load audio
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
        private String url;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            phonetic = itemView.findViewById(R.id.phoneticText);
            audioButton = itemView.findViewById(R.id.audioButton);
            audioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(url);
                    Executors.newFixedThreadPool(5).execute(AudioManager.getInstance(url));

                }
            });
        }
    }
}
