package com.example.memonary;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.memonary.dictionary.WordModel;
import com.example.memonary.dictionary.WordState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DatabaseManager {

    private static DatabaseManager instance = new DatabaseManager();
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private HashMap<String, WordModel> savedWords;
    private Scheduler scheduler = Scheduler.getInstance();

    private DatabaseManager() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        this.initializeDataBase();
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    private void initializeDataBase() {
        mDatabase.child("users").child(mAuth.getUid()).child("words").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                savedWords = new HashMap<>();
                for (DataSnapshot snapchild : snapshot.getChildren()) {
                    WordModel word = snapchild.getValue(WordModel.class);
                    savedWords.put(snapchild.getKey(), word);
                    scheduler.scheduleWorker(word);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addWord(WordModel word) {
        mDatabase.child("users").child(mAuth.getUid()).child("words").child(word.getId()).setValue(word);
    }

    public void removeWord(WordModel word) {
        mDatabase.child("users").child(mAuth.getUid()).child("words").child(word.getId()).removeValue();
    }

    public boolean isSaved(WordModel word) {
        return  this.savedWords.containsKey(word.getId());
    }

    public void setState(WordModel word, WordState state) {
        mDatabase.child("users").child(mAuth.getUid()).child("words").child(word.getId()).child("state").setValue(state);
    }

    public void setDueDate(WordModel word, Date dueDate) {
        mDatabase.child("users").child(mAuth.getUid()).child("words").child(word.getId()).child("dueDate").setValue(dueDate);
    }
}
