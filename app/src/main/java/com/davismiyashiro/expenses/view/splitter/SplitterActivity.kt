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
import com.davismiyashiro.expenses.datatypes.Tab

import java.util.ArrayList

import timber.log.Timber

class SplitterActivity : BaseCompatActivity(), SplitterView.View, View.OnClickListener {

    private val mTab: Tab? = null
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
        val participants = ArrayList<Participant>()//

        //TODO: Create a datatype for Payer UI
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

        private val SPLITTER_EXPENSE_REQUESTED = "com.davismiyashiro.expenses.view.splitter"

        fun newIntent(context: Context, expense: Expense?): Intent {
            val newIntent = Intent(context, SplitterActivity::class.java)
            newIntent.putExtra(SPLITTER_EXPENSE_REQUESTED, expense)
            return newIntent
        }
    }
}