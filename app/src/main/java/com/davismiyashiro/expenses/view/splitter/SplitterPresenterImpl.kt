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
package com.davismiyashiro.expenses.view.splitter

import com.davismiyashiro.expenses.datatypes.Expense
import com.davismiyashiro.expenses.datatypes.Participant
import com.davismiyashiro.expenses.datatypes.Split
import com.davismiyashiro.expenses.model.Repository

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

        // Delete Splits before adding new ones
        mModel.deleteSplitsByExpense(expense)

        for (count in payers.indices) {
            val split = Split(payers[count].id, expense.id, payers.size, expense.value / payers.size, expense.tabId)
            mSplits.add(split)
        }

        mModel.saveSplits(mSplits)
    }
}
