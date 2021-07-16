package com.example.memonary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResponseAdapter extends RecyclerView.Adapter<ResponseAdapter.ViewHolder> {

    private ArrayList<WordModel> words;
    private Context context;

    public void setWords(ArrayList<WordModel> words) {
        this.words = words;
    }

    public ResponseAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View responseItem =  inflater.inflate(R.layout.item_response, parent, false);
        return new ViewHolder(responseItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WordAdapter wordAdapter = new WordAdapter(words, context);
        holder.recyclerViewWords.setAdapter(wordAdapter);
        holder.recyclerViewWords.setLayoutManager(new LinearLayoutManager(context));
        wordAdapter.notifyDataSetChanged();
        holder.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = MainActivity.mDatabase;
                FirebaseUser user = MainActivity.mAuth.getCurrentUser();
                if (!MainActivity.savedWords.containsKey(words.get(0).getWord()))
                    reference.child("users").child(user.getUid()).child("words").child(words.get(0).getWord()).setValue(words);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (words == null)
            return 0;
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerViewWords;
        private Button saveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerViewWords = itemView.findViewById(R.id.recyclerWords);
            saveButton = itemView.findViewById(R.id.save_button);
        }
    }
}
