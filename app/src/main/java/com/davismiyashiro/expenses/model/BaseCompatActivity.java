package com.davismiyashiro.expenses.model;

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
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_exit:
//                finish();
//                //System.exit(0);
//                //return true;
//                break;
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//        }
//        return true;
//    }

    public void onStart() {
        super.onStart();
        Timber.d("onStart() event");
    }

    public void onRestart() {
        super.onRestart();
        Timber.d("onRestart() event");
    }

    public void onResume() {
        super.onResume();
        Timber.d("onResume() event");
    }

    public void onPause() {
        super.onPause();
        Timber.d("onPause() event");
    }

    public void onStop() {
        super.onStop();
        Timber.d("onStop() event");
    }

    public void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy() event");
    }
}
