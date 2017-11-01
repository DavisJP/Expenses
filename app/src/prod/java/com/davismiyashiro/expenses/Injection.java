package com.davismiyashiro.expenses;

import android.content.Context;

import com.davismiyashiro.expenses.model.InMemoryTabRepository;
import com.davismiyashiro.expenses.model.TabRepository;
import com.davismiyashiro.expenses.model.localrepo.TabRepositoryDataSourceLocal;

/**
 * Created by Davis on 25/01/2016.
 */
public class Injection {

    public static TabRepository provideTabsRepository(Context context) {
        return InMemoryTabRepository.getInstance(TabRepositoryDataSourceLocal.getInstance(context));
    }

}
