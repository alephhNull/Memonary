package com.example.memonary.dictionary;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.memonary.DatabaseManager;
import com.example.memonary.R;
import com.example.memonary.WordViewModel;
import com.example.memonary.WordWrapperViewModel;
import com.ferfalk.simplesearchview.SimpleSearchView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DictionaryFragment extends Fragment {

    private RecyclerView recyclerViewWords;
    private ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    private WordViewModel viewModel;
    private DatabaseManager dbManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dictionary, container, false);
        dbManager = DatabaseManager.getInstance();
        recyclerViewWords = root.findViewById(R.id.recyclerWords);
        recyclerViewWords.setAdapter(new WordsAdapter(getContext()));
        recyclerViewWords.setLayoutManager(new LinearLayoutManager(getContext()) {
        });
        viewModel = new ViewModelProvider(requireActivity()).get(WordViewModel.class);
        viewModel.getSelectedWord().observe(getViewLifecycleOwner(), this::show_word);
        if (viewModel.getSelectedWord().getValue() != null)
            show_word(viewModel.getSelectedWord().getValue());
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null && bundle.getString("word") != null) {
            Gson gson = new Gson();
            show_word(gson.fromJson(bundle.getString("word"), WordModel.class));
        }
        SimpleSearchView simpleSearchView = getActivity().findViewById(R.id.searchView);
        simpleSearchView.setOnQueryTextListener(new SimpleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                threadPoolExecutor.execute(() -> perform_search(s));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextCleared() {
                return false;
            }
        });
        return root;
    }

    public void perform_search(String word) {
        API_Interface searchService= RetrofitClient.getRetrofitInstance().create(API_Interface.class);
        Call<List<WordModel>> call = searchService.getResults(word);
        call.enqueue(new Callback<List<WordModel>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<WordModel>> call, Response<List<WordModel>> response) {
                ArrayList<WordModel> words = new ArrayList<>();
                WordsAdapter adapter = (WordsAdapter) recyclerViewWords.getAdapter();
                if (response.body() != null)
                    words = (ArrayList<WordModel>) response.body().stream()
                        .map(word -> fill(word)).collect(Collectors.toList());
                adapter.setSearchedWords(words);
            }

            @Override
            public void onFailure(Call<List<WordModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Connection failed, please check your network", Toast.LENGTH_LONG).show();
            }
        });
    }

    public WordModel fill(WordModel wordModel) {
        wordModel.setStringified(wordModel.toString());
        if (dbManager.isSaved(wordModel.getId()))
            return dbManager.getWordById(wordModel.getId());
        else
            return wordModel;
    }

    public void show_word(WordModel word) {
        WordsAdapter adapter = (WordsAdapter) recyclerViewWords.getAdapter();
        ArrayList<WordModel> wordModels = new ArrayList<>();
        wordModels.add(word);
        adapter.setSearchedWords(wordModels);
    }

}