package com.davismiyashiro.expenses.datatypes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Object that encapsulates data for the Participant
 */
@Parcelize
data class Participant(var id: String,
                       var name: String,
                       var email: String,
                       var number: String,
                       var tabId: String) : Parcelable {
    constructor(name: String, email: String, number: String, tabId: String) : this(UUID.randomUUID().toString().replace("-".toRegex(), ""), name, email, number, tabId)

    companion object {
        fun retrieveParticipant(id: String, name: String, email: String, number: String, tabId: String): Participant {

            val participant = Participant(name, email, number, tabId)
            participant.id = id
            return participant
        }
    }
}