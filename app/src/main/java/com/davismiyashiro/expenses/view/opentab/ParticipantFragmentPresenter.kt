package com.davismiyashiro.expenses.view.opentab

import com.davismiyashiro.expenses.datatypes.Participant
import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.model.Repository

import java.util.ArrayList

/**
 * Class that centralizes access to repository and the fragments
 */

class ParticipantFragmentPresenter(internal var mRepository: Repository) : ParticipantInterfaces.UserActionsListener {

    internal lateinit var mParticipantView: ParticipantInterfaces.ParticipantView

    override fun setParticipantView(view: ParticipantInterfaces.ParticipantView) {
        mParticipantView = view
    }

    override fun loadParticipants(tab: Tab) {
        if (tab != null) {
            mRepository.refreshData() // Fix to get Participants list updated when switching tabs
            mRepository.getParticipants(tab.groupId, object : Repository.LoadParticipantsCallback {
                override fun onParticipantsLoaded(participants: MutableList<Participant>) {
                    var participants = participants
                    if (participants == null) {
                        participants = ArrayList()
                    }
                    mParticipantView.showParticipants(participants)
                }
            })
        }
    }

    override fun removeParticipant(participant: Participant) {
        mRepository.deleteParticipant(participant)
    }
}
