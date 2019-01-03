package com.davismiyashiro.expenses.view.opentab

import com.davismiyashiro.expenses.datatypes.Expense
import com.davismiyashiro.expenses.datatypes.Tab

/**
 * @Author Davis Miyashiro
 */

interface ExpenseInterfaces {
    interface ExpenseView {

        fun showExpenses(expenses: MutableList<Expense>)
    }

    interface UserActionsListener {

        fun setExpenseView(view: ExpenseView)

        fun loadExpenses(tab: Tab)

        fun removeExpense(expense: Expense)

    }
}
