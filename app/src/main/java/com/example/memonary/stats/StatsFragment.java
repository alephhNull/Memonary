package com.example.memonary.stats;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.memonary.MainActivity;
import com.example.memonary.R;
import com.example.memonary.dictionary.WordWrapper;
import java.util.HashMap;


public class StatsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        HashMap<String, WordWrapper> map = MainActivity.savedWords;
        if (MainActivity.savedWords != null) {
            System.out.println(map.size());
            for (String s : map.keySet()) {
                //if (map.get(s).getState().equals("LEARNED")) {
                    System.out.println(s + "\t" + MainActivity.savedWords.get(s).getDateStart());
                //}
            }
        }
    }
}