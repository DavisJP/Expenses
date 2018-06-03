package com.davismiyashiro.expenses.model;

import android.support.v4.util.ArrayMap;

import com.davismiyashiro.expenses.datatypes.Expense;
import com.davismiyashiro.expenses.datatypes.Participant;
import com.davismiyashiro.expenses.datatypes.ReceiptItem;
import com.davismiyashiro.expenses.datatypes.Split;
import com.davismiyashiro.expenses.datatypes.Tab;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Class to store the cached data retrieved
 */
public class InMemoryTabRepository implements TabRepository{

    private static InMemoryTabRepository inMemoryTabRepository = null;
    private final TabRepositoryDataSource mTabRepositoryDataSource;

    /**
     * Variables have local visibility so it can be accessed from tests.
     */
    ArrayMap<String, Tab> TAB_SERVICE_DATA;
    private ArrayMap<String, Participant> PARTICIPANT_MAP;
    private ArrayMap<String, Expense> EXPENSE_MAP;
    private ArrayMap<String, Split> SPLIT_MAP;
    private ArrayMap<String, List<ReceiptItem>> RECEIPT_MAP;

    // Prevent direct instantiation. (Changed to support Injection)
    public InMemoryTabRepository (TabRepositoryDataSource tabRepositoryDataSource) {
        mTabRepositoryDataSource = tabRepositoryDataSource;
    }

    public static InMemoryTabRepository getInstance (TabRepositoryDataSource localDataSource) {
        if (inMemoryTabRepository == null) {
            inMemoryTabRepository = new InMemoryTabRepository(localDataSource);
        }
        return inMemoryTabRepository;
    }

    @Override
    public void getTabs(final LoadTabsCallback callback) {
        if (TAB_SERVICE_DATA == null) {
            mTabRepositoryDataSource.getAllTabs(mapTabs -> {
                TAB_SERVICE_DATA = mapTabs;
                List<Tab> tabs = new ArrayList<>(TAB_SERVICE_DATA.values());
                callback.onTabsLoaded(tabs);
            });
        } else {
            callback.onTabsLoaded(new ArrayList<Tab>(TAB_SERVICE_DATA.values()));
        }
    }

    @Override
    public void getTab(String tabId, final GetTabCallback callback) {

        Tab tab = getTabById (tabId);

        if (tab != null) {
            callback.onTabLoaded(tab);
            return;
        }

        mTabRepositoryDataSource.getTab(tabId, tab1 -> callback.onTabLoaded(tab1));
    }

    //Check memory before querying db
    private Tab getTabById (String id) {
        if (TAB_SERVICE_DATA == null || TAB_SERVICE_DATA.isEmpty()) {
            return null;
        } else {
            return TAB_SERVICE_DATA.get(id);
        }
    }

    @Override
    public void saveTab(Tab tab) {
        //Add for the UI
        if (TAB_SERVICE_DATA == null) {
            TAB_SERVICE_DATA = new ArrayMap<>();
        }
        TAB_SERVICE_DATA.put(tab.getGroupId(), tab);
        //Store in db
        mTabRepositoryDataSource.saveTab(tab);

        //refreshData();
    }

    @Override
    public void updateTab(Tab tab) {
        if (TAB_SERVICE_DATA == null) {
            TAB_SERVICE_DATA = new ArrayMap<>();
        }
        TAB_SERVICE_DATA.put(tab.getGroupId(), tab);

        mTabRepositoryDataSource.updateTabName(tab, tab.getGroupName());
    }

    @Override
    public void deleteTab(String tabId) {
        TAB_SERVICE_DATA.remove(tabId);
        mTabRepositoryDataSource.deleteTab(tabId);
        //refreshData();
    }

    @Override
    public void refreshData() {
        TAB_SERVICE_DATA = null;
        PARTICIPANT_MAP = null;
        EXPENSE_MAP = null;
        RECEIPT_MAP = null;
    }

    @Override
    public void saveParticipant (Participant participant) {

        if (PARTICIPANT_MAP == null) {
            PARTICIPANT_MAP = new ArrayMap<>();
        }
        PARTICIPANT_MAP.put(participant.getId(), participant);

        mTabRepositoryDataSource.saveParticipant (participant);

        //refreshData();
    }

    @Override
    public void updateParticipant(Participant participant) {
        if (PARTICIPANT_MAP == null) {
            PARTICIPANT_MAP = new ArrayMap<>();
        }
        PARTICIPANT_MAP.put(participant.getId(), participant);

        mTabRepositoryDataSource.updateParticipant(participant);
    }

    @Override
    public void deleteParticipant(Participant participant) {
        //Crashing... why is PARTMAP null when I try to delete?
        //PARTICIPANT_MAP.remove(participant.getId());

        mTabRepositoryDataSource.deleteParticipant(participant);
    }

    private Participant getParticipantById (String id) {
        if (PARTICIPANT_MAP == null || PARTICIPANT_MAP.isEmpty()) {
            return null;
        } else {
            return PARTICIPANT_MAP.get(id);
        }
    }

    @Override
    public void getParticipant(String partId, final GetParticipantCallback callback) {

        Participant participant = getParticipantById(partId);

        if (participant != null) {
            callback.onParticipantLoaded(participant);
            return;
        }

        mTabRepositoryDataSource.getParticipant(partId, new TabRepositoryDataSource.ParticipantServiceCallback<Participant>() {

            @Override
            public void onLoaded(Participant participant) {
                callback.onParticipantLoaded(participant);
            }

            @Override
            public void onDataNotAvailable() {
                Timber.d("Data not available, handle it here");
            }
        });
    }

    @Override
    public void getParticipants(String tabId, final LoadParticipantsCallback callback) {
        if (PARTICIPANT_MAP == null) {
            mTabRepositoryDataSource.getAllParticipants(tabId, new TabRepositoryDataSource.ParticipantServiceCallback<ArrayMap <String, Participant>>() {
                @Override
                public void onLoaded(ArrayMap <String, Participant> participants) {
                    PARTICIPANT_MAP = participants;
                    callback.onParticipantsLoaded(new ArrayList<>(PARTICIPANT_MAP.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    Timber.d("Data not available, handle it here");
                }
            });
        } else {
            callback.onParticipantsLoaded(new ArrayList<>(PARTICIPANT_MAP.values()));
        }
    }

    @Override
    public void saveExpense(Expense expense) {

        if (EXPENSE_MAP == null) {
            EXPENSE_MAP = new ArrayMap<>();
        }
        EXPENSE_MAP.put(expense.getId(), expense);

        mTabRepositoryDataSource.saveExpense(expense);
        //refreshData();
    }

    @Override
    public void updateExpense(Expense expense) {
        if (EXPENSE_MAP == null) {
            EXPENSE_MAP = new ArrayMap<>();
        }
        EXPENSE_MAP.put(expense.getId(), expense);

        mTabRepositoryDataSource.updateExpense(expense);
    }

    @Override
    public void deleteExpense(Expense expense) {
        //Crashing... why is EXPENSE_MAP null when I try to delete?
        //EXPENSE_MAP.remove(expense.getId());

        mTabRepositoryDataSource.deleteExpense(expense);
    }

    @Override
    public void getExpenses(String tabId, final LoadExpensesCallback callback) {
        if (EXPENSE_MAP == null) {
            mTabRepositoryDataSource.getAllExpenses(tabId, new TabRepositoryDataSource.ExpenseServiceCallback<ArrayMap <String, Expense>>() {
                @Override
                public void onLoaded(ArrayMap <String, Expense> expenses) {
                    EXPENSE_MAP = expenses;
                    callback.onExpensesLoaded(new ArrayList<>(EXPENSE_MAP.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    Timber.d("Data not available, handle it here");
                }
            });
        } else {
            callback.onExpensesLoaded(new ArrayList<>(EXPENSE_MAP.values()));
        }
    }

    @Override
    public void saveSplits(List<Split> splits) {
        if (SPLIT_MAP == null){
            SPLIT_MAP = new ArrayMap<>();
        }

        for (Split split : splits) {
            SPLIT_MAP.put(split.getId(), split);
        }
        mTabRepositoryDataSource.saveSplits(splits);
        //refreshData();
    }

    @Override
    public void deleteSplitsByExpense(Expense expense) {
        //Crashing... why is SPLIT_MAP null when I try to delete?
        //SPLIT_MAP.remove(expense.getId());

        mTabRepositoryDataSource.deleteSplitsByExpense(expense);
    }

    @Override
    public void getSplitsByExpense(String expId, final LoadSplitsCallback callback) {
        if (SPLIT_MAP == null) {
            mTabRepositoryDataSource.getSplitsByExpense(expId, splits -> {
                SPLIT_MAP = splits;
                callback.onSplitsLoaded(new ArrayList<Split>(SPLIT_MAP.values()));
            });
        } else {
            callback.onSplitsLoaded(new ArrayList<>(SPLIT_MAP.values()));
        }
    }

    @Override
    public void getReceiptItemsByTabId(String tabId, final LoadReceiptItemsCallback callback) {
        if (RECEIPT_MAP == null) {
            mTabRepositoryDataSource.getReceiptItemByTabId(tabId, items -> {
                RECEIPT_MAP = items;
                callback.onReceiptItemsLoaded(RECEIPT_MAP);
            });
        } else {
            callback.onReceiptItemsLoaded(RECEIPT_MAP);
        }
    }

    @Override
    public void deleteAllTables() {
        mTabRepositoryDataSource.deleteAllTables();
        refreshData();
    }

    public void destroyInstance() {
        inMemoryTabRepository = null;
    }
}
