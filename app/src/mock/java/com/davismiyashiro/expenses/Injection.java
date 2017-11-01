package com.davismiyashiro.expenses;

import android.content.Context;

import com.davismiyashiro.expenses.model.FakeTabRepositoryDataSourceLocal;
import com.davismiyashiro.expenses.model.InMemoryTabRepository;
import com.davismiyashiro.expenses.model.TabRepository;
import com.davismiyashiro.expenses.model.localrepo.TabRepositoryDataSourceLocal;

/**
 * Dependency injection of the mock TabRepositoryDataSourceLocal
 */
public class Injection {

    public static TabRepository provideTabsRepository(Context context) {
        return InMemoryTabRepository.getInstance(TabRepositoryDataSourceLocal.getInstance(context));
    }

}
