package com.example.memonary.words;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.memonary.DatabaseManager;
import com.example.memonary.R;
import com.example.memonary.WordWrapperViewModel;
import com.example.memonary.dictionary.WordWrapper;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class WordsFragment extends Fragment implements OnWordSelectedListener {

    private RecyclerView savedWordsRecyclerView;
    private WordWrapperViewModel wordWrapperViewModel;
    private SavedWordsAdapter savedWordsAdapter;
    private ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_words, container, false);
        savedWordsRecyclerView = root.findViewById(R.id.recyclerSavedWords);
        savedWordsAdapter = new SavedWordsAdapter(getActivity(), this);
        wordWrapperViewModel = new ViewModelProvider(requireActivity()).get(WordWrapperViewModel.class);
        savedWordsRecyclerView.setAdapter(savedWordsAdapter);
        savedWordsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        savedWordsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Spinner spinner = getActivity().findViewById(R.id.spinner);
        spinner.setAdapter(new SpinnerAdapter(getContext(), getResources().getStringArray(R.array.spinner_array)));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                threadPoolExecutor.execute(() -> {
                    savedWordsAdapter.setFilter(i);
                    savedWordsAdapter.filterWords();
                });
//                if (MainActivity.savedWords == null)
//                    return;
//                List<WordWrapper> filteredWords = new ArrayList<>(MainActivity.savedWords.values());
//                if (i == 0)
//                    filteredWords = filteredWords.stream().filter(WordWrapper::getIsDue).collect(Collectors.toList());
//                else if (i < 7)
//                    filteredWords = filteredWords.stream().filter(wordWrapper -> wordWrapper.getState().equals(WordWrapper.states.get(i-1))).collect(Collectors.toList());
//                savedWordsAdapter.setSavedWords((ArrayList<WordWrapper>) filteredWords, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        threadPoolExecutor.execute(() -> savedWordsAdapter.filterWords());
    }

    @Override
    public void onWordSelected(WordWrapper wordWrapper) {
        wordWrapperViewModel.selectWord(wordWrapper);
    }

}

interface OnWordSelectedListener {
    void onWordSelected(WordWrapper wordWrapper);
}