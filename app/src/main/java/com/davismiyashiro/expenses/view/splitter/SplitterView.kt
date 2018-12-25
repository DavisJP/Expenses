package com.davismiyashiro.expenses.view.splitter

import com.davismiyashiro.expenses.datatypes.Expense
import com.davismiyashiro.expenses.datatypes.Participant

interface SplitterView {

    interface View {
        fun splitWithGroup()
        fun setParticipantSelectedError()
    }

    interface UserActionsListener {
        fun validateParticipantsSelected(size: Int)
        fun getParticipants(expense: Expense): List<Participant>
        fun saveSplits(payers: List<Participant>, expense: Expense)
    }
}
