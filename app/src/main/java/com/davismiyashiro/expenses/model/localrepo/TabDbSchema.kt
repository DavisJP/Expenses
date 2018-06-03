package com.davismiyashiro.expenses.model.localrepo

/**
 * Class that defines the tables attributes
 */
class TabDbSchema {
    object TabTable {
        const val TABLE_NAME = "tabs"
        const val UUID = "uuid"
        const val GROUPNAME = "groupName"
    }

    object ParticipantTable {
        const val TABLE_NAME = "participants"
        const val UUID = "uuid"
        const val NAME = "name"
        const val EMAIL = "email"
        const val NUMBER = "number"
        const val TAB_ID = "tab_id"
    }

    object ExpenseTable {
        const val TABLE_NAME = "expenses"
        const val UUID = "uuid"
        const val DESCRIPTION = "description"
        const val VALUE = "value"
        const val TAB_ID = "tab_id"
    }


    object SplitTable {
        const val TABLE_NAME = "splits"
        const val UUID = "uuid"
        const val PARTICIPANT_ID = "participantId"
        const val EXPENSE_ID = "expenseId"
        const val SPLIT_VAL = "valueByParticipant"
        const val TAB_ID = "tabId"
    }
}
