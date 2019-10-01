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

import android.database.Cursor
import android.database.CursorWrapper

import com.davismiyashiro.expenses.datatypes.Expense
import com.davismiyashiro.expenses.datatypes.Participant
import com.davismiyashiro.expenses.datatypes.ReceiptItem
import com.davismiyashiro.expenses.datatypes.Split
import com.davismiyashiro.expenses.datatypes.Tab

import com.davismiyashiro.expenses.model.localrepo.TabDbSchema.* // ktlint-disable no-wildcard-imports

/**
 * Adds wrapper methods to make it easier to retrieve data from the cursor
 */
class TabsCursorWrapper(cursor: Cursor) : CursorWrapper(cursor) {

    /**
     * Returns object from DB column
     *
     * @return
     */
    val tab: Tab
        get() {
            val uuid = getString(getColumnIndex(TabTable.UUID))
            val tabName = getString(getColumnIndex(TabTable.GROUPNAME))

            return Tab(uuid, tabName)
        }

    val participant: Participant
        get() {
            val uuid = getString(getColumnIndex(ParticipantTable.UUID))
            val partName = getString(getColumnIndex(ParticipantTable.NAME))
            val partEmail = getString(getColumnIndex(ParticipantTable.EMAIL))
            val partNumber = getString(getColumnIndex(ParticipantTable.NUMBER))
            val tabId = getString(getColumnIndex(ParticipantTable.TAB_ID))

            return Participant.retrieveParticipant(uuid, partName, partEmail, partNumber, tabId)
        }

    val expense: Expense
        get() {
            val expId = getString(getColumnIndex(ExpenseTable.UUID))
            val expDesc = getString(getColumnIndex(ExpenseTable.DESCRIPTION))
            val expValue = getDouble(getColumnIndex(ExpenseTable.VALUE))
            val expTabId = getString(getColumnIndex(ExpenseTable.TAB_ID))

            return Expense.retrieveExpense(expId, expDesc, expValue, expTabId)
        }

    val split: Split
        get() {
            val splitId = getString(getColumnIndex(SplitTable.UUID))
            val partId = getString(getColumnIndex(SplitTable.PARTICIPANT_ID))
            val expId = getString(getColumnIndex(SplitTable.EXPENSE_ID))
            val splitVal = getDouble(getColumnIndex(SplitTable.SPLIT_VAL))
            val splitTabId = getString(getColumnIndex(SplitTable.TAB_ID))

            return Split.retrieveSplit(splitId, partId, expId, 0, splitVal, splitTabId)
        }

    val receiptItem: ReceiptItem
        get() {
            val partId = getString(getColumnIndex(SplitTable.PARTICIPANT_ID))
            val partName = getString(getColumnIndex(ParticipantTable.NAME))
            val expDesc = getString(getColumnIndex(ExpenseTable.DESCRIPTION))
            val splitVal = getDouble(getColumnIndex(SplitTable.SPLIT_VAL))

            return ReceiptItem(partId, partName, expDesc, splitVal)
        }
}
