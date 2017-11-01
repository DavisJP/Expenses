package com.davismiyashiro.expenses.view.addtab;

import com.davismiyashiro.expenses.datatypes.Tab;

/**
 * Created by Davis on 26/08/2016.
 */
public class AddTabInterfaces {
    interface View {
        void showTabNameError ();
        void setTabName (String name);
        void setTabValid ();
        void updatedTab (Tab tab);
    }

    interface UserActionsListener {
        void addTab(String name);
        void loadTab ();
        void updateTab(String name);
    }
}
