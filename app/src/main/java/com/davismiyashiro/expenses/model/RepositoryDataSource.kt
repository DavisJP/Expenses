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

    fun getReceiptItemByTabId(tabId: String, callback: ReceipItemServiceCallback<MutableMap<String, MutableList<ReceiptItem>>>)

    fun deleteAllTables()
}
