package com.davismiyashiro.expenses.injection;

import android.app.Application;

/**
 * @Author Davis Miyashiro
 */

public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .openTabModule(new OpenTabModule())
                .build();
    }

    public ApplicationComponent getComponent () {
        return component;
    }
}
