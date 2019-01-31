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

import java.util.ArrayList

import timber.log.Timber

/**
 * Class to store the cached data retrieved
 */
class RepositoryImpl // Prevent direct instantiation. (Changed to support Injection)
(private val mRepositoryDataSource: RepositoryDataSource) : Repository {

    /**
     * Variables have local visibility so it can be accessed from tests.
     */
    var TAB_SERVICE_DATA: MutableMap<String, Tab> = ArrayMap()
    private var PARTICIPANT_MAP: MutableMap<String, Participant> = ArrayMap()
    private var EXPENSE_MAP: MutableMap<String, Expense> = ArrayMap()
    private var SPLIT_MAP: MutableMap<String, Split> = ArrayMap()
    private var RECEIPT_MAP: MutableMap<String, MutableList<ReceiptItem>> = ArrayMap()

    override fun getTabs(callback: Repository.LoadTabsCallback) = if (TAB_SERVICE_DATA.isEmpty()) {
        mRepositoryDataSource.getAllTabs(object : RepositoryDataSource.TabServiceCallback<ArrayMap<String, Tab>> {
            override fun onLoaded(mapTabs: ArrayMap<String, Tab>) {
                TAB_SERVICE_DATA.putAll(mapTabs)
                val tabs = ArrayList(TAB_SERVICE_DATA.values)
                callback.onTabsLoaded(tabs)
            }
        })
    } else {
        callback.onTabsLoaded(ArrayList(TAB_SERVICE_DATA.values))
    }

    override fun getTab(tabId: String, callback: Repository.GetTabCallback) {

        val tab = getTabById(tabId)

        if (tab != null) {
            callback.onTabLoaded(tab)
            return
        }

        mRepositoryDataSource.getTab(tabId, object : RepositoryDataSource.TabServiceCallback<Tab> {
            override fun onLoaded(tab: Tab) {
                callback.onTabLoaded(tab)
            }
        })
    }

    // Check memory before querying db
    private fun getTabById(id: String): Tab? {
        return if (TAB_SERVICE_DATA.isEmpty()) {
            null
        } else {
            TAB_SERVICE_DATA[id]
        }
    }

    override fun saveTab(tab: Tab) {
        TAB_SERVICE_DATA[tab.groupId] = tab
        // Store in db
        mRepositoryDataSource.saveTab(tab)

        // refreshData();
    }

    override fun updateTab(tab: Tab) {
        TAB_SERVICE_DATA[tab.groupId] = tab

        mRepositoryDataSource.updateTabName(tab, tab.groupName)
    }

    override fun deleteTab(tabId: String) {
        TAB_SERVICE_DATA.remove(tabId)
        mRepositoryDataSource.deleteTab(tabId)
        // refreshData();
    }

    override fun refreshData() {
        TAB_SERVICE_DATA.clear()
        PARTICIPANT_MAP.clear()
        EXPENSE_MAP.clear()
        RECEIPT_MAP.clear()
    }

    override fun saveParticipant(participant: Participant) {

        PARTICIPANT_MAP[participant.id] = participant

        mRepositoryDataSource.saveParticipant(participant)

        // refreshData();
    }

    override fun updateParticipant(participant: Participant) {

        PARTICIPANT_MAP[participant.id] = participant

        mRepositoryDataSource.updateParticipant(participant)
    }

    override fun deleteParticipant(participant: Participant) {
        // TODO: Crashing... why is PARTMAP null when I try to delete?
//         PARTICIPANT_MAP.remove(participant.id)

        mRepositoryDataSource.deleteParticipant(participant)
    }

    private fun getParticipantById(id: String): Participant? {
        return if (PARTICIPANT_MAP.isEmpty()) {
            null
        } else {
            PARTICIPANT_MAP[id]
        }
    }

    override fun getParticipant(partId: String, callback: Repository.GetParticipantCallback) {

        val participant = getParticipantById(partId)

        if (participant != null) {
            callback.onParticipantLoaded(participant)
            return
        }

        mRepositoryDataSource.getParticipant(partId, object : RepositoryDataSource.ParticipantServiceCallback<Participant> {

            override fun onLoaded(participant: Participant) {
                callback.onParticipantLoaded(participant)
            }

            override fun onDataNotAvailable() {
                Timber.d("Data not available, handle it here")
            }
        })
    }

    override fun getParticipants(tabId: String, callback: Repository.LoadParticipantsCallback) {
        if (PARTICIPANT_MAP.isEmpty()) {
            mRepositoryDataSource.getAllParticipants(tabId, object : RepositoryDataSource.ParticipantServiceCallback<ArrayMap<String, Participant>> {
                override fun onLoaded(participants: ArrayMap<String, Participant>) {
                    PARTICIPANT_MAP.putAll(participants)
                    callback.onParticipantsLoaded(ArrayList(PARTICIPANT_MAP.values))
                }

                override fun onDataNotAvailable() {
                    Timber.d("Data not available, handle it here")
                }
            })
        } else {
            callback.onParticipantsLoaded(ArrayList(PARTICIPANT_MAP.values))
        }
    }

    override fun saveExpense(expense: Expense) {
        EXPENSE_MAP[expense.id] = expense

        mRepositoryDataSource.saveExpense(expense)
        // refreshData();
    }

    override fun updateExpense(expense: Expense) {
        EXPENSE_MAP[expense.id] = expense

        mRepositoryDataSource.updateExpense(expense)
    }

    override fun deleteExpense(expense: Expense) {
        // TODO: Crashing... why is EXPENSE_MAP null when I try to delete?
//         EXPENSE_MAP.remove(expense.id)

        mRepositoryDataSource.deleteExpense(expense)
    }

    override fun getExpenses(tabId: String, callback: Repository.LoadExpensesCallback) {
        if (EXPENSE_MAP.isEmpty()) {
            mRepositoryDataSource.getAllExpenses(tabId, object : RepositoryDataSource.ExpenseServiceCallback<ArrayMap<String, Expense>> {
                override fun onLoaded(expenses: ArrayMap<String, Expense>) {
                    EXPENSE_MAP = expenses
                    callback.onExpensesLoaded(ArrayList(EXPENSE_MAP.values))
                }

                override fun onDataNotAvailable() {
                    Timber.d("Data not available, handle it here")
                }
            })
        } else {
            callback.onExpensesLoaded(ArrayList(EXPENSE_MAP.values))
        }
    }

    override fun saveSplits(splits: MutableList<Split>) {

        for (split in splits) {
            SPLIT_MAP[split.id] = split
        }
        mRepositoryDataSource.saveSplits(splits)
        // refreshData();
    }

    override fun deleteSplitsByExpense(expense: Expense) {
        // TODO: Crashing... why is SPLIT_MAP null when I try to delete?
//         SPLIT_MAP.remove(expense.id)

        mRepositoryDataSource.deleteSplitsByExpense(expense)
    }

    override fun getSplitsByExpense(expId: String, callback: Repository.LoadSplitsCallback) {
        if (SPLIT_MAP.isEmpty()) {
            mRepositoryDataSource.getSplitsByExpense(expId, object : RepositoryDataSource.SplitServiceCallback<ArrayMap<String, Split>> {
                override fun onLoaded(splits: ArrayMap<String, Split>) {
                    SPLIT_MAP.putAll(splits)
                    callback.onSplitsLoaded(ArrayList(SPLIT_MAP.values))
                }
            })
        } else {
            callback.onSplitsLoaded(ArrayList(SPLIT_MAP.values))
        }
    }

    override fun getReceiptItemsByTabId(tabId: String, callback: Repository.LoadReceiptItemsCallback) {
        if (RECEIPT_MAP.isEmpty()) {
            mRepositoryDataSource.getReceiptItemByTabId(tabId, object : RepositoryDataSource.ReceipItemServiceCallback<MutableMap<String, MutableList<ReceiptItem>>> {
                override fun onLoaded(items: MutableMap<String, MutableList<ReceiptItem>>) {
                    RECEIPT_MAP.putAll(items)
                    callback.onReceiptItemsLoaded(RECEIPT_MAP)
                }
            })
        } else {
            callback.onReceiptItemsLoaded(RECEIPT_MAP)
        }
    }

    override fun deleteAllTables() {
        mRepositoryDataSource.deleteAllTables()
        refreshData()
    }

    fun destroyInstance() {
        tabRepositoryImpl = null
    }

    companion object {

        private var tabRepositoryImpl: RepositoryImpl? = null

        fun getInstance(localDataSource: RepositoryDataSource): RepositoryImpl {
            if (tabRepositoryImpl == null) {
                tabRepositoryImpl = RepositoryImpl(localDataSource)
            }
            return tabRepositoryImpl!!
        }
    }
}
