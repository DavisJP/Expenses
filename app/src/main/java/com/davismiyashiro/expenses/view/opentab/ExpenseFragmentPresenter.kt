package com.davismiyashiro.expenses.view.opentab

import com.davismiyashiro.expenses.datatypes.Expense
import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.model.Repository

import java.util.ArrayList

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
