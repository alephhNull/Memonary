package com.example.memonary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ResponseAdapter extends RecyclerView.Adapter<ResponseAdapter.ViewHolder> {

    private WordWrapper wordWrapper;
    private Context context;

    public void setWordWrapper(WordWrapper wordWrapper) {
        this.wordWrapper = wordWrapper;
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
        WordAdapter wordAdapter = new WordAdapter((ArrayList<WordModel>) wordWrapper.getWords(), context);
        holder.saveButton.setChecked(MainActivity.savedWords.containsKey(wordWrapper.getTitle()));
        holder.recyclerViewWords.setAdapter(wordAdapter);
        holder.recyclerViewWords.setLayoutManager(new LinearLayoutManager(context));
        wordAdapter.notifyDataSetChanged();
        holder.saveButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DatabaseReference reference = MainActivity.mDatabase;
                FirebaseUser user = MainActivity.mAuth.getCurrentUser();
                if (b) {
                    if (!MainActivity.savedWords.containsKey(wordWrapper.getTitle())) {
                        reference.child("users").child(user.getUid()).child("words").child(wordWrapper.getTitle()).setValue(wordWrapper);
                        scheduleWorker(wordWrapper.getTitle());
                    }
                } else {
                    reference.child("users").child(user.getUid()).child("words").child(wordWrapper.getTitle()).removeValue();
                    WorkManager.getInstance(context).cancelAllWorkByTag(wordWrapper.getTitle());
                }
            }
        });
    }

    public void scheduleWorker(String word) {
        Data data = new Data.Builder().putString("word", word).build();
        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWorker.class)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .addTag(word)
                .setInputData(data)
                .build();
        WorkManager.getInstance(context).enqueue(notificationWork);
    }

    @Override
    public int getItemCount() {
        if (wordWrapper == null || wordWrapper.getWords() == null)
            return 0;
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerViewWords;
        private ToggleButton saveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerViewWords = itemView.findViewById(R.id.recyclerWords);
            saveButton = itemView.findViewById(R.id.save_button);
        }
    }
}
