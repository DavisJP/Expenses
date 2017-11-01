package com.davismiyashiro.expenses.view.addparticipant;

import android.text.TextUtils;

import com.davismiyashiro.expenses.datatypes.Participant;
import com.davismiyashiro.expenses.model.TabRepository;

public class ParticipantPresenterImpl implements ParticipantInterfaces.UserActionsListener {

    ParticipantInterfaces.View mView;
    TabRepository mRepo;

  public ParticipantPresenterImpl (ParticipantInterfaces.View view, TabRepository model) {
      mView = view;
      mRepo = model;
  }

    @Override
    public void validateInputFields(String name, String email, String phone) {
        boolean error = false;

        if (TextUtils.isEmpty(name)) {
            mView.showNameError(true);
            error = true;
        } else {
            mView.showNameError(false);
        }

        if (TextUtils.isEmpty(email)) {
            mView.showEmailError(true);
            error = true;
        } else {
            mView.showEmailError(false);
        }

        if (TextUtils.isEmpty(phone)) {
            mView.showPhoneError(true);
            error = true;
        } else {
            mView.showPhoneError(false);
        }

        if (!error)
            mView.onSuccess();
    }

    @Override
    public void addParticipant(Participant participant) {
        mRepo.saveParticipant(participant);
    }

    @Override
    public void updateParticipant(Participant participant) {
        mRepo.updateParticipant(participant);
    }

//    @Override
//    public void loadParticipant() {
//        mRepo.getParticipant(participant);
//    }
}
