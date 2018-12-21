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
package com.davismiyashiro.expenses.view.opentab

import com.davismiyashiro.expenses.datatypes.Expense
import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.model.Repository

/**
 * @Author Davis Miyashiro
 */

class ExpenseFragmentPresenter(internal var mRepository: Repository) : ExpenseInterfaces.UserActionsListener {

    lateinit var mExpenseView: ExpenseInterfaces.ExpenseView

    override fun setExpenseView(view: ExpenseInterfaces.ExpenseView) {
        mExpenseView = view
    }

    override fun loadExpenses(tab: Tab) {
        if (tab != null) {
            mRepository.refreshData() // Fix to get Expenses list updated when switching tabs
            mRepository.getExpenses(tab.groupId, object : Repository.LoadExpensesCallback {
                override fun onExpensesLoaded(expenses: MutableList<Expense>) {
                    mExpenseView.showExpenses(expenses)
                }
            })
        }
    }

    override fun removeExpense(expense: Expense) {
        mRepository.deleteExpense(expense)
    }
}
