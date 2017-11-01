package com.davismiyashiro.expenses.model.localrepo;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.davismiyashiro.expenses.datatypes.Expense;
import com.davismiyashiro.expenses.datatypes.Participant;
import com.davismiyashiro.expenses.datatypes.ReceiptItem;
import com.davismiyashiro.expenses.datatypes.Split;
import com.davismiyashiro.expenses.datatypes.Tab;

import static com.davismiyashiro.expenses.model.localrepo.TabDbSchema.*;

/**
 * Adds wrapper methods to make it easier to retrieve data from the cursor
 */
public class TabsCursorWrapper extends CursorWrapper{

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public TabsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * Returns object from DB column
     *
     * @return
     */
    public Tab getTab () {
        String  uuid = getString(getColumnIndex(TabTable.Cols.UUID));
        String tabName = getString(getColumnIndex(TabTable.Cols.GROUPNAME));

        return new Tab (uuid, tabName);
    }

    public Participant getParticipant() {
        String uuid = getString(getColumnIndex(ParticipantTable.Cols.UUID));
        String partName = getString(getColumnIndex(ParticipantTable.Cols.NAME));
        String partEmail = getString(getColumnIndex(ParticipantTable.Cols.EMAIL));
        String partNumber = getString(getColumnIndex(ParticipantTable.Cols.NUMBER));
        String tabId = getString(getColumnIndex(ParticipantTable.Cols.TAB_ID));

        return Participant.retrieveParticipant(uuid, partName, partEmail, partNumber, tabId);
    }

    public Expense getExpense() {
        String expId = getString(getColumnIndex(ExpenseTable.Cols.UUID));
        String expDesc = getString(getColumnIndex(ExpenseTable.Cols.DESCRIPTION));
        double expValue = getDouble(getColumnIndex(ExpenseTable.Cols.VALUE));
        String expTabId = getString(getColumnIndex(ExpenseTable.Cols.TAB_ID));

        return Expense.retrieveExpense(expId, expDesc, expValue, expTabId);
    }

    public Split getSplit() {
        String splitId = getString(getColumnIndex(SplitTable.Cols.UUID));
        String partId = getString(getColumnIndex(SplitTable.Cols.PARTICIPANT_ID));
        String expId = getString(getColumnIndex(SplitTable.Cols.EXPENSE_ID));
        double splitVal = getDouble(getColumnIndex(SplitTable.Cols.SPLIT_VAL));
        String splitTabId = getString(getColumnIndex(SplitTable.Cols.TAB_ID));

        return Split.retrieveSplit(splitId, partId, expId, 0, splitVal, splitTabId);
    }

    public ReceiptItem getReceipItem() {
        String partId = getString(getColumnIndex(SplitTable.Cols.PARTICIPANT_ID));
        String partName = getString(getColumnIndex(ParticipantTable.Cols.NAME));
        String expDesc = getString(getColumnIndex(ExpenseTable.Cols.DESCRIPTION));
        double splitVal = getDouble(getColumnIndex(SplitTable.Cols.SPLIT_VAL));

        return new ReceiptItem(partId, partName, expDesc, splitVal);
    }
}
