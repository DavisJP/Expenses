package com.davismiyashiro.expenses.view.managetabs;

import com.davismiyashiro.expenses.datatypes.Tab;

import java.util.List;

/**
 * Created by Davis on 26/08/2016.
 */
public interface ChooseTabsInterfaces {
    interface View {
        void setProgressIndicator(boolean active);
        void showTabs(List<Tab> tabs);
        void showNoTabs();
        void showAddTab();
        void showTabDetailUi(Tab tab);
    }

    interface UserActionsListener {
        void addTab();
        void loadTabs(boolean forceUpdate);
        void openTab(String tabId);
        void removeTab(String tabId);
    }
}
