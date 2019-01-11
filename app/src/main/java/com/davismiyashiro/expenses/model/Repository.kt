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
        fun onReceiptItemsLoaded(items: MutableMap<String, MutableList<ReceiptItem>>)
    }

    fun getReceiptItemsByTabId(tabId: String, callback: LoadReceiptItemsCallback)

    fun deleteAllTables()
}
