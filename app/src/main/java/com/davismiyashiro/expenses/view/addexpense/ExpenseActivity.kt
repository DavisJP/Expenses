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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputLayout
import android.view.View
import android.view.WindowManager
import android.widget.EditText

import com.davismiyashiro.expenses.Injection
import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.view.BaseCompatActivity
import com.davismiyashiro.expenses.R
import com.davismiyashiro.expenses.datatypes.Expense
import com.davismiyashiro.expenses.view.splitter.SplitterActivity

import timber.log.Timber

class ExpenseActivity : BaseCompatActivity(), ExpenseView.View, View.OnClickListener {

    private lateinit var mInputLayoutDesc: TextInputLayout
    private lateinit var mEditTextDescription: EditText
    private lateinit var mInputLayoutValue: TextInputLayout
    private lateinit var mEditTextValue: EditText
    private lateinit var mFabExpense: FloatingActionButton

    private var mPresenter: ExpenseView.UserActionsListener? = null

    private var mTab: Tab? = null
    private var mExpense: Expense? = null

    private val isExpenseNew: Boolean
        get() = mExpense == null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.expenses)
        super.onCreate(savedInstanceState)

        mInputLayoutDesc = findViewById(R.id.edt_exp_desc_layout)
        mEditTextDescription = findViewById(R.id.edtDescriptionName)
        requestFocus(mEditTextDescription)

        mInputLayoutValue = findViewById(R.id.edt_exp_value_layout)
        mEditTextValue = findViewById(R.id.edtValue)

        mFabExpense = findViewById(R.id.fab_add_expense_details)
        mFabExpense.setOnClickListener(this)
        mFabExpense.show()

        mPresenter = ExpensePresenterImpl(this, Injection.provideTabsRepository(this))

        mTab = intent.getParcelableExtra(TAB_REQUESTED)
        mExpense = intent.getParcelableExtra(EDIT_EXPENSE)

        val ab = supportActionBar
        if (isExpenseNew) {
            ab?.setTitle(R.string.add_expense_action_bar)
        } else {
            ab?.setTitle(R.string.edit_expense_action_bar)
        }
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")

        if (!isExpenseNew) {
            setExpenseDescription(mExpense?.description.toString())
            setExpenseValue(mExpense?.value.toString())
        }
    }

    override fun chooseParticipants(expense: Expense) {
        val intent = SplitterActivity.newIntent(this, expense)
        startActivity(intent)
    }

    override fun setExpenseDescription(desc: String) {
        mEditTextDescription.setText(desc)
    }

    override fun setExpenseValue(value: String) {
        mEditTextValue.setText(value)
    }

    override fun setDescriptionError(value: Boolean) {
        if (value) {
            mInputLayoutDesc.error = getString(R.string.error_field_required)
            requestFocus(mEditTextDescription)
        }
        mInputLayoutDesc.isErrorEnabled = value
    }

    override fun setValueError(value: Boolean) {
        if (value) {
            mInputLayoutValue.error = getString(R.string.error_field_required)
            requestFocus(mEditTextValue)
        }
        mInputLayoutValue.isErrorEnabled = value
    }

    override fun onSuccess() {
        val description = mEditTextDescription.text.toString()
        val valueStr = mEditTextValue.text.toString()
        val value = java.lang.Double.parseDouble(valueStr)
        val expense: Expense

        if (isExpenseNew) {

            expense = Expense(description, value, mTab?.groupId.toString())

            mPresenter?.addExpense(expense)
        } else {
            expense = Expense.retrieveExpense(mExpense?.id.toString(), description, value, mTab?.groupId.toString())

            mPresenter?.updateExpense(expense)
        }
        chooseParticipants(expense)

        // ---closes the activity---
        mFabExpense.hide()
        finish()
    }

    override fun onClick(v: View) {
        mPresenter?.validateExpenseFields(mEditTextDescription.text.toString(), mEditTextValue.text.toString())
    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    companion object {

        private const val TAB_REQUESTED = "com.davismiyashiro.expenses.view.expense"
        private const val EDIT_EXPENSE = "com.davismiyashiro.expenses.view.expense.edit"

        fun newIntent(context: Context, tab: Tab, expense: Expense?): Intent {
            val intent = Intent(context, ExpenseActivity::class.java)
            intent.putExtra(TAB_REQUESTED, tab)
            intent.putExtra(EDIT_EXPENSE, expense)
            return intent
        }
    }
}
