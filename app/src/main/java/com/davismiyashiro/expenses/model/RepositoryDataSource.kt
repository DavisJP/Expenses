package com.davismiyashiro.expenses.model

import android.support.v4.util.ArrayMap

import com.davismiyashiro.expenses.datatypes.Expense
import com.davismiyashiro.expenses.datatypes.Participant
import com.davismiyashiro.expenses.datatypes.ReceiptItem
import com.davismiyashiro.expenses.datatypes.Split
import com.davismiyashiro.expenses.datatypes.Tab

/**
 * Interface for storage access
 */
interface RepositoryDataSource {
    interface TabServiceCallback<T> {

        fun onLoaded(tabs: T)
    }

    fun getAllTabs(callback: TabServiceCallback<ArrayMap<String, Tab>>)

    fun getTab(tabId: String, callback: TabServiceCallback<Tab>)

    fun saveTab(tab: Tab)

    fun updateTabName(tab: Tab, name: String): Int

    fun deleteTab(tabId: String)

    interface ParticipantServiceCallback<T> {

        fun onLoaded(participants: T)

        fun onDataNotAvailable()
    }

    fun getParticipant(partId: String, callback: ParticipantServiceCallback<Participant>)

    fun getAllParticipants(tabId: String, callback: ParticipantServiceCallback<ArrayMap<String, Participant>>)

    fun saveParticipant(participant: Participant)

    fun updateParticipant(participant: Participant): Int

    fun deleteParticipant(participant: Participant)

    interface ExpenseServiceCallback<T> {

        fun onLoaded(expenses: T)

        fun onDataNotAvailable()
    }

    fun getExpense(expId: String, callback: ExpenseServiceCallback<Expense>)

    fun getAllExpenses(tabId: String, callback: ExpenseServiceCallback<ArrayMap<String, Expense>>)

    fun saveExpense(expense: Expense)

    fun updateExpense(expense: Expense): Int

    fun deleteExpense(expense: Expense)

    interface SplitServiceCallback<T> {

        fun onLoaded(splits: T)
    }

    fun getSplitsByExpense(expId: String, callback: SplitServiceCallback<ArrayMap<String, Split>>)

    fun saveSplits(splits: List<Split>)

    fun deleteSplitsByExpense(expense: Expense)

    interface ReceipItemServiceCallback<T> {

        fun onLoaded(items: T)
    }

    fun getReceiptItemByTabId(tabId: String, callback: ReceipItemServiceCallback<ArrayMap<String, MutableList<ReceiptItem>>>)

    fun deleteAllTables()
}
