package com.davismiyashiro.expenses.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.davismiyashiro.expenses.BuildConfig;
import com.davismiyashiro.expenses.R;
import com.davismiyashiro.expenses.view.managetabs.ChooseTabsActivity;
import com.davismiyashiro.expenses.model.BaseCompatActivity;

import timber.log.Timber;

public class MainActivity extends BaseCompatActivity implements View.OnClickListener, MainView {

    public static final int FINISH_TIMER = 1 * 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                startApp();
            }
        }, FINISH_TIMER);
    }

    @Override
    public void startApp() {
        Intent intent = new Intent(this, ChooseTabsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        startApp();
    }
}
