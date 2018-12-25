package com.davismiyashiro.expenses.view.addparticipant

import android.text.TextUtils

import com.davismiyashiro.expenses.datatypes.Participant
import com.davismiyashiro.expenses.model.Repository

class ParticipantPresenterImpl(internal var mView: ParticipantInterfaces.View, internal var mRepo: Repository) : ParticipantInterfaces.UserActionsListener {

    override fun validateInputFields(name: String, email: String, phone: String) {
        var error = false

        if (TextUtils.isEmpty(name)) {
            mView.showNameError(true)
            error = true
        } else {
            mView.showNameError(false)
        }

        if (TextUtils.isEmpty(email)) {
            mView.showEmailError(true)
            error = true
        } else {
            mView.showEmailError(false)
        }

        if (TextUtils.isEmpty(phone)) {
            mView.showPhoneError(true)
            error = true
        } else {
            mView.showPhoneError(false)
        }

        if (!error)
            mView.onSuccess()
    }

    override fun addParticipant(participant: Participant) {
        mRepo.saveParticipant(participant)
    }

    override fun updateParticipant(participant: Participant) {
        mRepo.updateParticipant(participant)
    }
}
