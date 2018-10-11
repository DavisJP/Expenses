package com.davismiyashiro.expenses.view.opentab;

import android.support.v4.util.ArrayMap;

import com.davismiyashiro.expenses.datatypes.Tab;
import com.davismiyashiro.expenses.model.TabRepository;

/**
 * @Author Davis Miyashiro
 */

public class ReceiptFragmentPresenter implements ReceiptInterfaces.UserActionsListener{

    ReceiptInterfaces.ReceiptView mReceiptView;
    TabRepository mRepository;

    public ReceiptFragmentPresenter (TabRepository model) {
        mRepository = model;
    }

    @Override
    public void setReceiptView(ReceiptInterfaces.ReceiptView view) {
        mReceiptView = view;
    }

    @Override
    public void loadReceipItems(Tab tab) {
        if (tab!= null) {
            mRepository.refreshData(); // Fix to get Expenses list updated when switching tabs
            mRepository.getReceiptItemsByTabId(tab.getGroupId(), items -> {
                if (items == null || items.isEmpty()) {
                    items = new ArrayMap<>();
                }
                mReceiptView.showReceiptItems(items);
            });
        }
    }
}
