package com.davismiyashiro.expenses.view.addexpense

import android.text.TextUtils

import com.davismiyashiro.expenses.datatypes.Expense
import com.davismiyashiro.expenses.model.Repository

class ExpensePresenterImpl(private val mView: ExpenseView.View, internal var mRepo: Repository) : ExpenseView.UserActionsListener {

    override fun validateExpenseFields(description: String, value: String) {
        var error = false
        if (TextUtils.isEmpty(description)) {
            error = true
            mView.setDescriptionError(true)
        } else {
            mView.setDescriptionError(false)
        }

        if (TextUtils.isEmpty(value)) {
            error = true
            mView.setValueError(true)
        } else {
            mView.setValueError(false)
        }
        if (!error)
            mView.onSuccess()
    }

    override fun addExpense(expense: Expense) {
        mRepo.saveExpense(expense)
    }

    override fun updateExpense(expense: Expense) {
        mRepo.updateExpense(expense)
    }
}
