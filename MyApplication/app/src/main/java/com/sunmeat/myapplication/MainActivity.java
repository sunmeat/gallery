package com.sunmeat.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

// АКТИВИТИ С ГАЛЕРЕЕЙ

public class MainActivity extends AppCompatActivity {

    public static int currentPosition;
    private static final String POSITION = "currentPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(POSITION, 0);
            // Return here to prevent adding additional GridFragments when changing orientation.
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, new GridFragment(), GridFragment.class.getSimpleName())
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, currentPosition);
    }
}