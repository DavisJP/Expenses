package com.davismiyashiro.expenses.view.opentab

import com.davismiyashiro.expenses.datatypes.Participant
import com.davismiyashiro.expenses.datatypes.Tab

/**
 * Created by Davis Miyashiro on 09/12/2016.
 */

interface ParticipantInterfaces {
    interface ParticipantView {
        fun showParticipants(participants: MutableList<Participant>)
    }

    interface UserActionsListener {

        fun setParticipantView(view: ParticipantView)

        fun loadParticipants(tab: Tab)

        fun removeParticipant(participant: Participant)

    }
}
