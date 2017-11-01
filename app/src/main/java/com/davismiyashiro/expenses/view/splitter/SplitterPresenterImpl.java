package com.davismiyashiro.expenses.view.splitter;

import com.davismiyashiro.expenses.datatypes.Expense;
import com.davismiyashiro.expenses.datatypes.Participant;
import com.davismiyashiro.expenses.datatypes.Split;
import com.davismiyashiro.expenses.model.TabRepository;

import java.util.ArrayList;
import java.util.List;

public class SplitterPresenterImpl implements SplitterView.UserActionsListener {
    private SplitterView.View mView;
    private TabRepository mModel;

    private List<Participant> mParticipants;
    private List<Split> mSplits;

    public SplitterPresenterImpl (SplitterView.View splitterView, TabRepository model) {
        mView = splitterView;
        mModel = model;
    }

    @Override
    public void validateParticipantsSelected(int size) {
        boolean error = false;
        if (size == 0) {
            error = true;
            mView.setParticipantSelectedError();
        }
        if (!error)
            mView.splitWithGroup();
    }

    @Override
    public List <Participant> getParticipants (Expense expense) {

        if (expense!= null) {
            mModel.refreshData(); // Fix to get Participants list updated when switching tabs
            mModel.getParticipants(expense.getTabId(), new TabRepository.LoadParticipantsCallback() {
                @Override
                public void onParticipantsLoaded(List<Participant> participants) {
                    mParticipants = participants;
                }
            });
        }

        if (mParticipants == null) {
            mParticipants = new ArrayList<>();
        }
        return mParticipants;
    }

    public void saveSplits (List<Participant> payers, Expense expense) {

        mSplits = new ArrayList<>();

        //Delete Splits before adding new ones
        mModel.deleteSplitsByExpense(expense);

        for (int count = 0; count < payers.size(); count++){
            Split split = new Split (payers.get(count).getId(), expense.getId(), payers.size(), expense.getValue()/payers.size(), expense.getTabId());
            mSplits.add(split);
        }

        mModel.saveSplits(mSplits);
    }

}
