package com.davismiyashiro.expenses.injection

/**
 * @Author Davis Miyashiro
 */

import android.content.Context

import com.davismiyashiro.expenses.model.Repository
import com.davismiyashiro.expenses.model.RepositoryDataSource
import com.davismiyashiro.expenses.model.RepositoryImpl
import com.davismiyashiro.expenses.model.localrepo.RepositoryDataSourceLocal
import com.davismiyashiro.expenses.view.opentab.ExpenseFragmentPresenter
import com.davismiyashiro.expenses.view.opentab.ExpenseInterfaces
import com.davismiyashiro.expenses.view.opentab.ReceiptFragmentPresenter
import com.davismiyashiro.expenses.view.opentab.ReceiptInterfaces
import com.davismiyashiro.expenses.view.opentab.ParticipantInterfaces
import com.davismiyashiro.expenses.view.opentab.ParticipantFragmentPresenter

import dagger.Module
import dagger.Provides

@Module
class OpenTabModule {

    @Provides
    fun provideOpenTabActivityParticipantPresenter(model: Repository): ParticipantInterfaces.UserActionsListener {
        return ParticipantFragmentPresenter(model)
    }

    @Provides
    fun provideExpensePresenter(model: Repository): ExpenseInterfaces.UserActionsListener {
        return ExpenseFragmentPresenter(model)
    }

    @Provides
    fun provideReceiptPresenter(model: Repository): ReceiptInterfaces.UserActionsListener {
        return ReceiptFragmentPresenter(model)
    }

    @Provides
    fun provideTabRepositoryModel(dataSource: RepositoryDataSource): Repository {
        return RepositoryImpl(dataSource)
    }

    @Provides
    fun provideRepository(context: Context): RepositoryDataSource {
        return RepositoryDataSourceLocal(context)
    }
}
