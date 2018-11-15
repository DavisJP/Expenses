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
        String  uuid = getString(getColumnIndex(TabTable.UUID));
        String tabName = getString(getColumnIndex(TabTable.GROUPNAME));

        return new Tab (uuid, tabName);
    }

    public Participant getParticipant() {
        String uuid = getString(getColumnIndex(ParticipantTable.UUID));
        String partName = getString(getColumnIndex(ParticipantTable.NAME));
        String partEmail = getString(getColumnIndex(ParticipantTable.EMAIL));
        String partNumber = getString(getColumnIndex(ParticipantTable.NUMBER));
        String tabId = getString(getColumnIndex(ParticipantTable.TAB_ID));

        return Participant.Companion.retrieveParticipant(uuid, partName, partEmail, partNumber, tabId);
    }

    public Expense getExpense() {
        String expId = getString(getColumnIndex(ExpenseTable.UUID));
        String expDesc = getString(getColumnIndex(ExpenseTable.DESCRIPTION));
        double expValue = getDouble(getColumnIndex(ExpenseTable.VALUE));
        String expTabId = getString(getColumnIndex(ExpenseTable.TAB_ID));

        return Expense.Companion.retrieveExpense(expId, expDesc, expValue, expTabId);
    }

    public Split getSplit() {
        String splitId = getString(getColumnIndex(SplitTable.UUID));
        String partId = getString(getColumnIndex(SplitTable.PARTICIPANT_ID));
        String expId = getString(getColumnIndex(SplitTable.EXPENSE_ID));
        double splitVal = getDouble(getColumnIndex(SplitTable.SPLIT_VAL));
        String splitTabId = getString(getColumnIndex(SplitTable.TAB_ID));

        return Split.Companion.retrieveSplit(splitId, partId, expId, 0, splitVal, splitTabId);
    }

    public ReceiptItem getReceipItem() {
        String partId = getString(getColumnIndex(SplitTable.PARTICIPANT_ID));
        String partName = getString(getColumnIndex(ParticipantTable.NAME));
        String expDesc = getString(getColumnIndex(ExpenseTable.DESCRIPTION));
        double splitVal = getDouble(getColumnIndex(SplitTable.SPLIT_VAL));

        return new ReceiptItem(partId, partName, expDesc, splitVal);
    }
}
