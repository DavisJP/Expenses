package com.davismiyashiro.expenses.view.sendreceipt;

import com.davismiyashiro.expenses.datatypes.Tab;
import com.davismiyashiro.expenses.model.TabRepository;

/**
 * Created by Davis on 29/07/2016.
 */
public class ReceiptPresenter implements ReceiptInterfaces.UserActionsListener{

    TabRepository mRepo;

    public ReceiptPresenter (ReceiptInterfaces.View view, TabRepository model) {
        mRepo = model;
    }

    @Override
    public void saveTab(Tab tab) {
        mRepo.saveTab(tab);
    }
}
