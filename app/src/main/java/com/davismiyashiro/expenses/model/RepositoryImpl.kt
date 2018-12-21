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
class RepositoryImpl// Prevent direct instantiation. (Changed to support Injection)
(private val mRepositoryDataSource: RepositoryDataSource) : Repository {

    /**
     * Variables have local visibility so it can be accessed from tests.
     */
    var TAB_SERVICE_DATA: ArrayMap<String, Tab>? = null
    private var PARTICIPANT_MAP: ArrayMap<String, Participant>? = null
    private var EXPENSE_MAP: ArrayMap<String, Expense>? = null
    private var SPLIT_MAP: ArrayMap<String, Split>? = null
    private var RECEIPT_MAP: ArrayMap<String, MutableList<ReceiptItem>>? = null

    override fun getTabs(callback: Repository.LoadTabsCallback) = if (TAB_SERVICE_DATA == null) {
        mRepositoryDataSource.getAllTabs(object : RepositoryDataSource.TabServiceCallback<ArrayMap<String, Tab>> {
            override fun onLoaded(mapTabs: ArrayMap<String, Tab>) {
                TAB_SERVICE_DATA = mapTabs
                val tabs = ArrayList(TAB_SERVICE_DATA?.values)
                callback.onTabsLoaded(tabs)
            }
        })
    } else {
        callback.onTabsLoaded(ArrayList(TAB_SERVICE_DATA?.values))
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

    //Check memory before querying db
    private fun getTabById(id: String): Tab? {
        return if (TAB_SERVICE_DATA == null || TAB_SERVICE_DATA!!.isEmpty) {
            null
        } else {
            TAB_SERVICE_DATA!![id]
        }
    }

    override fun saveTab(tab: Tab) {
        //Add for the UI
        if (TAB_SERVICE_DATA == null) {
            TAB_SERVICE_DATA = ArrayMap()
        }
        TAB_SERVICE_DATA!![tab.groupId] = tab
        //Store in db
        mRepositoryDataSource.saveTab(tab)

        //refreshData();
    }

    override fun updateTab(tab: Tab) {
        if (TAB_SERVICE_DATA == null) {
            TAB_SERVICE_DATA = ArrayMap()
        }
        TAB_SERVICE_DATA!![tab.groupId] = tab

        mRepositoryDataSource.updateTabName(tab, tab.groupName)
    }

    override fun deleteTab(tabId: String) {
        TAB_SERVICE_DATA!!.remove(tabId)
        mRepositoryDataSource.deleteTab(tabId)
        //refreshData();
    }

    override fun refreshData() {
        TAB_SERVICE_DATA = null
        PARTICIPANT_MAP = null
        EXPENSE_MAP = null
        RECEIPT_MAP = null
    }

    override fun saveParticipant(participant: Participant) {

        if (PARTICIPANT_MAP == null) {
            PARTICIPANT_MAP = ArrayMap()
        }
        PARTICIPANT_MAP!![participant.id] = participant

        mRepositoryDataSource.saveParticipant(participant)

        //refreshData();
    }

    override fun updateParticipant(participant: Participant) {
        if (PARTICIPANT_MAP == null) {
            PARTICIPANT_MAP = ArrayMap()
        }
        PARTICIPANT_MAP!![participant.id] = participant

        mRepositoryDataSource.updateParticipant(participant)
    }

    override fun deleteParticipant(participant: Participant) {
        //TODO: Crashing... why is PARTMAP null when I try to delete?
        //PARTICIPANT_MAP.remove(participant.getId());

        mRepositoryDataSource.deleteParticipant(participant)
    }

    private fun getParticipantById(id: String): Participant? {
        return if (PARTICIPANT_MAP == null || PARTICIPANT_MAP!!.isEmpty) {
            null
        } else {
            PARTICIPANT_MAP!![id]
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
        if (PARTICIPANT_MAP == null) {
            mRepositoryDataSource.getAllParticipants(tabId, object : RepositoryDataSource.ParticipantServiceCallback<ArrayMap<String, Participant>> {
                override fun onLoaded(participants: ArrayMap<String, Participant>) {
                    PARTICIPANT_MAP = participants
                    callback.onParticipantsLoaded(ArrayList(PARTICIPANT_MAP!!.values))
                }

                override fun onDataNotAvailable() {
                    Timber.d("Data not available, handle it here")
                }
            })
        } else {
            callback.onParticipantsLoaded(ArrayList(PARTICIPANT_MAP!!.values))
        }
    }

    override fun saveExpense(expense: Expense) {

        if (EXPENSE_MAP == null) {
            EXPENSE_MAP = ArrayMap()
        }
        EXPENSE_MAP!![expense.id] = expense

        mRepositoryDataSource.saveExpense(expense)
        //refreshData();
    }

    override fun updateExpense(expense: Expense) {
        if (EXPENSE_MAP == null) {
            EXPENSE_MAP = ArrayMap()
        }
        EXPENSE_MAP!![expense.id] = expense

        mRepositoryDataSource.updateExpense(expense)
    }

    override fun deleteExpense(expense: Expense) {
        //TODO: Crashing... why is EXPENSE_MAP null when I try to delete?
        //EXPENSE_MAP.remove(expense.getId());

        mRepositoryDataSource.deleteExpense(expense)
    }

    override fun getExpenses(tabId: String, callback: Repository.LoadExpensesCallback) {
        if (EXPENSE_MAP == null) {
            mRepositoryDataSource.getAllExpenses(tabId, object : RepositoryDataSource.ExpenseServiceCallback<ArrayMap<String, Expense>> {
                override fun onLoaded(expenses: ArrayMap<String, Expense>) {
                    EXPENSE_MAP = expenses
                    callback.onExpensesLoaded(ArrayList(EXPENSE_MAP!!.values))
                }

                override fun onDataNotAvailable() {
                    Timber.d("Data not available, handle it here")
                }
            })
        } else {
            callback.onExpensesLoaded(ArrayList(EXPENSE_MAP!!.values))
        }
    }

    override fun saveSplits(splits: List<Split>) {
        if (SPLIT_MAP == null) {
            SPLIT_MAP = ArrayMap()
        }

        for (split in splits) {
            SPLIT_MAP!![split.id] = split
        }
        mRepositoryDataSource.saveSplits(splits)
        //refreshData();
    }

    override fun deleteSplitsByExpense(expense: Expense) {
        //TODO: Crashing... why is SPLIT_MAP null when I try to delete?
        //SPLIT_MAP.remove(expense.getId());

        mRepositoryDataSource.deleteSplitsByExpense(expense)
    }

    override fun getSplitsByExpense(expId: String, callback: Repository.LoadSplitsCallback) {
        if (SPLIT_MAP == null) {
            mRepositoryDataSource.getSplitsByExpense(expId, object: RepositoryDataSource.SplitServiceCallback<ArrayMap<String, Split>> {
                override fun onLoaded(splits: ArrayMap<String, Split>) {
                    SPLIT_MAP = splits
                    callback.onSplitsLoaded(ArrayList(SPLIT_MAP?.values))
                }
            })
        } else {
            callback.onSplitsLoaded(ArrayList(SPLIT_MAP!!.values))
        }
    }

    override fun getReceiptItemsByTabId(tabId: String, callback: Repository.LoadReceiptItemsCallback) {
        if (RECEIPT_MAP == null) {
            mRepositoryDataSource.getReceiptItemByTabId(tabId, object: RepositoryDataSource.ReceipItemServiceCallback<ArrayMap<String, MutableList<ReceiptItem>>> {
                override fun onLoaded(items: ArrayMap<String, MutableList<ReceiptItem>>) {
                    RECEIPT_MAP = items
                    callback.onReceiptItemsLoaded(RECEIPT_MAP!!)
                }

            })
        } else {
            callback.onReceiptItemsLoaded(RECEIPT_MAP!!)
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
