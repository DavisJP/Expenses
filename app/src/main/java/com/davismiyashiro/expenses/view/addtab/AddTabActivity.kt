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
package com.davismiyashiro.expenses.view.addtab

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.EditText

import com.davismiyashiro.expenses.Injection
import com.davismiyashiro.expenses.R
import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.view.BaseCompatActivity

import timber.log.Timber

/**
 * Class that saves the name of the tab and pass along to TabActivity.
 * Testing TextInputLayout library
 */
class AddTabActivity : BaseCompatActivity(), AddTabInterfaces.View {

    private lateinit var fabAddTabDetails: FloatingActionButton
    private lateinit var mEditTextTabName: EditText
    private lateinit var mInputLayoutName: TextInputLayout
    private var mPresenter: AddTabInterfaces.UserActionsListener? = null

    private var mTab: Tab? = null

    private val isTabNew: Boolean
        get() = mTab == null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.add_tab_layout)
        super.onCreate(savedInstanceState)

        mInputLayoutName = findViewById<View>(R.id.edt_tab_layout) as TextInputLayout
        mEditTextTabName = findViewById<View>(R.id.edt_txt_name) as TextInputEditText

        mEditTextTabName.addTextChangedListener(TabTextWatcher(mEditTextTabName))
        requestFocus(mEditTextTabName)

        fabAddTabDetails = findViewById<View>(R.id.fab_add_tab) as FloatingActionButton
        fabAddTabDetails.setOnClickListener {
            if (isTabNew) {
                if (validateName()) {
                    mPresenter?.addTab(mEditTextTabName.text.toString())
                }
            } else {
                mPresenter?.updateTab(mEditTextTabName.text.toString())
            }
        }

        mTab = intent.getParcelableExtra(EDIT_TAB)

        val ab = supportActionBar
        if (isTabNew) {
            ab?.setTitle(R.string.add_tab_action_bar)
        } else {
            ab?.setTitle(R.string.edit_tab_action_bar)
        }

        mPresenter = AddTabPresenterImpl(this, Injection.provideTabsRepository(applicationContext), mTab)
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")

        if (!isTabNew) {
            mPresenter?.loadTab()
        }
    }

    private fun validateName(): Boolean {
        if (mEditTextTabName.text.toString().trim { it <= ' ' }.isEmpty()) {
            showTabNameError()
            return false
        } else {
            mInputLayoutName.isErrorEnabled = false
        }

        return true
    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    private inner class TabTextWatcher constructor(private val view: View) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun afterTextChanged(editable: Editable) {
            when (view.id) {
                R.id.edt_txt_name -> validateName()
            }
        }
    }

    override fun showTabNameError() {
        mInputLayoutName.error = getString(R.string.err_msg_empty_name)
        requestFocus(mEditTextTabName)
    }

    override fun setTabName(name: String) {
        mEditTextTabName.setText(name)
    }

    override fun setTabValid() {
        finish()
    }

    override fun updatedTab(tab: Tab) {
        val returnIntent = Intent()
        returnIntent.putExtra(EDIT_TAB, tab)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    companion object {

        var EDIT_TAB = "com.davismiyashiro.expenses.view.addtab"

        fun newIntent(context: Context, tab: Tab?): Intent {
            val intent = Intent(context, AddTabActivity::class.java)
            intent.putExtra(EDIT_TAB, tab)
            return intent
        }
    }
}
