package com.davismiyashiro.expenses.view.opentab;

import com.davismiyashiro.expenses.datatypes.Participant;
import com.davismiyashiro.expenses.datatypes.Tab;
import com.davismiyashiro.expenses.model.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that centralizes access to repository and the fragments
 */

public class ParticipantFragmentPresenter implements ParticipantInterfaces.UserActionsListener {

    ParticipantInterfaces.ParticipantView mParticipantView;
    Repository mRepository;

    public ParticipantFragmentPresenter(Repository model) {
        mRepository = model;
    }

    @Override
    public void setParticipantView(ParticipantInterfaces.ParticipantView view) {
        mParticipantView = view;
    }

    @Override
    public void loadParticipants(Tab tab) {
        if (tab!= null) {
            mRepository.refreshData(); // Fix to get Participants list updated when switching tabs
            mRepository.getParticipants(tab.getGroupId(), new Repository.LoadParticipantsCallback() {
                @Override
                public void onParticipantsLoaded(List<Participant> participants) {
                    if (participants == null) {
                        participants = new ArrayList<>();
                    }
                    mParticipantView.showParticipants(participants);
                }
            });
        }
    }

    @Override
    public void removeParticipant(Participant participant) {
        mRepository.deleteParticipant(participant);
    }
}
