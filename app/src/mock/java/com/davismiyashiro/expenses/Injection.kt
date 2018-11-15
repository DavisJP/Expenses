package com.davismiyashiro.expenses

import android.content.Context

import com.davismiyashiro.expenses.model.FakeTabRepositoryDataSourceLocal
import com.davismiyashiro.expenses.model.InMemoryTabRepository
import com.davismiyashiro.expenses.model.TabRepository

/**
 * Dependency injection of the mock TabRepositoryDataSourceLocal
 */
object Injection {

    fun provideTabsRepository(context: Context): TabRepository {
        return InMemoryTabRepository.getInstance(FakeTabRepositoryDataSourceLocal.getInstance(context))
    }

}
