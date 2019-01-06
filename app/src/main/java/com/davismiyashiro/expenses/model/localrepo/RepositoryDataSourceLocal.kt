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
package com.davismiyashiro.expenses.model.localrepo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.support.v4.util.ArrayMap

import com.davismiyashiro.expenses.datatypes.Expense
import com.davismiyashiro.expenses.datatypes.Participant
import com.davismiyashiro.expenses.datatypes.ReceiptItem
import com.davismiyashiro.expenses.datatypes.Split
import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.model.RepositoryDataSource

import java.util.ArrayList

import timber.log.Timber

import com.davismiyashiro.expenses.model.localrepo.TabDbSchema.* // ktlint-disable no-wildcard-imports

/**
 * Implementation of local Database with SQLiteDatabase
 */
class RepositoryDataSourceLocal // private to prevent direct instantiation (Changed to support Injection)
(context: Context) : RepositoryDataSource {

    private var mDatabase: SQLiteDatabase

    init {
        val dbHelper = TabsDbHelper(context)
        mDatabase = dbHelper.writableDatabase
    }

    private fun queryDb(tableName: String, whereClause: String?, whereArgs: Array<String>?): TabsCursorWrapper {
        val cursor = mDatabase.query(
                tableName, null, // Columns - null selects all columns
                whereClause,
                whereArgs, null, null, null // orderBy
        ) // groupBy
        // having
        return TabsCursorWrapper(cursor)
    }

    override fun getAllTabs(callback: RepositoryDataSource.TabServiceCallback<ArrayMap<String, Tab>>) {

        val mapTabs = ArrayMap<String, Tab>()

        val cursor = queryDb(TabTable.TABLE_NAME, null, null)

        try {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                mapTabs[cursor.tab.groupId] = cursor.tab
                cursor.moveToNext()
            }
        } finally {
            // Always close cursors
            cursor.close()
        }
        callback.onLoaded(mapTabs)
    }

    override fun getTab(tabId: String, callback: RepositoryDataSource.TabServiceCallback<Tab>) {
        val cursor = queryDb(TabTable.TABLE_NAME, TabTable.UUID + " = ?", arrayOf(tabId))

        try {
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                val tab = cursor.tab
                callback.onLoaded(tab)
            }
        } finally {
            cursor.close()
        }
    }

    private fun getTabContentValues(tab: Tab): ContentValues {
        val values = ContentValues()
        values.put(TabTable.UUID, tab.groupId)
        values.put(TabTable.GROUPNAME, tab.groupName)

        return values
    }

    override fun saveTab(tab: Tab) {
        val values = getTabContentValues(tab)
        mDatabase.insert(TabTable.TABLE_NAME, null, values)
    }

    override fun updateTabName(tab: Tab, name: String): Int {
        val values = ContentValues()
        values.put(TabTable.GROUPNAME, name)

        // Updating Tab name by Tab.groupId
        return mDatabase.update(TabTable.TABLE_NAME, values, TabTable.UUID + " = ?",
                arrayOf(tab.groupId))
    }

    private fun deleteEntry(tableName: String, selection: String, tabId: String) {
        mDatabase.beginTransaction()
        try {
            mDatabase.delete(tableName, selection,
                    arrayOf(tabId))
            mDatabase.setTransactionSuccessful()
        } catch (e: Exception) {
            Timber.d(e.message + "Error while trying to delete = " + tabId)
        } finally {
            mDatabase.endTransaction()
        }
    }

    override fun deleteTab(tabId: String) {
        deleteEntry(TabTable.TABLE_NAME, TabTable.UUID + " = ?", tabId)
    }

    override fun getParticipant(partId: String, callback: RepositoryDataSource.ParticipantServiceCallback<Participant>) {
        val cursor = queryDb(ParticipantTable.TABLE_NAME, ParticipantTable.UUID + " = ?", arrayOf(partId))

        try {
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                val participant = cursor.participant
                callback.onLoaded(participant)
            } else {
                callback.onDataNotAvailable()
            }
        } finally {
            cursor.close()
        }
    }

    private fun getParticipantContentValues(participant: Participant): ContentValues {
        val values = ContentValues()
        values.put(ParticipantTable.UUID, participant.id)
        values.put(ParticipantTable.NAME, participant.name)
        values.put(ParticipantTable.EMAIL, participant.email)
        values.put(ParticipantTable.NUMBER, participant.number)
        values.put(ParticipantTable.TAB_ID, participant.tabId)

        return values
    }

    override fun saveParticipant(participant: Participant) {
        val values = getParticipantContentValues(participant)
        mDatabase.insert(ParticipantTable.TABLE_NAME, null, values)
    }

    override fun updateParticipant(participant: Participant): Int {
        val values = getParticipantContentValues(participant)

        return mDatabase.update(ParticipantTable.TABLE_NAME, values, ParticipantTable.UUID + " = ?",
                arrayOf(participant.id))
    }

    override fun deleteParticipant(participant: Participant) {
        deleteEntry(ParticipantTable.TABLE_NAME, ParticipantTable.UUID + " = ?", participant.id)
    }

    override fun getAllParticipants(tabId: String, callback: RepositoryDataSource.ParticipantServiceCallback<ArrayMap<String, Participant>>) {
        val mapParticipants = ArrayMap<String, Participant>()

        val cursor = queryDb(ParticipantTable.TABLE_NAME, ParticipantTable.TAB_ID + " = ?",
                arrayOf(tabId))

        try {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                mapParticipants[cursor.participant.id] = cursor.participant
                cursor.moveToNext()
            }
        } finally {
            // Always close cursors
            cursor.close()
        }

        callback.onLoaded(mapParticipants)
    }

    private fun getExpenseContentValues(expense: Expense): ContentValues {
        val values = ContentValues()
        values.put(ExpenseTable.UUID, expense.id)
        values.put(ExpenseTable.DESCRIPTION, expense.description)
        values.put(ExpenseTable.VALUE, expense.value)
        values.put(ExpenseTable.TAB_ID, expense.tabId)

        return values
    }

    override fun saveExpense(expense: Expense) {
        val values = getExpenseContentValues(expense)
        mDatabase.insert(ExpenseTable.TABLE_NAME, null, values)
    }

    override fun updateExpense(expense: Expense): Int {
        val values = getExpenseContentValues(expense)
        return mDatabase.update(ExpenseTable.TABLE_NAME, values, ExpenseTable.UUID + " = ?",
                arrayOf(expense.id))
    }

    override fun deleteExpense(expense: Expense) {
        deleteEntry(ExpenseTable.TABLE_NAME, ExpenseTable.UUID + " = ?", expense.id)
    }

    override fun getExpense(expId: String, callback: RepositoryDataSource.ExpenseServiceCallback<Expense>) {
        val cursor = queryDb(ExpenseTable.TABLE_NAME, ExpenseTable.UUID + " = ?", arrayOf(expId))

        try {
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                val expense = cursor.expense
                callback.onLoaded(expense)
            } else {
                callback.onDataNotAvailable()
            }
        } finally {
            cursor.close()
        }
    }

    override fun getAllExpenses(tabId: String, callback: RepositoryDataSource.ExpenseServiceCallback<ArrayMap<String, Expense>>) {
        val mapExpenses = ArrayMap<String, Expense>()

        val cursor = queryDb(ExpenseTable.TABLE_NAME, ExpenseTable.TAB_ID + " = ?",
                arrayOf(tabId))

        try {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                mapExpenses[cursor.expense.id] = cursor.expense
                cursor.moveToNext()
            }
        } finally {
            // Always close cursors
            cursor.close()
        }
        callback.onLoaded(mapExpenses)
    }

    private fun getSplitContentValues(split: Split): ContentValues {
        val values = ContentValues()
        values.put(SplitTable.UUID, split.id)
        values.put(SplitTable.PARTICIPANT_ID, split.participantId)
        values.put(SplitTable.EXPENSE_ID, split.expenseId)
        values.put(SplitTable.SPLIT_VAL, split.valueByParticipant)
        values.put(SplitTable.TAB_ID, split.tabId)

        return values
    }

    override fun saveSplits(splits: List<Split>) {
        try {
            mDatabase.beginTransaction()

            for (split in splits) {
                val values = getSplitContentValues(split)
                mDatabase.insert(SplitTable.TABLE_NAME, null, values)
            }

            mDatabase.setTransactionSuccessful()
        } catch (ex: SQLiteException) {
            Timber.e("Exception Inserting Splits:" + ex.message)
        } finally {
            mDatabase.endTransaction()
        }
    }

    override fun deleteSplitsByExpense(expense: Expense) {
        deleteEntry(SplitTable.TABLE_NAME, SplitTable.EXPENSE_ID + " = ?", expense.id)
    }

    override fun getSplitsByExpense(expId: String, callback: RepositoryDataSource.SplitServiceCallback<ArrayMap<String, Split>>) {
        val mapSplits = ArrayMap<String, Split>()

        val cursor = queryDb(SplitTable.TABLE_NAME, SplitTable.EXPENSE_ID + " = ?",
                arrayOf(expId))

        try {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                mapSplits[cursor.split.id] = cursor.split
                cursor.moveToNext()
            }
        } finally {
            // Always close cursors
            cursor.close()
        }
        callback.onLoaded(mapSplits)
    }

    //    select splits.participant_id, expenses.description, splits.split_participant from splits, expenses
    //    where splits.tab_id = 1 and expenses.uuid = splits.expense_id
    private fun queryReceiptItem(whereArgs: Array<String>): TabsCursorWrapper {

        val cursor = mDatabase.rawQuery(
                "SELECT " + SplitTable.TABLE_NAME + "." + SplitTable.PARTICIPANT_ID + "," +
                        ParticipantTable.TABLE_NAME + "." + ParticipantTable.NAME + "," +
                        ExpenseTable.TABLE_NAME + "." + ExpenseTable.DESCRIPTION + "," +
                        SplitTable.TABLE_NAME + "." + SplitTable.SPLIT_VAL +
                        " FROM " + SplitTable.TABLE_NAME + "," + ExpenseTable.TABLE_NAME + "," + ParticipantTable.TABLE_NAME +
                        " WHERE " + SplitTable.TABLE_NAME + "." + SplitTable.TAB_ID + " = ? " +
                        " AND " + SplitTable.TABLE_NAME + "." + SplitTable.PARTICIPANT_ID + " = " +
                        ParticipantTable.TABLE_NAME + "." + ParticipantTable.UUID +
                        " AND " + ExpenseTable.TABLE_NAME + "." + ExpenseTable.UUID + " = " +
                        SplitTable.TABLE_NAME + "." + SplitTable.EXPENSE_ID, whereArgs)

        return TabsCursorWrapper(cursor)
    }

    override fun getReceiptItemByTabId(tabId: String, callback: RepositoryDataSource.ReceipItemServiceCallback<ArrayMap<String, MutableList<ReceiptItem>>>) {
        val mapItems = ArrayMap<String, MutableList<ReceiptItem>>()

        val cursor = queryReceiptItem(arrayOf(tabId))

        try {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                if (mapItems.containsKey(cursor.receiptItem.participantId)) {
                    mapItems[cursor.receiptItem.participantId]?.add(cursor.receiptItem)
                } else {
                    val list = ArrayList<ReceiptItem>()
                    list.add(cursor.receiptItem)
                    mapItems[cursor.receiptItem.participantId] = list
                }
                cursor.moveToNext()
            }
        } finally {
            // Always close cursors
            cursor.close()
        }
        callback.onLoaded(mapItems)
    }

    override fun deleteAllTables() {
        mDatabase.beginTransaction()
        try {
            // Order of deletions is important when foreign key relationships exist.
            mDatabase.delete(TabTable.TABLE_NAME, null, null)
            mDatabase.delete(ParticipantTable.TABLE_NAME, null, null)
            mDatabase.delete(ExpenseTable.TABLE_NAME, null, null)
            mDatabase.delete(SplitTable.TABLE_NAME, null, null)
            mDatabase.setTransactionSuccessful()
        } catch (e: Exception) {
            Timber.d(e.message + "Error while trying to delete all tabs")
        } finally {
            mDatabase.endTransaction()
        }
    }

    companion object {
        private var mTabRepositoryDataSourceLocal: RepositoryDataSourceLocal? = null

        @Synchronized
        fun getInstance(context: Context): RepositoryDataSourceLocal {
            if (mTabRepositoryDataSourceLocal == null)
                mTabRepositoryDataSourceLocal = RepositoryDataSourceLocal(context)

            return mTabRepositoryDataSourceLocal!!
        }
    }
}
