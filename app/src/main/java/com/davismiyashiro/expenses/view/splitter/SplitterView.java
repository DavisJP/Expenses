package com.davismiyashiro.expenses.view.splitter;

import com.davismiyashiro.expenses.datatypes.Expense;
import com.davismiyashiro.expenses.datatypes.Participant;

import java.util.List;

interface SplitterView {

    interface View {
        void splitWithGroup();
        void setParticipantSelectedError();
    }

    interface UserActionsListener {
        void validateParticipantsSelected (int size);
        List<Participant> getParticipants (Expense expense);
        void saveSplits (List<Participant> payers, Expense expense);
    }
}
