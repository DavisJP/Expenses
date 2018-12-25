package com.davismiyashiro.expenses.view.addparticipant

import com.davismiyashiro.expenses.datatypes.Participant

/**
 * Created by Davis on 25/08/2016.
 */
interface ParticipantInterfaces {
    interface View {
        fun setParticipantName(name: String)
        fun setParticipantEmail(email: String)
        fun setParticipantPhone(phone: String)
        fun showNameError(value: Boolean)
        fun showEmailError(value: Boolean)
        fun showPhoneError(value: Boolean)
        fun onSuccess()
    }

    interface UserActionsListener {
        fun validateInputFields(name: String, phone: String, email: String)
        fun addParticipant(participant: Participant)
        fun updateParticipant(participant: Participant)
    }
}
