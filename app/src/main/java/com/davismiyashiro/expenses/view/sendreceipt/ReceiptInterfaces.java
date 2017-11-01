package com.davismiyashiro.expenses.view.sendreceipt;

import com.davismiyashiro.expenses.datatypes.Tab;

/**
 * Created by Davis on 29/07/2016.
 */
public interface ReceiptInterfaces {

    interface View {
        void showReceipt();
    }

    interface UserActionsListener {
        void saveTab(Tab tab);
    }
}
