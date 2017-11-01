package com.davismiyashiro.expenses.model.localrepo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.util.ArrayMap;

import com.davismiyashiro.expenses.datatypes.Expense;
import com.davismiyashiro.expenses.datatypes.Participant;
import com.davismiyashiro.expenses.datatypes.ReceiptItem;
import com.davismiyashiro.expenses.datatypes.Split;
import com.davismiyashiro.expenses.datatypes.Tab;
import com.davismiyashiro.expenses.model.TabRepositoryDataSource;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.davismiyashiro.expenses.model.localrepo.TabDbSchema.*;

/**
 * Implementation of the Tabs Service API (adds a latency simulating network)
 */
public class TabRepositoryDataSourceLocal implements TabRepositoryDataSource {

    private static SQLiteDatabase mDatabase;
    private static TabRepositoryDataSourceLocal mTabRepositoryDataSourceLocal;

    public static synchronized TabRepositoryDataSourceLocal getInstance (Context context) {
        if (mTabRepositoryDataSourceLocal == null)
            mTabRepositoryDataSourceLocal = new TabRepositoryDataSourceLocal(context);

        return mTabRepositoryDataSourceLocal;
    }

    //private to prevent direct instantiation (Changed to support Injection)
    public TabRepositoryDataSourceLocal(Context context) {
        TabsDbHelper dbHelper = new TabsDbHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
    }

    private TabsCursorWrapper queryDb (String tableName, String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                tableName,
                null,  //Columns - null selects all columns
                whereClause,
                whereArgs,
                null,  //groupBy
                null,  //having
                null   //orderBy
        );

//        if (cursor != null) {
//            cursor.close();
//        }
        return new TabsCursorWrapper(cursor);
    }

    @Override
    public void getAllTabs(final TabServiceCallback<ArrayMap <String, Tab>> callback) {

                ArrayMap <String, Tab> mapTabs = new ArrayMap<>();

                TabsCursorWrapper cursor = queryDb(TabTable.NAME, null, null);

                try {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        mapTabs.put(cursor.getTab().getGroupId(), cursor.getTab());
                        cursor.moveToNext();
                    }
                }
                finally {
                    //Always close cursors
                    cursor.close();
                }
                callback.onLoaded(mapTabs);
    }

    @Override
    public void getTab(String tabId, TabServiceCallback<Tab> callback) {
        TabsCursorWrapper cursor = queryDb(TabTable.NAME, TabTable.Cols.UUID + " = ?", new String [] { tabId });

        try {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Tab tab = cursor.getTab();
                callback.onLoaded(tab);
            }
        }
        finally {
            cursor.close();
        }
    }

    private ContentValues getTabContentValues (Tab tab) {
        ContentValues values = new ContentValues();
        values.put(TabTable.Cols.UUID, tab.getGroupId());
        values.put (TabTable.Cols.GROUPNAME, tab.getGroupName());

        return values;
    }

    @Override
    public void saveTab(Tab tab) {
        ContentValues values = getTabContentValues(tab);
        mDatabase.insert(TabTable.NAME, null, values);
    }

    @Override
    public int updateTabName(Tab tab, String name) {
        ContentValues values = new ContentValues();
        values.put(TabTable.Cols.GROUPNAME, name);

        // Updating Tab name by Tab.groupId
        return mDatabase.update(TabTable.NAME, values, TabTable.Cols.UUID + " = ?",
                new String[] { String.valueOf(tab.getGroupId()) });
    }

    private void deleteEntry(String tableName, String selection, String tabId ) {
        mDatabase.beginTransaction();
        try {
            mDatabase.delete(tableName, selection,
                    new String[]{String.valueOf(tabId)});
            mDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Timber.d(e.getMessage().concat("Error while trying to delete = ").concat(tabId)) ;
        } finally {
            mDatabase.endTransaction();
        }
    }

    @Override
    public void deleteTab(String tabId) {
        deleteEntry(TabTable.NAME, TabTable.Cols.UUID + " = ?", tabId);
    }

    @Override
    public void getParticipant(String partId, ParticipantServiceCallback<Participant> callback) {
        TabsCursorWrapper cursor = queryDb(ParticipantTable.NAME, ParticipantTable.Cols.UUID + " = ?", new String [] { partId });

        try {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Participant participant = cursor.getParticipant();
                callback.onLoaded(participant);
            } else {
                callback.onDataNotAvailable();
            }
        }
        finally {
            cursor.close();
        }

    }

    private ContentValues getParticipantContentValues (Participant participant) {
        ContentValues values = new ContentValues();
        values.put(ParticipantTable.Cols.UUID, participant.getId());
        values.put (ParticipantTable.Cols.NAME, participant.getName());
        values.put (ParticipantTable.Cols.EMAIL, participant.getEmail());
        values.put (ParticipantTable.Cols.NUMBER, participant.getNumber());
        values.put (ParticipantTable.Cols.TAB_ID, participant.getTabId());

        return values;
    }

    @Override
    public void saveParticipant (Participant participant) {
        ContentValues values = getParticipantContentValues(participant);
        mDatabase.insert(ParticipantTable.NAME, null, values);
    }

    @Override
    public int updateParticipant(Participant participant) {
        ContentValues values = getParticipantContentValues (participant);

        return mDatabase.update(ParticipantTable.NAME, values, ParticipantTable.Cols.UUID + " = ?",
                new String[] { String.valueOf(participant.getId()) });
    }

    @Override
    public void deleteParticipant(Participant participant) {
        deleteEntry(ParticipantTable.NAME, ParticipantTable.Cols.UUID + " = ?", participant.getId());
//        mDatabase.beginTransaction();
//        try {
//            mDatabase.delete(ParticipantTable.NAME, ParticipantTable.Cols.UUID + " = ?",
//                    new String[] { String.valueOf(participant.getId()) });
//            mDatabase.setTransactionSuccessful();
//        } catch (Exception e) {
//            Timber.d(e.getMessage().concat("Error while trying to delete participant = ").concat(participant.getName()));
//        } finally {
//            mDatabase.endTransaction();
//        }
    }

    @Override
    public void getAllParticipants(final String tabId, final ParticipantServiceCallback<ArrayMap <String, Participant>> callback) {
        ArrayMap <String, Participant> mapParticipants = new ArrayMap<>();

        TabsCursorWrapper cursor = queryDb(ParticipantTable.NAME, ParticipantTable.Cols.TAB_ID + " = ?",
                new String[] { String.valueOf(tabId) });

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mapParticipants.put(cursor.getParticipant().getId(), cursor.getParticipant());
                cursor.moveToNext();
            }
        }
        finally {
            //Always close cursors
            cursor.close();
        }

        callback.onLoaded(mapParticipants);
    }

    private ContentValues getExpenseContentValues (Expense expense) {
        ContentValues values = new ContentValues();
        values.put (ExpenseTable.Cols.UUID, expense.getId());
        values.put (ExpenseTable.Cols.DESCRIPTION, expense.getDescription());
        values.put (ExpenseTable.Cols.VALUE, expense.getValue());
        values.put (ExpenseTable.Cols.TAB_ID, expense.getTabId());

        return values;
    }

    @Override
    public void saveExpense(Expense expense) {
        ContentValues values = getExpenseContentValues(expense);
        mDatabase.insert(ExpenseTable.NAME, null, values);
    }

    @Override
    public int updateExpense(Expense expense) {
        ContentValues values = getExpenseContentValues(expense);
        return mDatabase.update(ExpenseTable.NAME, values, ExpenseTable.Cols.UUID + " = ?",
                new String[] { String.valueOf(expense.getId()) });
    }

    @Override
    public void deleteExpense(Expense expense) {
        deleteEntry(ExpenseTable.NAME, ExpenseTable.Cols.UUID + " = ?", expense.getId());
//        mDatabase.beginTransaction();
//        try {
//            mDatabase.delete(ExpenseTable.NAME, ExpenseTable.Cols.UUID + " = ?",
//                    new String[] { String.valueOf(expense.getId()) });
//            mDatabase.setTransactionSuccessful();
//        } catch (Exception e) {
//            Timber.d(e.getMessage().concat("Error while trying to delete expense = ").concat(expense.getDescription()));
//        } finally {
//            mDatabase.endTransaction();
//        }
    }

    @Override
    public void getExpense(String expId, ExpenseServiceCallback<Expense> callback) {
        TabsCursorWrapper cursor = queryDb(ExpenseTable.NAME, ExpenseTable.Cols.UUID + " = ?", new String [] { expId });

        try {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                Expense expense = cursor.getExpense();
                callback.onLoaded(expense);
            } else {
                callback.onDataNotAvailable();
            }
        }
        finally {
            cursor.close();
        }
    }

    @Override
    public void getAllExpenses(String tabId, ExpenseServiceCallback<ArrayMap <String, Expense>> callback) {
        ArrayMap <String, Expense> mapExpenses = new ArrayMap<>();

        TabsCursorWrapper cursor = queryDb(ExpenseTable.NAME, ExpenseTable.Cols.TAB_ID + " = ?",
                new String[] { String.valueOf(tabId) });

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mapExpenses.put(cursor.getExpense().getId(), cursor.getExpense());
                cursor.moveToNext();
            }
        }
        finally {
            //Always close cursors
            cursor.close();
        }
        callback.onLoaded(mapExpenses);
    }

    private ContentValues getSplitContentValues (Split split) {
        ContentValues values = new ContentValues();
        values.put (SplitTable.Cols.UUID, split.getId());
        values.put (SplitTable.Cols.PARTICIPANT_ID, split.getParticipantId());
        values.put (SplitTable.Cols.EXPENSE_ID, split.getExpenseId());
        values.put (SplitTable.Cols.SPLIT_VAL, split.getValueByParticipant());
        values.put (SplitTable.Cols.TAB_ID, split.getTabId());

        return values;
    }

    @Override
    public void saveSplits(List<Split> splits) {
        try {
            mDatabase.beginTransaction();

            for (Split split : splits) {
                ContentValues values = getSplitContentValues(split);
                mDatabase.insert(SplitTable.NAME, null, values);
            }

            mDatabase.setTransactionSuccessful();

        } catch (SQLiteException ex){
            Timber.e("Exception Inserting Splits:".concat(ex.getMessage()));
        } finally {
            mDatabase.endTransaction();
        }
    }

    @Override
    public void deleteSplitsByExpense(Expense expense) {
        deleteEntry(SplitTable.NAME, SplitTable.Cols.EXPENSE_ID + " = ?", expense.getId());
    }

    @Override
    public void getSplitsByExpense(String expId, SplitServiceCallback<ArrayMap <String, Split>> callback) {
        ArrayMap <String, Split> mapSplits = new ArrayMap<>();

        TabsCursorWrapper cursor = queryDb(SplitTable.NAME, SplitTable.Cols.EXPENSE_ID + " = ?",
                new String[] { String.valueOf(expId) });

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mapSplits.put(cursor.getSplit().getId(), cursor.getSplit());
                cursor.moveToNext();
            }
        }
        finally {
            //Always close cursors
            cursor.close();
        }
        callback.onLoaded(mapSplits);
    }

//    select splits.participant_id, expenses.description, splits.split_participant from splits, expenses
//    where splits.tab_id = 1 and expenses.uuid = splits.expense_id
    private TabsCursorWrapper queryReceiptItem (String [] whereArgs) {

        Cursor cursor = mDatabase.rawQuery(
                "SELECT "+ SplitTable.NAME +"."+ SplitTable.Cols.PARTICIPANT_ID + "," +
                        ParticipantTable.NAME + "." + ParticipantTable.Cols.NAME + "," +
                        ExpenseTable.NAME + "." + ExpenseTable.Cols.DESCRIPTION + "," +
                        SplitTable.NAME + "." + SplitTable.Cols.SPLIT_VAL +
                        " FROM " + SplitTable.NAME + "," + ExpenseTable.NAME + "," + ParticipantTable.NAME +
                        " WHERE " + SplitTable.NAME + "." + SplitTable.Cols.TAB_ID + " = ? " +
                        " AND " + SplitTable.NAME + "." + SplitTable.Cols.PARTICIPANT_ID + " = " +
                        ParticipantTable.NAME + "." + ParticipantTable.Cols.UUID +
                        " AND " + ExpenseTable.NAME + "." + ExpenseTable.Cols.UUID + " = " +
                        SplitTable.NAME + "." + SplitTable.Cols.EXPENSE_ID, whereArgs);

        return new TabsCursorWrapper(cursor);
    }

    @Override
    public void getReceiptItemByTabId(String tabId, ReceipItemServiceCallback<ArrayMap<String, List<ReceiptItem>>> callback) {
        ArrayMap <String, List<ReceiptItem>> mapItems = new ArrayMap<>();

        TabsCursorWrapper cursor = queryReceiptItem(new String [] { String.valueOf(tabId)});

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (mapItems.containsKey(cursor.getReceipItem().getParticipantId())) {
                    mapItems.get(cursor.getReceipItem().getParticipantId()).add(cursor.getReceipItem());
                } else {
                    List<ReceiptItem> list = new ArrayList<>();
                    list.add(cursor.getReceipItem());
                    mapItems.put(cursor.getReceipItem().getParticipantId(), list);
                }
                cursor.moveToNext();
            }
        }
        finally {
            //Always close cursors
            cursor.close();
        }
        callback.onLoaded(mapItems);
    }

    @Override
    public void deleteAllTables() {
        mDatabase.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            mDatabase.delete(TabTable.NAME, null, null);
            mDatabase.delete(ParticipantTable.NAME, null, null);
            mDatabase.delete(ExpenseTable.NAME, null, null);
            mDatabase.delete(SplitTable.NAME, null, null);
            mDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Timber.d(e.getMessage().concat("Error while trying to delete all tabs"));
        } finally {
            mDatabase.endTransaction();
        }
    }
}
