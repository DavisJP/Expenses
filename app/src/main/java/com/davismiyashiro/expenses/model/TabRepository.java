package com.davismiyashiro.expenses.model;

import android.support.v4.util.ArrayMap;

import com.davismiyashiro.expenses.datatypes.Expense;
import com.davismiyashiro.expenses.datatypes.Participant;
import com.davismiyashiro.expenses.datatypes.ReceiptItem;
import com.davismiyashiro.expenses.datatypes.Split;
import com.davismiyashiro.expenses.datatypes.Tab;

import java.util.List;

/**
 * Created by Davis on 24/01/2016.
 */
public interface TabRepository {

    interface LoadTabsCallback {

        void onTabsLoaded(List<Tab> tabs);
    }

    interface GetTabCallback {

        void onTabLoaded(Tab tab);
    }

    void getTabs(LoadTabsCallback callback);

    void getTab(String tabId, GetTabCallback callback);

    void saveTab(Tab tab);

    void updateTab (Tab tab);

    void deleteTab(String tabId);

    void refreshData();

    interface LoadParticipantsCallback {

        void onParticipantsLoaded(List<Participant> participants);
    }

    interface GetParticipantCallback {

        void onParticipantLoaded(Participant participant);
    }

    void saveParticipant (Participant participant);

    void updateParticipant (Participant participant);

    void deleteParticipant(Participant participant);

    void getParticipant(String partId, GetParticipantCallback callback);

    void getParticipants(String tabId, LoadParticipantsCallback callback);

    void saveExpense (Expense expense);

    void updateExpense (Expense expense);

    void deleteExpense(Expense expense);

    interface LoadExpensesCallback {
        void onExpensesLoaded (List<Expense> expenses);
    }

    void getExpenses (String tabId, LoadExpensesCallback callback);

    void saveSplits (List <Split> splits);

    void deleteSplitsByExpense (Expense expense);

    interface LoadSplitsCallback {
        void onSplitsLoaded (List<Split> splits);
    }

    void getSplitsByExpense (String expId, LoadSplitsCallback callback);

    interface LoadReceiptItemsCallback {
        void onReceiptItemsLoaded (ArrayMap<String, List<ReceiptItem>> items);
    }

    void getReceiptItemsByTabId (String tabId, LoadReceiptItemsCallback callback);

    void deleteAllTables ();
}
