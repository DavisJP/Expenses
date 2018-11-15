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
class FakeTabRepositoryDataSourceLocal private constructor(context: Context) : TabRepositoryDataSource {

    override fun getAllTabs(callback: TabRepositoryDataSource.TabServiceCallback<ArrayMap<String, Tab>>) {
        callback.onLoaded(TAB_SERVICE_DATA)
    }

    override fun getTab(tabId: String, callback: TabRepositoryDataSource.TabServiceCallback<Tab>) {
        val tab = TAB_SERVICE_DATA.get(tabId)
        callback.onLoaded(tab)
    }

    override fun saveTab(tab: Tab) {
        TAB_SERVICE_DATA.put(tab.groupId, tab)
    }

    override fun updateTabName(tab: Tab, name: String): Int {
        return 0
    }

    override fun deleteTab(tabId: String) {

    }

    override fun getParticipant(partId: String, callback: TabRepositoryDataSource.ParticipantServiceCallback<Participant>) {

    }

    override fun getAllParticipants(tabId: String, callback: TabRepositoryDataSource.ParticipantServiceCallback<ArrayMap<String, Participant>>) {
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

    override fun getExpense(expId: String, callback: TabRepositoryDataSource.ExpenseServiceCallback<Expense>) {

    }

    override fun getAllExpenses(tabId: String, callback: TabRepositoryDataSource.ExpenseServiceCallback<ArrayMap<String, Expense>>) {

    }

    override fun saveExpense(expense: Expense) {

    }

    override fun updateExpense(expense: Expense): Int {
        return 0
    }

    override fun deleteExpense(expense: Expense) {

    }

    override fun getSplitsByExpense(partId: String, callback: TabRepositoryDataSource.SplitServiceCallback<ArrayMap<String, Split>>) {

    }

    override fun saveSplits(splits: List<Split>) {

    }

    override fun deleteSplitsByExpense(expense: Expense) {

    }

    override fun getReceiptItemByTabId(tabId: String, callback: TabRepositoryDataSource.ReceipItemServiceCallback<ArrayMap<String, List<ReceiptItem>>>) {

    }

    override fun deleteAllTables() {

    }

    companion object {

        private var INSTANCE: FakeTabRepositoryDataSourceLocal? = null

        private val TAB_SERVICE_DATA = ArrayMap<String, Tab>()
        private val PARTICIPANT_MAP = ArrayMap<String, Participant>()

        fun getInstance(context: Context): TabRepositoryDataSource {
            if (INSTANCE == null) {
                INSTANCE = FakeTabRepositoryDataSourceLocal(context)
            }
            return INSTANCE as FakeTabRepositoryDataSourceLocal
        }
    }
}