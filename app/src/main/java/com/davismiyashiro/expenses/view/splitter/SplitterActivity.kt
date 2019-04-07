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
package com.davismiyashiro.expenses.view.splitter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

import com.davismiyashiro.expenses.Injection
import com.davismiyashiro.expenses.R
import com.davismiyashiro.expenses.view.BaseCompatActivity
import com.davismiyashiro.expenses.datatypes.Expense
import com.davismiyashiro.expenses.datatypes.Participant

import java.util.ArrayList

import timber.log.Timber

class SplitterActivity : BaseCompatActivity(), SplitterView.View, View.OnClickListener {

    private var mPresenter: SplitterView.UserActionsListener? = null

    private lateinit var mListView: ListView
    private var mExpense: Expense? = null
    private var mParticipants = listOf<Participant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.splitter)
        super.onCreate(savedInstanceState)

        findViewById<View>(R.id.fab_split_participants).setOnClickListener(this)

        mPresenter = SplitterPresenterImpl(this, Injection.provideTabsRepository(this))

        mListView = findViewById<View>(R.id.list) as ListView
        mListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        mListView.isTextFilterEnabled = true

        mExpense = intent.getParcelableExtra(SPLITTER_EXPENSE_REQUESTED)
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")

        mParticipants = mPresenter!!.getParticipants(mExpense!!)

        val adapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_checked, getParticipantsNames(mParticipants))
        mListView.adapter = adapter
    }

    private fun getParticipantsNames(participants: List<Participant>): List<String> {
        val names = ArrayList<String>()
        for ((_, name) in participants) {
            names.add(name)
        }
        return names
    }

    override fun splitWithGroup() {
        val participants = ArrayList<Participant>() //

        // TODO: Create a datatype for Payer UI
        for (i in 0 until mListView.count) {
            if (mListView.isItemChecked(i)) {
                participants.add(mParticipants[i])
            }
        }

        mPresenter?.saveSplits(participants, mExpense!!)

        finish()
    }

    override fun setParticipantSelectedError() {
        Toast.makeText(this, "Select at least 1 participant",
                Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View) {
        mPresenter?.validateParticipantsSelected(mListView.checkedItemCount)
    }

    companion object {

        private const val SPLITTER_EXPENSE_REQUESTED = "com.davismiyashiro.expenses.view.splitter"

        fun newIntent(context: Context, expense: Expense?): Intent {
            val newIntent = Intent(context, SplitterActivity::class.java)
            newIntent.putExtra(SPLITTER_EXPENSE_REQUESTED, expense)
            return newIntent
        }
    }
}