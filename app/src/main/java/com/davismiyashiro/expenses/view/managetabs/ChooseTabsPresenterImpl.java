package com.davismiyashiro.expenses.view.managetabs;

import com.davismiyashiro.expenses.datatypes.Tab;
import com.davismiyashiro.expenses.model.TabRepository;

import java.util.List;

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

        mModel.getTabs(new TabRepository.LoadTabsCallback() {
            @Override
            public void onTabsLoaded(List<Tab> tabs) {
                mView.setProgressIndicator(false);

                if (tabs.isEmpty()) {
                    mView.showNoTabs();
                } else {
                    mView.showTabs(tabs);
                }
            }
        });
    }

    @Override
    public void openTab(String tabId) {
        mModel.getTab(tabId, new TabRepository.GetTabCallback() {
            @Override
            public void onTabLoaded(Tab tab) {
                mView.showTabDetailUi(tab);
            }
        });
    }

    @Override
    public void removeTab (String tabId) {
        mModel.deleteTab(tabId);
    }
}
