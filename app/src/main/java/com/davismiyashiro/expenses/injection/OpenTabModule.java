package com.davismiyashiro.expenses.injection;

/**
 * @Author Davis Miyashiro
 */

import android.content.Context;

import com.davismiyashiro.expenses.model.InMemoryTabRepository;
import com.davismiyashiro.expenses.model.TabRepository;
import com.davismiyashiro.expenses.model.TabRepositoryDataSource;
import com.davismiyashiro.expenses.model.localrepo.TabRepositoryDataSourceLocal;
import com.davismiyashiro.expenses.view.opentab.ExpenseFragmentPresenter;
import com.davismiyashiro.expenses.view.opentab.ExpenseInterfaces;
import com.davismiyashiro.expenses.view.opentab.ReceiptFragmentPresenter;
import com.davismiyashiro.expenses.view.opentab.ReceiptInterfaces;
import com.davismiyashiro.expenses.view.opentab.ParticipantInterfaces;
import com.davismiyashiro.expenses.view.opentab.ParticipantFragmentPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class OpenTabModule {

    @Provides
    public ParticipantInterfaces.UserActionsListener provideOpenTabActivityParticipantPresenter (TabRepository model) {
        return new ParticipantFragmentPresenter(model);
    }

    @Provides
    public ExpenseInterfaces.UserActionsListener provideExpensePresenter (TabRepository model) {
        return new ExpenseFragmentPresenter(model);
    }

    @Provides
    public ReceiptInterfaces.UserActionsListener provideReceiptPresenter (TabRepository model) {
        return new ReceiptFragmentPresenter(model);
    }

    @Provides
    public TabRepository provideTabRepositoryModel (TabRepositoryDataSource dataSource) {
        return new InMemoryTabRepository(dataSource);
    }

    @Provides
    public TabRepositoryDataSource provideRepository (Context context) {
        return new TabRepositoryDataSourceLocal(context);
    }
}
