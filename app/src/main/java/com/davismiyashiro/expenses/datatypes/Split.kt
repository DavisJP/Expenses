package com.davismiyashiro.expenses.datatypes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Class that represents each split Expense
 */
@Parcelize
data class Split(var id: String,
                 var participantId: String,
                 var expenseId: String,
                 var numberParticipants: Int,
                 var valueByParticipant: Double,
                 var tabId: String) : Parcelable {

    constructor (participantId: String,
                 expenseId: String,
                 numberParticipants: Int,
                 valueByParticipant: Double,
                 tabId: String) :
            this(UUID.randomUUID().toString(),
                    participantId,
                    expenseId,
                    numberParticipants,
                    valueByParticipant,
                    tabId)

    companion object {
        fun retrieveSplit(id: String,
                          participantId: String,
                          expenseId: String,
                          numberParticipants: Int,
                          valueByParticipant: Double,
                          tabId: String): Split {
            var split = Split(participantId, expenseId, numberParticipants, valueByParticipant, tabId)
            split.id = id
            return split
        }
    }
}