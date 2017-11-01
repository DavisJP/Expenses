package com.davismiyashiro.expenses.view.opentab;

import com.davismiyashiro.expenses.datatypes.Participant;
import com.davismiyashiro.expenses.datatypes.Tab;

import java.util.List;

/**
 * Created by Davis Miyashiro on 09/12/2016.
 */

public interface ParticipantInterfaces {
    interface ParticipantView {
        void showParticipants (List<Participant> participants);
    }

    interface UserActionsListener {

        void setParticipantView (ParticipantView view);

        void loadParticipants (Tab tab);

        void removeParticipant (Participant participant);

    }
}
