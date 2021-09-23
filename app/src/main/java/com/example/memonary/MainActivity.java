package com.example.memonary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.example.memonary.authentication.LoginActivity;
import com.example.memonary.dictionary.WordModel;
import com.ferfalk.simplesearchview.SimpleSearchView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.memonary.dictionary.ViewPagerAdapter;
import com.example.memonary.dictionary.WordWrapper;

public class MainActivity extends AppCompatActivity {

    public static FirebaseAuth mAuth;
    public static DatabaseReference mDatabase;
    public static HashMap<String, WordWrapper> savedWords;
    public static HashMap<String, WordModel> words;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private SimpleSearchView simpleSearchView;
    private Toolbar toolbar;
    private WordWrapperViewModel viewModel;
    private Spinner spinner;
    private static final String[] TABS = {"Dictionary", "Words"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        viewModel = new ViewModelProvider(this).get(WordWrapperViewModel.class);
        viewModel.getSelectedWord().observe(this, wordWrapper -> viewPager.setCurrentItem(0));
        viewPager = findViewById(R.id.viewPager2);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected   (TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    spinner.setVisibility(View.GONE);
                    toolbar.setTitle(TABS[tab.getPosition()]);
                } else {
                    toolbar.setTitle(null);
                    spinner.setVisibility(View.VISIBLE);
                }
                invalidateOptionsMenu();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        simpleSearchView = findViewById(R.id.searchView);
        simpleSearchView.setTabLayout(tabLayout);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        spinner = findViewById(R.id.spinner);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(TABS[position])).attach();
        createNotificationChannel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        simpleSearchView.setMenuItem(item);
        if (viewPager.getCurrentItem() != 0)
            item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mAuth.signOut();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notification";
            String description = "";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        Intent i = new Intent(getApplicationContext(), CloseNotifications.class);
        startService(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isDestroyed())
            Log.d("testDestroy", "onDestoy called");
    }

}