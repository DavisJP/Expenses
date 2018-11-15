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
import com.davismiyashiro.expenses.model.RepositoryDataSource;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.davismiyashiro.expenses.model.localrepo.TabDbSchema.*;

/**
 * Implementation of local Database with SQLiteDatabase
 */
public class RepositoryDataSourceLocal implements RepositoryDataSource {

    private static SQLiteDatabase mDatabase;
    private static RepositoryDataSourceLocal mTabRepositoryDataSourceLocal;

    public static synchronized RepositoryDataSourceLocal getInstance (Context context) {
        if (mTabRepositoryDataSourceLocal == null)
            mTabRepositoryDataSourceLocal = new RepositoryDataSourceLocal(context);

        return mTabRepositoryDataSourceLocal;
    }

    //private to prevent direct instantiation (Changed to support Injection)
    public RepositoryDataSourceLocal(Context context) {
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
        return new TabsCursorWrapper(cursor);
    }

    @Override
    public void getAllTabs(final TabServiceCallback<ArrayMap <String, Tab>> callback) {

                ArrayMap <String, Tab> mapTabs = new ArrayMap<>();

                TabsCursorWrapper cursor = queryDb(TabTable.TABLE_NAME, null, null);

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
        TabsCursorWrapper cursor = queryDb(TabTable.TABLE_NAME, TabTable.UUID + " = ?", new String [] { tabId });

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
        values.put(TabTable.UUID, tab.getGroupId());
        values.put (TabTable.GROUPNAME, tab.getGroupName());

        return values;
    }

    @Override
    public void saveTab(Tab tab) {
        ContentValues values = getTabContentValues(tab);
        mDatabase.insert(TabTable.TABLE_NAME, null, values);
    }

    @Override
    public int updateTabName(Tab tab, String name) {
        ContentValues values = new ContentValues();
        values.put(TabTable.GROUPNAME, name);

        // Updating Tab name by Tab.groupId
        return mDatabase.update(TabTable.TABLE_NAME, values, TabTable.UUID + " = ?",
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
        deleteEntry(TabTable.TABLE_NAME, TabTable.UUID + " = ?", tabId);
    }

    @Override
    public void getParticipant(String partId, ParticipantServiceCallback<Participant> callback) {
        TabsCursorWrapper cursor = queryDb(ParticipantTable.TABLE_NAME, ParticipantTable.UUID + " = ?", new String [] { partId });

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
        values.put(ParticipantTable.UUID, participant.getId());
        values.put (ParticipantTable.NAME, participant.getName());
        values.put (ParticipantTable.EMAIL, participant.getEmail());
        values.put (ParticipantTable.NUMBER, participant.getNumber());
        values.put (ParticipantTable.TAB_ID, participant.getTabId());

        return values;
    }

    @Override
    public void saveParticipant (Participant participant) {
        ContentValues values = getParticipantContentValues(participant);
        mDatabase.insert(ParticipantTable.TABLE_NAME, null, values);
    }

    @Override
    public int updateParticipant(Participant participant) {
        ContentValues values = getParticipantContentValues (participant);

        return mDatabase.update(ParticipantTable.TABLE_NAME, values, ParticipantTable.UUID + " = ?",
                new String[] { String.valueOf(participant.getId()) });
    }

    @Override
    public void deleteParticipant(Participant participant) {
        deleteEntry(ParticipantTable.TABLE_NAME, ParticipantTable.UUID + " = ?", participant.getId());
    }

    @Override
    public void getAllParticipants(final String tabId, final ParticipantServiceCallback<ArrayMap <String, Participant>> callback) {
        ArrayMap <String, Participant> mapParticipants = new ArrayMap<>();

        TabsCursorWrapper cursor = queryDb(ParticipantTable.TABLE_NAME, ParticipantTable.TAB_ID + " = ?",
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
        values.put (ExpenseTable.UUID, expense.getId());
        values.put (ExpenseTable.DESCRIPTION, expense.getDescription());
        values.put (ExpenseTable.VALUE, expense.getValue());
        values.put (ExpenseTable.TAB_ID, expense.getTabId());

        return values;
    }

    @Override
    public void saveExpense(Expense expense) {
        ContentValues values = getExpenseContentValues(expense);
        mDatabase.insert(ExpenseTable.TABLE_NAME, null, values);
    }

    @Override
    public int updateExpense(Expense expense) {
        ContentValues values = getExpenseContentValues(expense);
        return mDatabase.update(ExpenseTable.TABLE_NAME, values, ExpenseTable.UUID + " = ?",
                new String[] { String.valueOf(expense.getId()) });
    }

    @Override
    public void deleteExpense(Expense expense) {
        deleteEntry(ExpenseTable.TABLE_NAME, ExpenseTable.UUID + " = ?", expense.getId());
    }

    @Override
    public void getExpense(String expId, ExpenseServiceCallback<Expense> callback) {
        TabsCursorWrapper cursor = queryDb(ExpenseTable.TABLE_NAME, ExpenseTable.UUID + " = ?", new String [] { expId });

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

        TabsCursorWrapper cursor = queryDb(ExpenseTable.TABLE_NAME, ExpenseTable.TAB_ID + " = ?",
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
        values.put (SplitTable.UUID, split.getId());
        values.put (SplitTable.PARTICIPANT_ID, split.getParticipantId());
        values.put (SplitTable.EXPENSE_ID, split.getExpenseId());
        values.put (SplitTable.SPLIT_VAL, split.getValueByParticipant());
        values.put (SplitTable.TAB_ID, split.getTabId());

        return values;
    }

    @Override
    public void saveSplits(List<Split> splits) {
        try {
            mDatabase.beginTransaction();

            for (Split split : splits) {
                ContentValues values = getSplitContentValues(split);
                mDatabase.insert(SplitTable.TABLE_NAME, null, values);
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
        deleteEntry(SplitTable.TABLE_NAME, SplitTable.EXPENSE_ID + " = ?", expense.getId());
    }

    @Override
    public void getSplitsByExpense(String expId, SplitServiceCallback<ArrayMap <String, Split>> callback) {
        ArrayMap <String, Split> mapSplits = new ArrayMap<>();

        TabsCursorWrapper cursor = queryDb(SplitTable.TABLE_NAME, SplitTable.EXPENSE_ID + " = ?",
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
                "SELECT "+ SplitTable.TABLE_NAME +"."+ SplitTable.PARTICIPANT_ID + "," +
                        ParticipantTable.TABLE_NAME + "." + ParticipantTable.NAME + "," +
                        ExpenseTable.TABLE_NAME + "." + ExpenseTable.DESCRIPTION + "," +
                        SplitTable.TABLE_NAME + "." + SplitTable.SPLIT_VAL +
                        " FROM " + SplitTable.TABLE_NAME + "," + ExpenseTable.TABLE_NAME + "," + ParticipantTable.TABLE_NAME +
                        " WHERE " + SplitTable.TABLE_NAME + "." + SplitTable.TAB_ID + " = ? " +
                        " AND " + SplitTable.TABLE_NAME + "." + SplitTable.PARTICIPANT_ID + " = " +
                        ParticipantTable.TABLE_NAME + "." + ParticipantTable.UUID +
                        " AND " + ExpenseTable.TABLE_NAME + "." + ExpenseTable.UUID + " = " +
                        SplitTable.TABLE_NAME + "." + SplitTable.EXPENSE_ID, whereArgs);

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
            mDatabase.delete(TabTable.TABLE_NAME, null, null);
            mDatabase.delete(ParticipantTable.TABLE_NAME, null, null);
            mDatabase.delete(ExpenseTable.TABLE_NAME, null, null);
            mDatabase.delete(SplitTable.TABLE_NAME, null, null);
            mDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Timber.d(e.getMessage().concat("Error while trying to delete all tabs"));
        } finally {
            mDatabase.endTransaction();
        }
    }
}
