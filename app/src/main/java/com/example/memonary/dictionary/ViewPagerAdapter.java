package com.example.memonary.dictionary;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.memonary.stats.StatsFragment;
import com.example.memonary.words.WordsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final int NUM_OF_TABS = 2;

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new DictionaryFragment();
        }
        return new WordsFragment();
    }

    @Override
    public int getItemCount() {
        return NUM_OF_TABS;
    }
}
