package com.davismiyashiro.expenses.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.davismiyashiro.expenses.R;

import timber.log.Timber;

public abstract class BaseCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate() event");
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d("onStart() event");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Timber.d("onRestart() event");
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume() event");
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("onPause() event");
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.d("onStop() event");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy() event");
    }
}
