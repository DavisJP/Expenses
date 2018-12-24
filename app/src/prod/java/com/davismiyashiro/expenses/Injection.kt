package com.davismiyashiro.expenses

import android.content.Context

import com.davismiyashiro.expenses.model.RepositoryImpl
import com.davismiyashiro.expenses.model.Repository
import com.davismiyashiro.expenses.model.localrepo.RepositoryDataSourceLocal

/**
 * Created by Davis on 25/01/2016.
 */
object Injection {

    fun provideTabsRepository(context: Context): Repository {
        return RepositoryImpl.getInstance(RepositoryDataSourceLocal.getInstance(context))
    }

}
