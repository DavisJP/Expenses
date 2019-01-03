package com.davismiyashiro.expenses.view.splitter

import com.davismiyashiro.expenses.datatypes.Expense
import com.davismiyashiro.expenses.datatypes.Participant
import com.davismiyashiro.expenses.datatypes.Split
import com.davismiyashiro.expenses.model.Repository

import java.util.ArrayList

class SplitterPresenterImpl(private val mView: SplitterView.View, private val mModel: Repository) : SplitterView.UserActionsListener {

    private var mParticipants = listOf<Participant>()
    private var mSplits = mutableListOf<Split>()

    override fun validateParticipantsSelected(size: Int) {
        var error = false
        if (size == 0) {
            error = true
            mView.setParticipantSelectedError()
        }
        if (!error)
            mView.splitWithGroup()
    }

    override fun getParticipants(expense: Expense): List<Participant> {

        if (expense != null) {
            mModel.refreshData() // Fix to get Participants list updated when switching tabs
            mModel.getParticipants(expense.tabId, object : Repository.LoadParticipantsCallback {
                override fun onParticipantsLoaded(participants: MutableList<Participant>) {
                    mParticipants = participants
                }
            })
        }

        return mParticipants
    }

    override fun saveSplits(payers: List<Participant>, expense: Expense) {

        //Delete Splits before adding new ones
        mModel.deleteSplitsByExpense(expense)

        for (count in payers.indices) {
            val split = Split(payers[count].id, expense.id, payers.size, expense.value / payers.size, expense.tabId)
            mSplits.add(split)
        }

        mModel.saveSplits(mSplits)
    }

}
