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
