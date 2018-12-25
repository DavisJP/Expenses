package com.davismiyashiro.expenses.view.managetabs

import com.davismiyashiro.expenses.datatypes.Tab
import com.davismiyashiro.expenses.model.Repository

/**
 * Created by Davis on 23/01/2016.
 */
class ChooseTabsPresenterImpl(private val mView: ChooseTabsInterfaces.View, private val mModel: Repository) : ChooseTabsInterfaces.UserActionsListener {

    override fun addTab() {
        mView.showAddTab()
    }

    override fun loadTabs(forceUpdate: Boolean) {
        mView.setProgressIndicator(true)
        if (forceUpdate) {
            mModel.refreshData()
        }

        mModel.getTabs(object : Repository.LoadTabsCallback {
            override fun onTabsLoaded(tabs: MutableList<Tab>) {
                mView.setProgressIndicator(false)
                if (tabs.isEmpty()) {
                    mView.showNoTabs()
                } else {
                    mView.showTabs(tabs)
                }
            }
        })
    }

    override fun openTab(tabId: String) {
        mModel.getTab(tabId, object : Repository.GetTabCallback {
            override fun onTabLoaded(tab: Tab) {
                mView.showTabDetailUi(tab)
            }
        })
    }

    override fun removeTab(tabId: String) {
        mModel.deleteTab(tabId)
    }
}
