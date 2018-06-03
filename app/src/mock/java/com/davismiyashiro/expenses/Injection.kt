package com.davismiyashiro.expenses

import android.content.Context

import com.davismiyashiro.expenses.model.FakeRepositoryDataSourceLocal
import com.davismiyashiro.expenses.model.RepositoryImpl
import com.davismiyashiro.expenses.model.Repository

/**
 * Dependency injection of the mock RepositoryDataSourceLocal
 */
object Injection {

    fun provideTabsRepository(context: Context): Repository {
        return RepositoryImpl.getInstance(FakeRepositoryDataSourceLocal.getInstance(context))
    }

}
