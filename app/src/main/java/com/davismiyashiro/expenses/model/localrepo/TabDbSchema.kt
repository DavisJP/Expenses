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
