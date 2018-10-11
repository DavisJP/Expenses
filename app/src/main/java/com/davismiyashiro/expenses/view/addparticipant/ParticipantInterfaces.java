package com.davismiyashiro.expenses.view.addparticipant;

import com.davismiyashiro.expenses.datatypes.Participant;

/**
 * Created by Davis on 25/08/2016.
 */
public interface ParticipantInterfaces {
    interface View {
        void setParticipantName (String name);
        void setParticipantEmail (String email);
        void setParticipantPhone (String phone);
        void showNameError (boolean value);
        void showEmailError(boolean value);
        void showPhoneError(boolean value);
        void onSuccess ();
    }

    interface UserActionsListener {
        void validateInputFields (String name, String phone, String email);
        void addParticipant(Participant participant);
        void updateParticipant (Participant participant);
    }
}
