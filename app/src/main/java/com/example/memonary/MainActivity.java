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

import java.util.HashMap;
import java.util.concurrent.Executors;

import com.example.memonary.dictionary.ViewPagerAdapter;
import com.example.memonary.dictionary.WordWrapper;

public class MainActivity extends AppCompatActivity {

    public static FirebaseAuth mAuth;
    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference mDatabase;
    public static HashMap<String, WordWrapper> savedWords;
    private ViewPager2 viewPager2;
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
        viewModel.getSelectedWord().observe(this, wordWrapper -> viewPager2.setCurrentItem(0));
        viewPager2 = findViewById(R.id.viewPager2);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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
        viewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(TABS[position])).attach();
        createNotificationChannel();
        Log.d("test", "onCreate called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        } else {
            Executors.newSingleThreadExecutor().execute(this::initialize_database);
        }
        Log.d("test", "onStart called");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        simpleSearchView.setMenuItem(item);
        if (viewPager2.getCurrentItem() != 0)
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

    private void initialize_database() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference();
        mDatabase.child("users").child(mAuth.getUid()).child("words").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                savedWords = new HashMap<>();
                for (DataSnapshot snapchild : snapshot.getChildren()) {
                    WordWrapper wordWrapper = snapchild.getValue(WordWrapper.class);
                    savedWords.put(wordWrapper.getTitle(), wordWrapper);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
    }

}