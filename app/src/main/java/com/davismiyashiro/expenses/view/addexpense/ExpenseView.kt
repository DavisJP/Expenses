package com.davismiyashiro.expenses.view.addexpense

import com.davismiyashiro.expenses.datatypes.Expense

interface ExpenseView {
    interface View {
        fun setExpenseDescription(desc: String)
        fun setExpenseValue(value: String)
        fun setDescriptionError(value: Boolean)
        fun setValueError(value: Boolean)
        fun onSuccess()
        fun chooseParticipants(expense: Expense)
    }

    interface UserActionsListener {
        fun validateExpenseFields(description: String, value: String)
        fun addExpense(expense: Expense)
        fun updateExpense(expense: Expense)
    }
}
