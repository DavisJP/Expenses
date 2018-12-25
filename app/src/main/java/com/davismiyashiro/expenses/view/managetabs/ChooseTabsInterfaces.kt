package com.davismiyashiro.expenses.view.managetabs

import com.davismiyashiro.expenses.datatypes.Tab

/**
 * Created by Davis on 26/08/2016.
 */
interface ChooseTabsInterfaces {
    interface View {
        fun setProgressIndicator(active: Boolean)
        fun showTabs(tabs: MutableList<Tab>)
        fun showNoTabs()
        fun showAddTab()
        fun showTabDetailUi(tab: Tab)
    }

    interface UserActionsListener {
        fun addTab()
        fun loadTabs(forceUpdate: Boolean)
        fun openTab(tabId: String)
        fun removeTab(tabId: String)
    }
}
