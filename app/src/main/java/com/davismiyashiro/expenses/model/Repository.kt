package com.davismiyashiro.expenses.model

import android.support.v4.util.ArrayMap

import com.davismiyashiro.expenses.datatypes.Expense
import com.davismiyashiro.expenses.datatypes.Participant
import com.davismiyashiro.expenses.datatypes.ReceiptItem
import com.davismiyashiro.expenses.datatypes.Split
import com.davismiyashiro.expenses.datatypes.Tab

/**
 * Created by Davis on 24/01/2016.
 */
interface Repository {

    interface LoadTabsCallback {

        fun onTabsLoaded(tabs: MutableList<Tab>)
    }

    interface GetTabCallback {

        fun onTabLoaded(tab: Tab)
    }

    fun getTabs(callback: LoadTabsCallback)

    fun getTab(tabId: String, callback: GetTabCallback)

    fun saveTab(tab: Tab)

    fun updateTab(tab: Tab)

    fun deleteTab(tabId: String)

    fun refreshData()

    interface LoadParticipantsCallback {

        fun onParticipantsLoaded(participants: MutableList<Participant>)
    }

    interface GetParticipantCallback {

        fun onParticipantLoaded(participant: Participant)
    }

    fun saveParticipant(participant: Participant)

    fun updateParticipant(participant: Participant)

    fun deleteParticipant(participant: Participant)

    fun getParticipant(partId: String, callback: GetParticipantCallback)

    fun getParticipants(tabId: String, callback: LoadParticipantsCallback)

    fun saveExpense(expense: Expense)

    fun updateExpense(expense: Expense)

    fun deleteExpense(expense: Expense)

    interface LoadExpensesCallback {
        fun onExpensesLoaded(expenses: MutableList<Expense>)
    }

    fun getExpenses(tabId: String, callback: LoadExpensesCallback)

    fun saveSplits(splits: MutableList<Split>)

    fun deleteSplitsByExpense(expense: Expense)

    interface LoadSplitsCallback {
        fun onSplitsLoaded(splits: List<Split>)
    }

    fun getSplitsByExpense(expId: String, callback: LoadSplitsCallback)

    interface LoadReceiptItemsCallback {
        fun onReceiptItemsLoaded(items: ArrayMap<String, MutableList<ReceiptItem>>)
    }

    fun getReceiptItemsByTabId(tabId: String, callback: LoadReceiptItemsCallback)

    fun deleteAllTables()
}
