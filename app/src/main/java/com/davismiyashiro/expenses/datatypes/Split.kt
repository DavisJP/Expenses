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
package com.davismiyashiro.expenses.datatypes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

/**
 * Class that represents each split Expense
 */
@Parcelize
data class Split(
    var id: String,
    var participantId: String,
    var expenseId: String,
    var numberParticipants: Int,
    var valueByParticipant: Double,
    var tabId: String
) : Parcelable {

    constructor (
        participantId: String,
        expenseId: String,
        numberParticipants: Int,
        valueByParticipant: Double,
        tabId: String
    ) :
            this(UUID.randomUUID().toString(),
                    participantId,
                    expenseId,
                    numberParticipants,
                    valueByParticipant,
                    tabId)

    companion object {
        fun retrieveSplit(
            id: String,
            participantId: String,
            expenseId: String,
            numberParticipants: Int,
            valueByParticipant: Double,
            tabId: String
        ): Split {
            var split = Split(participantId, expenseId, numberParticipants, valueByParticipant, tabId)
            split.id = id
            return split
        }
    }
}