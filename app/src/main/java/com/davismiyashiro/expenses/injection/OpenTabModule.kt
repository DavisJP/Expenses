/*
 * MIT License
 *
 * Copyright (c) 2019 Davis Miyashiro
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
