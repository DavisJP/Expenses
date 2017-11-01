package com.davismiyashiro.expenses.injection;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Davis Miyashiro
 */

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule (Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext () {
        return application;
    }
}
