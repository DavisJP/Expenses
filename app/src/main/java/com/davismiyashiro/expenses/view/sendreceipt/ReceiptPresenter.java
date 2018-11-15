package com.davismiyashiro.expenses.view.sendreceipt;

import com.davismiyashiro.expenses.datatypes.Tab;
import com.davismiyashiro.expenses.model.Repository;

/**
 * Created by Davis on 29/07/2016.
 */
public class ReceiptPresenter implements ReceiptInterfaces.UserActionsListener{

    Repository mRepo;

    public ReceiptPresenter (ReceiptInterfaces.View view, Repository model) {
        mRepo = model;
    }

    @Override
    public void saveTab(Tab tab) {
        mRepo.saveTab(tab);
    }
}
