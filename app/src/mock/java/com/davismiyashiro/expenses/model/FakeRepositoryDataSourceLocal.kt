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

import android.content.Context
import android.support.v4.util.ArrayMap

import com.davismiyashiro.expenses.datatypes.Expense
import com.davismiyashiro.expenses.datatypes.Participant
import com.davismiyashiro.expenses.datatypes.ReceiptItem
import com.davismiyashiro.expenses.datatypes.Split
import com.davismiyashiro.expenses.datatypes.Tab

/**
 * Fake Repository to provide data for testing repository of persisted data
 */
class FakeRepositoryDataSourceLocal private constructor(context: Context) : RepositoryDataSource {

    override fun getAllTabs(callback: RepositoryDataSource.TabServiceCallback<ArrayMap<String, Tab>>) {
        callback.onLoaded(TAB_SERVICE_DATA)
    }

    override fun getTab(tabId: String, callback: RepositoryDataSource.TabServiceCallback<Tab>) {
        val tab = TAB_SERVICE_DATA[tabId]
        tab?.let { callback.onLoaded(it) }
    }

    override fun saveTab(tab: Tab) {
        TAB_SERVICE_DATA.put(tab.groupId, tab)
    }

    override fun updateTabName(tab: Tab, name: String): Int {
        return 0
    }

    override fun deleteTab(tabId: String) = Unit

    override fun getParticipant(partId: String, callback: RepositoryDataSource.ParticipantServiceCallback<Participant>) {
    }

    override fun getAllParticipants(tabId: String, callback: RepositoryDataSource.ParticipantServiceCallback<ArrayMap<String, Participant>>) {
        callback.onLoaded(PARTICIPANT_MAP)
    }

    override fun saveParticipant(participant: Participant) {
        PARTICIPANT_MAP.put(participant.id, participant)
    }

    override fun updateParticipant(participant: Participant): Int {
        return 0
    }

    override fun deleteParticipant(participant: Participant) {
    }

    override fun getExpense(expId: String, callback: RepositoryDataSource.ExpenseServiceCallback<Expense>) {
    }

    override fun getAllExpenses(tabId: String, callback: RepositoryDataSource.ExpenseServiceCallback<ArrayMap<String, Expense>>) {
    }

    override fun saveExpense(expense: Expense) {
    }

    override fun updateExpense(expense: Expense): Int {
        return 0
    }

    override fun deleteExpense(expense: Expense) {
    }

    override fun getSplitsByExpense(partId: String, callback: RepositoryDataSource.SplitServiceCallback<ArrayMap<String, Split>>) {
    }

    override fun saveSplits(splits: List<Split>) {
    }

    override fun deleteSplitsByExpense(expense: Expense) {
    }

    override fun getReceiptItemByTabId(tabId: String, callback: RepositoryDataSource.ReceipItemServiceCallback<ArrayMap<String, MutableList<ReceiptItem>>>) {
    }

    override fun deleteAllTables() = Unit

    companion object {

        private var INSTANCE: FakeRepositoryDataSourceLocal? = null

        private val TAB_SERVICE_DATA = ArrayMap<String, Tab>()
        private val PARTICIPANT_MAP = ArrayMap<String, Participant>()

        fun getInstance(context: Context): RepositoryDataSource {
            if (INSTANCE == null) {
                INSTANCE = FakeRepositoryDataSourceLocal(context)
            }
            return INSTANCE as FakeRepositoryDataSourceLocal
        }
    }
}