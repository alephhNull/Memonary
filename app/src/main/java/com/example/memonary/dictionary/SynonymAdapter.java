package com.example.memonary.dictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memonary.R;

import java.util.List;

public class SynonymAdapter extends RecyclerView.Adapter<SynonymAdapter.ViewHolder>{

    List<String> synonyms;

    public SynonymAdapter(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_synonym, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.synonyms.setText(synonyms.get(position));
    }

    @Override
    public int getItemCount() {
        if (synonyms == null)
            return 0;
        return synonyms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView synonyms;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.synonyms = itemView.findViewById(R.id.synonymTextView);
        }
    }
}
