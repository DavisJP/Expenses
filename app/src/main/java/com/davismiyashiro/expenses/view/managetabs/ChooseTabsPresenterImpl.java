package com.davismiyashiro.expenses.view.managetabs;

import com.davismiyashiro.expenses.model.TabRepository;

/**
 * Created by Davis on 23/01/2016.
 */
public class ChooseTabsPresenterImpl implements ChooseTabsInterfaces.UserActionsListener{
    private ChooseTabsInterfaces.View mView;
    private TabRepository mModel;

    public ChooseTabsPresenterImpl (ChooseTabsInterfaces.View view, TabRepository repository) {
        mView = view;
        mModel = repository;
    }

    @Override
    public void addTab() {
        mView.showAddTab();
    }

    @Override
    public void loadTabs(boolean forceUpdate) {
        mView.setProgressIndicator(true);
        if (forceUpdate) {
            mModel.refreshData();
        }

        mModel.getTabs(tabs -> {
            mView.setProgressIndicator(false);

            if (tabs.isEmpty()) {
                mView.showNoTabs();
            } else {
                mView.showTabs(tabs);
            }
        });
    }

    @Override
    public void openTab(String tabId) {
        mModel.getTab(tabId, tab -> mView.showTabDetailUi(tab));
    }

    @Override
    public void removeTab (String tabId) {
        mModel.deleteTab(tabId);
    }
}
