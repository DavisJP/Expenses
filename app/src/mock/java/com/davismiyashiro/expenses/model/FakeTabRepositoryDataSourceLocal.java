package com.davismiyashiro.expenses.model;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.davismiyashiro.expenses.datatypes.Expense;
import com.davismiyashiro.expenses.datatypes.Participant;
import com.davismiyashiro.expenses.datatypes.ReceiptItem;
import com.davismiyashiro.expenses.datatypes.Split;
import com.davismiyashiro.expenses.datatypes.Tab;

import java.util.List;

/**
 * Created by Davis on 25/01/2016.
 */
public class FakeTabRepositoryDataSourceLocal implements TabRepositoryDataSource {

    private static FakeTabRepositoryDataSourceLocal INSTANCE = null;

    private static final ArrayMap<String, Tab> TAB_SERVICE_DATA = new ArrayMap();
    private static final ArrayMap<String, Participant> PARTICIPANT_MAP = new ArrayMap();

    private FakeTabRepositoryDataSourceLocal(Context context) {

    }

    public static TabRepositoryDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FakeTabRepositoryDataSourceLocal(context);
        }
        return INSTANCE;
    }

    @Override
    public void getAllTabs(TabServiceCallback<ArrayMap<String, Tab>> callback) {
        callback.onLoaded(TAB_SERVICE_DATA);
    }

    @Override
    public void getTab(String tabId, TabServiceCallback<Tab> callback) {
        Tab tab = TAB_SERVICE_DATA.get(tabId);
        callback.onLoaded(tab);
    }

    @Override
    public void saveTab(Tab tab) {
        TAB_SERVICE_DATA.put(tab.getGroupId(), tab);
    }

    @Override
    public int updateTabName(Tab tab, String name) {
        return 0;
    }

    @Override
    public void deleteTab(String tabId) {

    }

    @Override
    public void getParticipant(String partId, ParticipantServiceCallback<Participant> callback) {

    }

    @Override
    public void getAllParticipants(String tabId, ParticipantServiceCallback<ArrayMap<String, Participant>> callback) {
        callback.onLoaded(PARTICIPANT_MAP);
    }

    @Override
    public void saveParticipant(Participant participant) {
        PARTICIPANT_MAP.put(participant.getId(), participant);
    }

    @Override
    public int updateParticipant(Participant participant) {
        return 0;
    }

    @Override
    public void deleteParticipant(Participant participant) {

    }

    @Override
    public void getExpense(String expId, ExpenseServiceCallback<Expense> callback) {

    }

    @Override
    public void getAllExpenses(String tabId, ExpenseServiceCallback<ArrayMap<String, Expense>> callback) {

    }

    @Override
    public void saveExpense(Expense expense) {

    }

    @Override
    public int updateExpense(Expense expense) {
        return 0;
    }

    @Override
    public void deleteExpense(Expense expense) {

    }

    @Override
    public void getSplitsByExpense(String partId, SplitServiceCallback<ArrayMap<String, Split>> callback) {

    }

    @Override
    public void saveSplits(List<Split> splits) {

    }

    @Override
    public void deleteSplitsByExpense(Expense expense) {

    }

    @Override
    public void getReceiptItemByTabId(String tabId, ReceipItemServiceCallback<ArrayMap<String, List<ReceiptItem>>> callback) {

    }

    @Override
    public void deleteAllTables() {

    }
}