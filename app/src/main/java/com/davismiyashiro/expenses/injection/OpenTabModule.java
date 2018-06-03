package com.davismiyashiro.expenses.injection;

/**
 * @Author Davis Miyashiro
 */

import android.content.Context;

import com.davismiyashiro.expenses.model.Repository;
import com.davismiyashiro.expenses.model.RepositoryDataSource;
import com.davismiyashiro.expenses.model.RepositoryImpl;
import com.davismiyashiro.expenses.model.localrepo.RepositoryDataSourceLocal;
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
    public ParticipantInterfaces.UserActionsListener provideOpenTabActivityParticipantPresenter (Repository model) {
        return new ParticipantFragmentPresenter(model);
    }

    @Provides
    public ExpenseInterfaces.UserActionsListener provideExpensePresenter (Repository model) {
        return new ExpenseFragmentPresenter(model);
    }

    @Provides
    public ReceiptInterfaces.UserActionsListener provideReceiptPresenter (Repository model) {
        return new ReceiptFragmentPresenter(model);
    }

    @Provides
    public Repository provideTabRepositoryModel (RepositoryDataSource dataSource) {
        return new RepositoryImpl(dataSource);
    }

    @Provides
    public RepositoryDataSource provideRepository (Context context) {
        return new RepositoryDataSourceLocal(context);
    }
}
