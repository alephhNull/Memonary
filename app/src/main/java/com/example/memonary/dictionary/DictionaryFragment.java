package com.example.memonary.dictionary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.memonary.MainActivity;
import com.example.memonary.R;
import com.example.memonary.WordWrapperViewModel;
import com.ferfalk.simplesearchview.SimpleSearchView;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DictionaryFragment extends Fragment {

    private RecyclerView recyclerViewResponse;
    private ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    private WordWrapperViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dictionary, container, false);
        recyclerViewResponse = root.findViewById(R.id.recyclerResponse);
        recyclerViewResponse.setAdapter(new ResponseAdapter(getContext(), this));
        recyclerViewResponse.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        viewModel = new ViewModelProvider(requireActivity()).get(WordWrapperViewModel.class);
        viewModel.getSelectedWord().observe(getViewLifecycleOwner(), this::show_word);
        if (viewModel.getSelectedWord().getValue() != null)
            show_word(viewModel.getSelectedWord().getValue());
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null && bundle.getString("word") != null) {
            show_word(MainActivity.savedWords.get(bundle.getString("word")));
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
            @Override
            public void onResponse(Call<List<WordModel>> call, Response<List<WordModel>> response) {
                WordWrapper wordWrapper = new WordWrapper(word, response.body());
                viewModel.selectWord(wordWrapper);
            }

            @Override
            public void onFailure(Call<List<WordModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Connection failed, please check your network", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void show_word(WordWrapper word) {
        ResponseAdapter adapter = (ResponseAdapter) recyclerViewResponse.getAdapter();
        adapter.setWordWrapper(word);
        adapter.notifyDataSetChanged();
    }

}