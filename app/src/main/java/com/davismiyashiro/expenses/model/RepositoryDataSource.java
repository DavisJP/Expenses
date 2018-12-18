package com.davismiyashiro.expenses.model;

import android.util.ArrayMap;

import com.davismiyashiro.expenses.datatypes.Expense;
import com.davismiyashiro.expenses.datatypes.Participant;
import com.davismiyashiro.expenses.datatypes.ReceiptItem;
import com.davismiyashiro.expenses.datatypes.Split;
import com.davismiyashiro.expenses.datatypes.Tab;

import java.util.List;

/**
 * Interface for storage access
 */
public interface RepositoryDataSource {
    interface TabServiceCallback<T> {

        void onLoaded(T tabs);
    }

    void getAllTabs(TabServiceCallback<ArrayMap<String, Tab>> callback);

    void getTab(String tabId, TabServiceCallback<Tab> callback);

    void saveTab(Tab tab);

    int updateTabName (Tab tab, String name);

    void deleteTab(String tabId);

    interface ParticipantServiceCallback<T> {

        void onLoaded(T participants);

        void onDataNotAvailable ();
    }

    void getParticipant (String partId, ParticipantServiceCallback<Participant> callback);

    void getAllParticipants(String tabId, ParticipantServiceCallback<ArrayMap <String, Participant>> callback);

    void saveParticipant (Participant participant);

    int updateParticipant (Participant participant);

    void deleteParticipant(Participant participant);

    interface ExpenseServiceCallback<T> {

        void onLoaded(T expenses);

        void onDataNotAvailable ();
    }

    void getExpense (String expId, ExpenseServiceCallback <Expense> callback);

    void getAllExpenses (String tabId, ExpenseServiceCallback<ArrayMap <String, Expense>> callback);

    void saveExpense (Expense expense);

    int updateExpense (Expense expense);

    void deleteExpense (Expense expense);

    interface SplitServiceCallback<T> {

        void onLoaded(T splits);
    }

    void getSplitsByExpense (String expId, SplitServiceCallback<ArrayMap<String, Split>> callback);

    void saveSplits (List<Split> splits);

    void deleteSplitsByExpense (Expense expense);

    interface ReceipItemServiceCallback<T> {

        void onLoaded(T items);
    }

    void getReceiptItemByTabId (String tabId, ReceipItemServiceCallback<ArrayMap<String, List<ReceiptItem>>> callback);

    void deleteAllTables ();
}
