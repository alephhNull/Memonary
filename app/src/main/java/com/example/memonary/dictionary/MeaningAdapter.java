package com.example.memonary.dictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memonary.R;

import java.util.List;

public class MeaningAdapter extends RecyclerView.Adapter<MeaningAdapter.ViewHolder> {

    List<Meaning> meanings;
    Context context;

    public MeaningAdapter(List<Meaning> meanings, Context context) {
        this.meanings = meanings;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_meaning, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meaning meaning = meanings.get(position);
        holder.partOfSpeech.setText(meaning.getPartOfSpeech());
        DefinitionAdapter definitionAdapter = new DefinitionAdapter(meaning.getDefinitions(), context);
        holder.definitions.setAdapter(definitionAdapter);
        holder.definitions.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public int getItemCount() {
        if (meanings == null)
            return 0;
        return meanings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView partOfSpeech;
        private RecyclerView definitions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            partOfSpeech = itemView.findViewById(R.id.partOfSpeech);
            definitions = itemView.findViewById(R.id.recylerDefinitions);
        }
    }
}
